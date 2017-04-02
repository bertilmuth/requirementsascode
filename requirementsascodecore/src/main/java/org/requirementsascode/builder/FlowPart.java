package org.requirementsascode.builder;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCase;
import org.requirementsascode.Flow;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.NoSuchElementInModel;
import org.requirementsascode.predicate.After;
import org.requirementsascode.predicate.InsteadOf;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 * 
 * @see Flow
 * @author b_muth
 *
 */
public class FlowPart {
	private Flow useCaseFlow;
	private UseCase useCase;
	private UseCasePart useCasePart;
	private FlowPredicate flowPredicate;

	public FlowPart(Flow useCaseFlow, UseCasePart useCasePart) {
		this.useCaseFlow = useCaseFlow;
		this.useCasePart = useCasePart;
		this.useCase = useCasePart.useCase();
		this.flowPredicate = new FlowPredicate();
	}

	/**
	 * Creates the first step of this flow, with the specified name.
	 * 
	 * @param stepName the name of the step to be created
	 * @return the newly created step, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
	 */
	public UseCaseStepPart step(String stepName) {
		UseCaseStep useCaseStep = 
			useCasePart.useCase().newStep(stepName, useCaseFlow, 
				Optional.empty(), flowPredicate());
		return new UseCaseStepPart(useCaseStep, this);
	}

	/**
	 * Returns the predicate for this flow
	 * (that will become the predicate of its first step).
	 * 
	 * @return the flow's predicate
	 */
	public Optional<Predicate<UseCaseModelRunner>> flowPredicate() {
		return flowPredicate.get();
	}
	
	/**
	 * Starts the flow after the specified step has been run, 
	 * in this flow's use case. You should use after to handle exceptions
	 * that occured in the specified step.
	 * 
	 * @param stepName the name of the step to start the flow after
	 * @return this use case flow, to ease creation of the predicate and the first step of the flow
	 * @throws NoSuchElementInModel if the specified step is not found in this flow's use case
	 */
	public FlowPart after(String stepName) {
		UseCaseStep step = useCase.findStep(stepName);
		flowPredicate.setStepPredicate(new After(Optional.of(step)));
		return this;
	}
	
	/**
	 * Starts the flow as an alternative to the specified step,
	 * in this flow's use case.
	 * 
	 * @param stepName the name of the specified step
	 * @return this use case flow, to ease creation of the predicate and the first step of the flow
	 * @throws NoSuchElementInModel if the specified step is not found in this flow's use case
	 */
	public FlowPart insteadOf(String stepName) {
		UseCaseStep step = useCase.findStep(stepName);
		flowPredicate.setStepPredicate(new InsteadOf(step));
	
		return this;	
	}
	
	/**
	 * Constrains the flow's predicate: only if the specified predicate is
	 * true as well (beside the step condition), the flow is started.
	 * 
	 * @param whenPredicate the condition that constrains when the flow is started
	 * @return this use case flow, to ease creation of the predicate and the first step of the flow
	 */
	public FlowPart when(Predicate<UseCaseModelRunner> whenPredicate) {
		Objects.requireNonNull(whenPredicate);
		flowPredicate.setWhenPredicate(whenPredicate);
		return this;
	}
	
	public Flow useCaseFlow(){
		return useCaseFlow;
	}
	
	public UseCasePart useCasePart(){
		return useCasePart;
	}

	public UseCaseModelBuilder useCaseModelBuilder(){
		return useCasePart.useCaseModelBuilder();
	}
	
	private class FlowPredicate{
		private Optional<Predicate<UseCaseModelRunner>> predicate;

		private FlowPredicate() {
			this.predicate = Optional.empty();
		}
		
		private void setStepPredicate(Predicate<UseCaseModelRunner> stepPredicate){
			predicate = Optional.of(stepPredicate);
		}
		
		public void setWhenPredicate(Predicate<UseCaseModelRunner> whenPredicate){
			predicate = Optional.of(predicate.orElse(r -> true).and(whenPredicate));
		}
		
		public Optional<Predicate<UseCaseModelRunner>> get(){
			return predicate.map(pred -> isRunnerInDifferentFlow().and(pred));
		}
		
		private Predicate<UseCaseModelRunner> isRunnerInDifferentFlow() {			
			Predicate<UseCaseModelRunner> isRunnerInDifferentFlow = 
				runner -> runner.latestFlow().map(
					runnerFlow -> !useCaseFlow.equals(runnerFlow)).orElse(true);
			return isRunnerInDifferentFlow;
		}
	}
}
