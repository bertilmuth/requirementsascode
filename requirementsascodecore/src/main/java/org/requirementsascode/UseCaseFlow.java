package org.requirementsascode;

import static org.requirementsascode.UseCaseStepPredicate.afterStep;
import static org.requirementsascode.UseCaseStepPredicate.atFirstStep;
import static org.requirementsascode.UseCaseStepPredicate.isRunnerInDifferentFlowThan;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

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
	
	public UseCase continueAfter(String stepName) {
		Objects.requireNonNull(stepName);

		continueAfter(stepName, Optional.empty(), flowPredicate.get());
		return getUseCase();
	}

	void continueAfter(String continueAfterStepName, Optional<UseCaseStep> stepBeforeJumpHappens, Optional<Predicate<UseCaseRunner>> predicate) {
		Optional<UseCaseStep> continueAfterStep = getUseCase().findStep(continueAfterStepName);
		String stepWhereJumpHappensName = uniqueStepWhereJumpHappensName(continueAfterStepName);

		continueAfterStep.map(step -> 
			newStep(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate).system(continueAfterRunnable(step)))
			.orElseThrow(() -> new NoSuchElementInUseCaseException(continueAfterStepName));
	}
	
	private Runnable continueAfterRunnable(UseCaseStep stepToContinueAfter) {
		return () -> getUseCaseModel().getUseCaseRunner().setLatestStep(Optional.of(stepToContinueAfter));
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
		setCompleteStepPredicate(alternativeFlowPredicate().and(atFirstStep()));
		return this;
	}
	
	private void setCompleteStepPredicate(Predicate<UseCaseRunner> stepPredicate){
		flowPredicate.setStepPredicate(stepPredicate);
	}
	
	public UseCaseFlow after(String stepName, String useCaseName) {
		Objects.requireNonNull(stepName);
		Objects.requireNonNull(useCaseName);
		
		UseCase useCase = getUseCaseModel().findUseCase(useCaseName)
			.orElseThrow(() -> new NoSuchElementInUseCaseException(stepName));
		return after(stepName, useCase);
	}
	
	private UseCaseFlow after(String stepName, UseCase useCase) {
		NoSuchElementInUseCaseException exception = new NoSuchElementInUseCaseException(stepName);
		Optional<UseCaseStep> foundStep = useCase.findStep(stepName);
		foundStep.orElseThrow(() -> exception);
		setCompleteStepPredicate(alternativeFlowPredicate().and(afterStep(foundStep)));
		return this;
	}

	public UseCaseFlow after(String stepName) {
		return after(stepName, useCase);
	}

	public UseCaseFlow when(Predicate<UseCaseRunner> whenPredicate) {
		Objects.requireNonNull(whenPredicate);

		flowPredicate.setWhenPredicate(whenPredicate);
		return this;
	}
	
	public Predicate<UseCaseRunner> alternativeFlowPredicate() {
		return isRunnerInDifferentFlowThan(this);
	}
	
	private class FlowPredicate{
		private Optional<Predicate<UseCaseRunner>> predicate;

		public FlowPredicate() {
			this.predicate = Optional.empty();
		}
		
		public void setStepPredicate(Predicate<UseCaseRunner> stepPredicate){
			predicate = Optional.of(stepPredicate);
		}
		
		public void setWhenPredicate(Predicate<UseCaseRunner> whenPredicate){
			predicate = Optional.of(
				predicate.orElse(alternativeFlowPredicate()).and(whenPredicate));
		}
		
		public Optional<Predicate<UseCaseRunner>> get(){
			return predicate;
		}
	}
	
	protected String uniqueStepWhereJumpHappensName(String stepName) {
		return uniqueStepName("Continue after " + stepName);
	}
}
