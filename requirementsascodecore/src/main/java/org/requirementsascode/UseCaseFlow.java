package org.requirementsascode;

import static org.requirementsascode.UseCaseStepPredicate.afterStep;
import static org.requirementsascode.UseCaseStepPredicate.atFirstStep;
import static org.requirementsascode.UseCaseStepPredicate.isRunnerInDifferentFlowThan;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseStep.SystemPart;
import org.requirementsascode.exception.NoSuchElementInUseCaseException;

public class UseCaseFlow extends UseCaseModelElement {
	private UseCase useCase;
	private FlowPredicate flowPredicate;

	public UseCaseFlow(String name, UseCase useCase) {
		super(name, useCase.getUseCaseModel());
		
		this.useCase = useCase;
		this.flowPredicate = new FlowPredicate();
	}

	public UseCase getUseCase() {
		return useCase;
	}
	
	public void continueAtFirst() {
		continueAtFirst(Optional.empty(), flowPredicate.get());	
	}

	void continueAtFirst(Optional<UseCaseStep> stepBeforeJumpHappens, Optional<Predicate<UseCaseRunner>> predicate) {
		String stepWhereJumpHappensName = uniqueStepWhereJumpHappensNameToContinueAtFirst();
		newStepWhereJumpHappens(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate, Optional.empty());
	}

	private SystemPart<?> newStepWhereJumpHappens(String stepWhereJumpHappensName, Optional<UseCaseStep> stepBeforeJumpHappens,
			Optional<Predicate<UseCaseRunner>> predicate, Optional<UseCaseStep> continueAfterStep) {
		return newStep(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate).system(jumpTo(continueAfterStep));
	}
	
	public UseCase continueAfter(String stepName) {
		Objects.requireNonNull(stepName);

		continueAfter(stepName, Optional.empty(), flowPredicate.get());
		return getUseCase();
	}

	void continueAfter(String continueAfterStepName, Optional<UseCaseStep> stepBeforeJumpHappens, Optional<Predicate<UseCaseRunner>> predicate) {
		Optional<UseCaseStep> continueAfterStep = getUseCase().findStep(continueAfterStepName);
		String stepWhereJumpHappensName = uniqueStepWhereJumpHappensNameToContinueAfter(continueAfterStepName);

		continueAfterStep.map(s -> 
			newStepWhereJumpHappens(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate, continueAfterStep))
			.orElseThrow(() -> new NoSuchElementInUseCaseException(continueAfterStepName));
	}
	
	private Runnable jumpTo(Optional<UseCaseStep> continueAfterStep) {
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

	public UseCaseFlow atFirst() {
		flowPredicate.step(atFirstStep());
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
	
	protected String uniqueStepWhereJumpHappensNameToContinueAtFirst() {
		return uniqueStepName("Continue at first step");
	}
	
	protected String uniqueStepWhereJumpHappensNameToContinueAfter(String stepName) {
		return uniqueStepName("Continue after " + stepName);
	}
}
