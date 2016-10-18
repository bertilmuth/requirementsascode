package org.requirementsascode;

import static org.requirementsascode.UseCaseStepPredicate.afterStep;
import static org.requirementsascode.UseCaseStepPredicate.isRunnerAtStart;
import static org.requirementsascode.UseCaseStepPredicate.isRunnerInDifferentFlowThan;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseStep.SystemPart;
import org.requirementsascode.exception.NoSuchElementInUseCaseException;

/**
 * A use case flow defines a sequence of steps that lead the user through the use case.
 * 
 * A flow either ends with the user reaching her goal, or terminates before, usually
 * because of an error that occured.
 * 
 * A flow has a predicate. The predicate defines which condition must be fulfilled in order 
 * for the system to enter the flow, and react to its first step.
 * 
 * If the flow's condition is still fulfilled or fulfilled again while running through the
 * flow's step, the flow is NOT reentered. Rather, the flow is exited if a condition of
 * a different flow is fulfilled.
 * 
 * @author b_muth
 *
 */
public class UseCaseFlow extends UseCaseModelElement {
	private UseCase useCase;
	private FlowPredicate flowPredicate;

	UseCaseFlow(String name, UseCase useCase) {
		super(name, useCase.getUseCaseModel());
		
		this.useCase = useCase;
		this.flowPredicate = new FlowPredicate();
	}

	/**
	 * Returns the use case this flow is part of.
	 * 
	 * @return the containing use case
	 */
	public UseCase getUseCase() {
		return useCase;
	}
	
	/**
	 * Jump back to start, as if the {@link UseCaseRunner} had just been started or restarted.
	 */
	public void continueAtStart() {
		continueAtStart(Optional.empty(), flowPredicate.get());	
	}

	UseCase continueAtStart(Optional<UseCaseStep> stepBeforeJumpHappens, Optional<Predicate<UseCaseRunner>> predicate) {
		String stepWhereJumpHappensName = uniqueStepWhereJumpHappensNameToContinueAtStart();
		newStepWhereJumpHappens(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate, Optional.empty());
		return getUseCase();
	}

	private SystemPart<?> newStepWhereJumpHappens(String stepWhereJumpHappensName, Optional<UseCaseStep> stepBeforeJumpHappens,
			Optional<Predicate<UseCaseRunner>> predicate, Optional<UseCaseStep> continueAfterStep) {
		return newStep(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate).system(jumpAfter(continueAfterStep));
	}
	
	public UseCase continueAfter(String stepName) {
		Objects.requireNonNull(stepName);

		return continueAfter(stepName, Optional.empty(), flowPredicate.get());
	}

	UseCase continueAfter(String continueAfterStepName, Optional<UseCaseStep> stepBeforeJumpHappens, Optional<Predicate<UseCaseRunner>> predicate) {
		Optional<UseCaseStep> continueAfterStep = getUseCase().findStep(continueAfterStepName);
		String stepWhereJumpHappensName = uniqueStepWhereJumpHappensNameToContinueAfter(continueAfterStepName);

		continueAfterStep.map(s -> 
			newStepWhereJumpHappens(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate, continueAfterStep))
			.orElseThrow(() -> new NoSuchElementInUseCaseException(continueAfterStepName));
		return getUseCase();
	}
	
	private Runnable jumpAfter(Optional<UseCaseStep> continueAfterStep) {
		return () -> getUseCaseModel().getUseCaseRunner().setLatestStep(continueAfterStep);
	}
	
	public UseCaseStep newStep(String stepName) {
		Objects.requireNonNull(stepName);

		UseCaseStep newStep = newStep(stepName, Optional.empty(), flowPredicate.get());

		return newStep;
	}

	private UseCaseStep newStep(String stepName, Optional<UseCaseStep> previousStep, Optional<Predicate<UseCaseRunner>> predicate) {
		UseCaseStep stepToLeave = getUseCase().newStep(stepName, this, previousStep, predicate);
		return stepToLeave;
	}

	public UseCaseFlow atStart() {
		flowPredicate.step(isRunnerAtStart());
		return this;
	}
	
	public UseCaseFlow after(String stepName, String useCaseName) {
		Objects.requireNonNull(stepName);
		Objects.requireNonNull(useCaseName);
		
		UseCase useCase = getUseCaseModel().findUseCase(useCaseName)
			.orElseThrow(() -> new NoSuchElementInUseCaseException(stepName));
		return after(stepName, useCase);
	}
	
	private UseCaseFlow after(String stepName, UseCase useCase) {
		Optional<UseCaseStep> foundStep = useCase.findStep(stepName);
		flowPredicate.step(afterStep(foundStep
			.orElseThrow(() -> new NoSuchElementInUseCaseException(stepName))));
		return this;
	}

	public UseCaseFlow after(String stepName) {
		return after(stepName, useCase);
	}

	public UseCaseFlow when(Predicate<UseCaseRunner> whenPredicate) {
		Objects.requireNonNull(whenPredicate);

		flowPredicate.when(whenPredicate);
		return this;
	}
	
	private class FlowPredicate{
		private Optional<Predicate<UseCaseRunner>> predicate;

		private FlowPredicate() {
			this.predicate = Optional.empty();
		}
		
		private void step(Predicate<UseCaseRunner> stepPredicate){
			predicate = Optional.of(alternativeFlowPredicate().and(stepPredicate));
		}
		
		public void when(Predicate<UseCaseRunner> whenPredicate){
			predicate = Optional.of(
				predicate.orElse(alternativeFlowPredicate()).and(whenPredicate));
		}
		
		public Optional<Predicate<UseCaseRunner>> get(){
			return predicate;
		}
		
		private Predicate<UseCaseRunner> alternativeFlowPredicate() {
			return isRunnerInDifferentFlowThan(UseCaseFlow.this);
		}
	}
	
	protected String uniqueStepWhereJumpHappensNameToContinueAtStart() {
		return uniqueStepName("Continue at first step");
	}
	
	protected String uniqueStepWhereJumpHappensNameToContinueAfter(String stepName) {
		return uniqueStepName("Continue after " + stepName);
	}
}
