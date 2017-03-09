package org.requirementsascode;

import static org.requirementsascode.UseCaseStepPredicate.afterStep;
import static org.requirementsascode.UseCaseStepPredicate.isRunnerAtStart;
import static org.requirementsascode.UseCaseStepPredicate.isRunnerInDifferentFlowThan;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.NoSuchElementInModel;
import org.requirementsascode.exception.NoSuchElementInUseCase;

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
	 * Returns the steps contained in this flow.
	 * Do not modify the returned collection directly, use {@link #step(String)}
	 * 
	 * @return a collection of the steps
	 */
	public List<UseCaseStep> steps() {
		return useCase().steps().stream()
			.filter(step -> step.flow().equals(this))
			.collect(Collectors.toList());
	}
	
	/**
	 * Creates the first step of this flow, with the specified name.
	 * 
	 * @param stepName the name of the step to be created
	 * @return the newly created step, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
	 */
	public UseCaseStep step(String stepName) {
		Objects.requireNonNull(stepName);

		UseCaseStep newStep = newStep(stepName, Optional.empty(), flowPredicate.get());

		return newStep;
	}

	private UseCaseStep newStep(String stepName, Optional<UseCaseStep> previousStep, Optional<Predicate<UseCaseRunner>> predicate) {
		UseCaseStep stepToLeave = useCase().newStep(stepName, this, previousStep, predicate);
		return stepToLeave;
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
	 * Sets the flow's predicate to start the flow after the specified step, 
	 * in this flow's use case.
	 * 
	 * @param stepName the name of the step to start the flow after
	 * @return this use case flow, to ease creation of the predicate and the first step of the flow
	 * @throws NoSuchElementInUseCase if the specified step is not found in this flow's use case
	 */
	public UseCaseFlow after(String stepName) {
		return after(stepName, useCase);
	}
	
	/**
	 * Sets the flow's predicate to start the flow after the specified step, 
	 * which is contained in the specified use case.
	 * 
	 * @param stepName the name of the step to start the flow after
	 * @param useCaseName the name of the use case containing the step named stepName
	 * @return this use case flow, to ease creation of the predicate and the first step of the flow
	 * @throws NoSuchElementInModel if the specified use case is not found in the use case model
	 * @throws NoSuchElementInUseCase if the specified step is not found in the specified use case
	 */
	public UseCaseFlow after(String stepName, String useCaseName) {
		Objects.requireNonNull(stepName);
		Objects.requireNonNull(useCaseName);
		
		UseCase useCase = useCaseModel().findUseCase(useCaseName)
			.orElseThrow(() -> new NoSuchElementInModel(useCaseName));
		return after(stepName, useCase);
	}
	
	private UseCaseFlow after(String stepName, UseCase useCase) {
		Optional<UseCaseStep> foundStep = useCase.findStep(stepName);
		flowPredicate.setStepPredicate(afterStep(foundStep
			.orElseThrow(() -> new NoSuchElementInUseCase(useCase, stepName))));
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
		
		private void setStepPredicate(Predicate<UseCaseRunner> stepPredicate){
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
}
