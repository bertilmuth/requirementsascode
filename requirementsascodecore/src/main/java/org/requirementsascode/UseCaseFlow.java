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
	private Optional<Predicate<UseCaseRunner>> stepPredicate;
	private Optional<Predicate<UseCaseRunner>> completePredicate;

	public UseCaseFlow(String name, UseCase useCase) {
		super(name, useCase.getUseCaseModel());
		
		this.useCase = useCase;
		this.stepPredicate = Optional.empty();
		this.completePredicate = Optional.empty();
	}

	public UseCase getUseCase() {
		return useCase;
	}
	
	public UseCase continueAfter(String stepName) {
		Objects.requireNonNull(stepName);

		continueAfter(stepName, Optional.empty(), completePredicate);
		return getUseCase();
	}

	private void continueAfter(String continueAfterStepName, Optional<UseCaseStep> stepBeforeJumpHappens, Optional<Predicate<UseCaseRunner>> predicate) {
		Optional<UseCaseStep> continueAfterStep = getUseCase().findStep(continueAfterStepName);
		String stepWhereJumpHappensName = uniqueStepWhereJumpHappensName(continueAfterStepName);

		continueAfterStep.map(step -> 
			newStep(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate).system(continueAfterRunnable(step)))
			.orElseThrow(() -> new NoSuchElementInUseCaseException(continueAfterStepName));
	}
	
	void continueAfter(String continueAfterStepName, UseCaseStep stepBeforeJumpHappens) {
		Objects.requireNonNull(continueAfterStepName);
		Objects.requireNonNull(stepBeforeJumpHappens);

		continueAfter(continueAfterStepName, Optional.of(stepBeforeJumpHappens), Optional.empty());
	}
	
	private Runnable continueAfterRunnable(UseCaseStep stepToContinueAfter) {
		return () -> getUseCaseModel().getUseCaseRunner().setLatestStep(Optional.of(stepToContinueAfter));
	}
	
	public UseCaseStep newStep(String stepName) {
		Objects.requireNonNull(stepName);

		UseCaseStep newStep = newStep(stepName, Optional.empty(), completePredicate);

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
		this.stepPredicate = Optional.of(stepPredicate);
		this.completePredicate = Optional.of(stepPredicate);
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

		completePredicate = 
		  Optional.of(stepPredicate
			.orElse(alternativeFlowPredicate())
			.and(whenPredicate));
		return this;
	}
	
	public Predicate<UseCaseRunner> alternativeFlowPredicate() {
		return isRunnerInDifferentFlowThan(this);
	}
	
	protected String uniqueStepWhereJumpHappensName(String stepName) {
		return uniqueStepName("Continue after " + stepName);
	}
}
