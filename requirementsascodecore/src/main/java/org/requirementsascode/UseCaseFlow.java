package org.requirementsascode;

import static org.requirementsascode.UseCaseStepCondition.afterStep;
import static org.requirementsascode.UseCaseStepCondition.atFirstStep;
import static org.requirementsascode.UseCaseStepCondition.isRunnerInDifferentFlowThan;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.exception.NoSuchElementInUseCaseException;

public class UseCaseFlow extends UseCaseModelElement {
	private UseCase useCase;
	private Optional<Predicate<UseCaseRunner>> optionalStepPredicate;
	private Predicate<UseCaseRunner> completePredicate;

	public UseCaseFlow(String name, UseCase useCase) {
		super(name, useCase.getUseCaseModel());

		Objects.requireNonNull(useCase);
		this.useCase = useCase;
		this.optionalStepPredicate = Optional.empty();
	}

	public UseCase getUseCase() {
		return useCase;
	}
	
	public UseCase continueAfter(String stepName) {
		Objects.requireNonNull(stepName);

		continueAfter(stepName, Optional.empty(), completePredicate);
		return getUseCase();
	}

	private void continueAfter(String continueAfterStepName, Optional<UseCaseStep> optionalStepBeforeJumpHappens,
			Predicate<UseCaseRunner> predicate) {
		Optional<UseCaseStep> continueAfterStep = getUseCase().findStep(continueAfterStepName);
		String stepWhereJumpHappensName = uniqueStepWhereJumpHappensName(continueAfterStepName);

		continueAfterStep.map(step -> 
			newStep(stepWhereJumpHappensName, optionalStepBeforeJumpHappens, predicate).system(jumpTo(step)))
			.orElseThrow(() -> new NoSuchElementInUseCaseException(continueAfterStepName));
	}
	
	void continueAfter(String continueAfterStepName, UseCaseStep stepBeforeJumpHappens) {
		Objects.requireNonNull(continueAfterStepName);
		Objects.requireNonNull(stepBeforeJumpHappens);

		continueAfter(continueAfterStepName, Optional.of(stepBeforeJumpHappens), null);
	}
	
	public UseCaseStep newStep(String stepName) {
		Objects.requireNonNull(stepName);

		UseCaseStep newStep = newStep(stepName, Optional.empty(), completePredicate);

		return newStep;
	}

	private UseCaseStep newStep(String stepName, Optional<UseCaseStep> optionalPreviousStep, Predicate<UseCaseRunner> predicate) {
		UseCaseStep stepToLeave = getUseCase().newStep(stepName, this, optionalPreviousStep, predicate);
		return stepToLeave;
	}

	Runnable jumpTo(UseCaseStep stepToContinueAfter) {
		return () -> getUseCaseModel().getUseCaseRunner().setLatestStep(stepToContinueAfter);
	}

	public UseCaseFlow atFirst() {
		setCompleteStepPredicate(alternativeFlowPredicate().and(atFirstStep()));
		return this;
	}
	private void setCompleteStepPredicate(Predicate<UseCaseRunner> stepPredicate){
		this.optionalStepPredicate = Optional.of(stepPredicate);
		this.completePredicate = stepPredicate;
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

		completePredicate = optionalStepPredicate
			.orElse(alternativeFlowPredicate())
			.and(whenPredicate);
		return this;
	}
	
	public Predicate<UseCaseRunner> alternativeFlowPredicate() {
		return isRunnerInDifferentFlowThan(this);
	}
	
	protected String uniqueStepWhereJumpHappensName(String stepName) {
		return uniqueStepName("Continue after " + stepName);
	}
}
