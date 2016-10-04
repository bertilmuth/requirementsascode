package org.requirementsascode;

import static org.requirementsascode.UseCaseStepCondition.afterStep;
import static org.requirementsascode.UseCaseStepCondition.atFirstStep;
import static org.requirementsascode.UseCaseStepCondition.isSystemInDifferentFlowThan;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class UseCaseFlow extends UseCaseModelElement {
	private UseCase useCase;
	private Optional<Predicate<UseCaseRunner>> optionalStepPredicate;
	private Predicate<UseCaseRunner> completePredicate;

	public UseCaseFlow(String name, UseCase useCase) {
		super(name, useCase.getModel());

		Objects.requireNonNull(useCase);
		this.useCase = useCase;
		this.optionalStepPredicate = Optional.empty();
	}

	public UseCase getUseCase() {
		return useCase;
	}
	
	public UseCase continueAfter(String stepName) {
		Objects.requireNonNull(stepName);

		continueAfter(stepName, null, completePredicate);
		return getUseCase();
	}

	private void continueAfter(String continueAfterStepName, UseCaseStep stepBeforeJumpHappens,
			Predicate<UseCaseRunner> predicate) {
		UseCaseStep continueAfterStep = getUseCase().getStep(continueAfterStepName);
		String stepWhereJumpHappensName = uniqueStepWhereJumpHappensName(continueAfterStepName);

		newStep(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate).system(jumpTo(continueAfterStep));
	}
	
	void continueAfter(String continueAfterStepName, UseCaseStep stepBeforeJumpHappens) {
		Objects.requireNonNull(continueAfterStepName);
		Objects.requireNonNull(stepBeforeJumpHappens);

		continueAfter(continueAfterStepName, stepBeforeJumpHappens, null);
	}
	
	public UseCaseStep newStep(String stepName) {
		Objects.requireNonNull(stepName);

		UseCaseStep newStep = newStep(stepName, null, completePredicate);

		return newStep;
	}

	private UseCaseStep newStep(String stepName, UseCaseStep previousStep, Predicate<UseCaseRunner> predicate) {
		UseCaseStep stepToLeave = getUseCase().newStep(stepName, this, previousStep, predicate);
		return stepToLeave;
	}

	Runnable jumpTo(UseCaseStep stepToContinueAfter) {
		return () -> getModel().getUseCaseRunner().setLatestStep(stepToContinueAfter);
	}

	public UseCaseFlow atFirst() {
		setCompleteStepPredicate(alternativeFlowPredicate().and(atFirstStep()));
		return this;
	}
	private void setCompleteStepPredicate(Predicate<UseCaseRunner> stepPredicate){
		this.optionalStepPredicate = Optional.of(stepPredicate);
		this.completePredicate = stepPredicate;
	}

	public UseCaseFlow after(String stepName) {
		Objects.requireNonNull(stepName);

		UseCaseStep useCaseStep = useCase.getStep(stepName);
		setCompleteStepPredicate(alternativeFlowPredicate().and(afterStep(useCaseStep)));

		return this;
	}

	public UseCaseFlow when(Predicate<UseCaseRunner> whenPredicate) {
		Objects.requireNonNull(whenPredicate);

		completePredicate = optionalStepPredicate
			.orElse(alternativeFlowPredicate())
			.and(whenPredicate);
		return this;
	}
	
	private Predicate<UseCaseRunner> alternativeFlowPredicate() {
		return isSystemInDifferentFlowThan(this);
	}
	
	protected String uniqueStepWhereJumpHappensName(String stepName) {
		return uniqueStepName("Continue after " + stepName);
	}
}
