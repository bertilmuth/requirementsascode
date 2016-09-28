package org.requirementsascode;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import static org.requirementsascode.UseCaseStepCondition.*;

public class UseCaseFlow extends UseCaseModelElement{
	private UseCase useCase;
	private ConditionalPart conditionalPart;

	public UseCaseFlow(String name, UseCase useCase) {
		super(name, useCase.getUseCaseModel());

		Objects.requireNonNull(useCase);
		this.useCase = useCase;
		this.useCase.addFlow(this);
	}

	public UseCase getUseCase() {
		return useCase;
	}
	
	ConditionalPart newConditionalPart() {
		conditionalPart = new ConditionalPart();
		return conditionalPart;
	}
	
	void continueAfter(String continueAfterStepName, UseCaseStep stepBeforeJumpHappens) {		
		Objects.requireNonNull(continueAfterStepName);
		Objects.requireNonNull(stepBeforeJumpHappens);

		continueAfter(continueAfterStepName, stepBeforeJumpHappens, null);
	}
	
	private void continueAfter(String continueAfterStepName, UseCaseStep stepBeforeJumpHappens, Predicate<UseCaseModelRun> predicate) {
		UseCaseStep continueAfterStep = getUseCase().getStep(continueAfterStepName);
		String stepWhereJumpHappensName = uniqueStepWhereJumpHappensName(continueAfterStepName);
		
		newStep(stepWhereJumpHappensName, stepBeforeJumpHappens, predicate)
			.system(jumpTo(continueAfterStep));
	}

	private UseCaseStep newStep(String stepName, UseCaseStep previousStep, Predicate<UseCaseModelRun> predicate) {
		UseCaseStep stepToLeave = getUseCase().newStep(
				stepName, 
				this, 
				previousStep, 
				predicate);
		return stepToLeave;
	}
	
	Runnable jumpTo(UseCaseStep stepToContinueAfter) {
		return () -> getUseCaseModel().run().setLatestStep(stepToContinueAfter);
	}
	
	private String uniqueStepWhereJumpHappensName(String stepName) {
		return UUID.randomUUID() + ": Continue with " + stepName;
	}
	
	public class ConditionalPart {
		private Predicate<UseCaseModelRun> stepPredicate;
		private Predicate<UseCaseModelRun> completePredicate;


		private ConditionalPart() {
			stepPredicate = inDifferentFlow();
		}

		private Predicate<UseCaseModelRun> inDifferentFlow() {
			Predicate<UseCaseModelRun> inDifferentFlowPredicate = 
				isSystemInDifferentFlowThan(UseCaseFlow.this);
			return inDifferentFlowPredicate;
		}

		public UseCaseStep newStep(String stepName) {
			Objects.requireNonNull(stepName);

			UseCaseStep newStep = UseCaseFlow.this.newStep(stepName, null, completePredicate);

			return newStep;
		}
		
		public ConditionalPart atFirst() {
			stepPredicate = inDifferentFlow().and(atFirstStep());
			completePredicate = stepPredicate;
			return this;
		}

		public ConditionalPart after(String stepName) {
			Objects.requireNonNull(stepName);
			
			UseCaseStep useCaseStep = useCase.getStep(stepName);
			stepPredicate = inDifferentFlow().and(afterStep(useCaseStep));
			completePredicate = stepPredicate;

			return this;
		}
		
		public ConditionalPart when(Predicate<UseCaseModelRun> whenPredicate) {
			Objects.requireNonNull(whenPredicate);
			
			completePredicate = stepPredicate.and(whenPredicate);
			return this;
		}
		
		
		public UseCase continueAfter(String stepName) {
			Objects.requireNonNull(stepName);
			
			UseCaseFlow.this.continueAfter(stepName, null, completePredicate);
			
			return getUseCase();
		}
	}
}
