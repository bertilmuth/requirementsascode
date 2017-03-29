package org.requirementsascode;

import static org.requirementsascode.UseCaseStepPredicates.afterStep;
import static org.requirementsascode.UseCaseStepPredicates.isRunnerAtStart;
import static org.requirementsascode.UseCaseStepPredicates.isRunnerInDifferentFlowThan;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.requirementsascode.exception.NoSuchElementInModel;

/**
 * A use case flow, as part of a use case.
 * A use case flow defines a sequence of steps that lead the user through the use case.
 * 
 * A flow either ends with the user reaching her/his goal, or terminates before, usually
 * because of an exception that occurred.
 * 
 * A flow has a predicate. The predicate defines which condition must be fulfilled in order 
 * for the system to enter the flow, and react to its first step.
 * 
 * If the flow's condition is still fulfilled or fulfilled again while running through the
 * flow's steps, the flow is NOT reentered. Rather, the flow is exited if a condition of
 * a different flow is fulfilled.
 * 
 * Under the hood, the predicate is not owned by the flow, but by the first use case step of the flow.
 * 
 * @author b_muth
 *
 */
public class UseCaseFlow extends UseCaseModelElement {
	private UseCase useCase;
	private FlowPredicate flowPredicate;

	/**
	 * Creates a use case flow with the specified name that 
	 * belongs to the specified use case.
	 * 
	 * @param name the name of the flow to be created
	 * @param useCase the use case that will contain the new flow
	 */
	UseCaseFlow(String name, UseCase useCase) {
		super(name, useCase.useCaseModel());
		
		this.useCase = useCase;
		this.flowPredicate = new FlowPredicate();
	}

	/**
	 * Returns the use case this flow is part of.
	 * 
	 * @return the containing use case
	 */
	public UseCase useCase() {
		return useCase;
	}
	
	/**
	 * Returns the predicate for this flow
	 * (that will become the predicate of its first step).
	 * 
	 * @return the flow's predicate
	 */
	public Optional<Predicate<UseCaseRunner>> flowPredicate() {
		return flowPredicate.get();
	}
	
	/**
	 * Returns the steps contained in this flow.
	 * Do not modify the returned collection directly.
	 * 
	 * @return a collection of the steps
	 */
	public List<UseCaseStep> steps() {
		return useCase().steps().stream()
			.filter(step -> step.flow().equals(this))
			.collect(Collectors.toList());
	}

	/**
	 * Sets the flow's predicate to start the flow at the beginning of the run, 
	 * when no flow and step has been run.
	 * 
	 * @return this use case flow, to ease creation of the predicate and the first step of the flow
	 */
	public UseCaseFlow atStart() {
		flowPredicate.setStepPredicate(isRunnerAtStart());
		return this;
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
	public UseCaseFlow after(String stepName) {
		UseCaseStep foundStep = useCase.findStep(stepName);
		flowPredicate.setStepPredicate(afterStep(foundStep));
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
	public UseCaseFlow insteadOf(String stepName) {
		Optional<UseCaseStep> stepBeforeAtStep = 
			useCase.findStep(stepName).previousStepInFlow();

		flowPredicate.setStepPredicate(afterStep(stepBeforeAtStep));
	
		return this;	
	}
	
	/**
	 * Constrains the flow's predicate: only if the specified predicate is
	 * true as well (beside the step condition), the flow is started.
	 * 
	 * @param whenPredicate the condition that constrains when the flow is started
	 * @return this use case flow, to ease creation of the predicate and the first step of the flow
	 */
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
		
		public void when(Predicate<UseCaseRunner> whenPredicate){
			predicate = Optional.of(
				predicate.orElse(isRunnerInDifferentFlow()).and(whenPredicate));
		}
		
		public Optional<Predicate<UseCaseRunner>> get(){
			return predicate;
		}
		
		private void setStepPredicate(Predicate<UseCaseRunner> stepPredicate){
			predicate = Optional.of(isRunnerInDifferentFlow().and(stepPredicate));
		}
		
		private Predicate<UseCaseRunner> isRunnerInDifferentFlow() {
			return isRunnerInDifferentFlowThan(UseCaseFlow.this);
		}
	}
}
