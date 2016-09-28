package org.requirementsascode;

import static org.requirementsascode.UseCaseStepCondition.afterStep;
import static org.requirementsascode.UseCaseStepCondition.atFirstStep;
import static org.requirementsascode.UseCaseStepCondition.isSystemInDifferentFlowThan;

import java.util.Objects;
import java.util.function.Predicate;

public class UseCaseFlow extends UseCaseModelElement {
	private UseCase useCase;
	private Predicate<UseCaseModelRun> stepPredicate;
	private Predicate<UseCaseModelRun> completePredicate;

	public UseCaseFlow(String name, UseCase useCase) {
		super(name, useCase.getModel());

		Objects.requireNonNull(useCase);
		this.useCase = useCase;
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
			Predicate<UseCaseModelRun> predicate) {
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

	private UseCaseStep newStep(String stepName, UseCaseStep previousStep, Predicate<UseCaseModelRun> predicate) {
		UseCaseStep stepToLeave = getUseCase().newStep(stepName, this, previousStep, predicate);
		return stepToLeave;
	}

	Runnable jumpTo(UseCaseStep stepToContinueAfter) {
		return () -> getModel().run().setLatestStep(stepToContinueAfter);
	}

	public UseCaseFlow atFirst() {
		setCompleteStepPredicate(isRunInDifferentFlow().and(atFirstStep()));
		return this;
	}
	private void setCompleteStepPredicate(Predicate<UseCaseModelRun> stepPredicate){
		this.stepPredicate = stepPredicate;
		this.completePredicate = stepPredicate;
	}

	public UseCaseFlow after(String stepName) {
		Objects.requireNonNull(stepName);

		UseCaseStep useCaseStep = useCase.getStep(stepName);
		setCompleteStepPredicate(isRunInDifferentFlow().and(afterStep(useCaseStep)));

		return this;
	}

	public UseCaseFlow when(Predicate<UseCaseModelRun> whenPredicate) {
		Objects.requireNonNull(whenPredicate);

		if(stepPredicate == null){
			this.stepPredicate = isRunInDifferentFlow();
		}
		completePredicate = stepPredicate.and(whenPredicate);
		return this;
	}
	
	private Predicate<UseCaseModelRun> isRunInDifferentFlow() {
		Predicate<UseCaseModelRun> inDifferentFlowPredicate = isSystemInDifferentFlowThan(UseCaseFlow.this);
		return inDifferentFlowPredicate;
	}
	
	protected String uniqueStepWhereJumpHappensName(String stepName) {
		return uniqueStepName("Continue after " + stepName);
	}
}
