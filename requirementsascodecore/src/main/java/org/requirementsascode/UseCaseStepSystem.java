package org.requirementsascode;

import static org.requirementsascode.UseCaseStepPredicate.afterStep;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyInModel;

/**
 * The part of the step that contains a reference to the system reaction
 * that can be triggered (given an appropriate actor and event, and the 
 * step's predicate being true).
 * 
 * @author b_muth
 *
 */
public class UseCaseStepSystem<T>{
	private UseCaseStep useCaseStep;
	private Consumer<T> systemReaction;

	UseCaseStepSystem(UseCaseStep useCaseStep, Consumer<T> systemReaction) {
		this.useCaseStep = useCaseStep;
		this.systemReaction = systemReaction;
	}
	
	/**
	 * Returns the system reaction,
	 * meaning how the system will react when the step's predicate
	 * is true and an appropriate event object is received
	 * via {@link UseCaseRunner#reactTo(Object)}.
	 * 
	 * @return the system reaction
	 */
	public Consumer<T> systemReaction() {
		return systemReaction;
	}
	
	/**
	 * Creates a new use case in this model.
	 * 
	 * @param useCaseName the name of the use case to be created.
	 * @return the newly created use case
	 * @throws ElementAlreadyInModel if a use case with the specified name already exists in the model
	 */
	public UseCase useCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);

		UseCase newUseCase = useCaseStep.useCaseModel().useCase(useCaseName);
		return newUseCase;
	}
	
	/**
	 * Creates a new flow in the use case that contains this step.
	 * 
	 * @param flowName the name of the flow to be created.
	 * @return the newly created flow
	 * @throws ElementAlreadyInModel if a flow with the specified name already exists in the use case
	 */
	public UseCaseFlow flow(String flowName) {
		Objects.requireNonNull(flowName);

		UseCaseFlow newFlow = useCaseStep.useCase().flow(flowName);
		return newFlow;
	}

	/**
	 * Creates a new step in this flow, with the specified name, that follows this step in sequence.
	 * 
	 * @param stepName the name of the step to be created
	 * @return the newly created step, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
	 */
	public UseCaseStep step(String stepName) {			
		Objects.requireNonNull(stepName);

		UseCaseStep newStep = 
			useCaseStep.useCase().newStep(stepName, useCaseStep.flow(), 
				Optional.of(useCaseStep), Optional.empty());
		
		return newStep; 
	}
	
	/**
	 * React to this step's event as long as the condition is fulfilled.
	 * 
	 * Even when the condition is fulfilled, the flow can advance given
	 * that the event of the next step is received.
	 * 
	 * Note that if the condition is not fulfilled after the previous step has been performed,
	 * the step will not react at all.
	 * 
	 * @param condition the condition to check
	 * @return the system part
	 */
	public UseCaseStepSystem<T> reactWhile(Predicate<UseCaseRunner> condition) {
		Objects.requireNonNull(condition);
					
		Predicate<UseCaseRunner> performIfConditionIsTruePredicate = useCaseStep.predicate().and(condition);
		Predicate<UseCaseRunner> repeatIfConditionIsTruePredicate = afterStep(useCaseStep).and(condition);
		useCaseStep.setPredicate(performIfConditionIsTruePredicate.or(repeatIfConditionIsTruePredicate));
		
		return this;
	}
}