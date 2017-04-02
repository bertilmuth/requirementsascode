package org.requirementsascode.builder;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.Step;
import org.requirementsascode.exception.NoSuchElementInModel;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 * 
 * @see Step
 * @author b_muth
 *
 */
public class StepPart {
	private Step useCaseStep;
	private FlowPart useCaseFlowPart;
	private UseCaseModelBuilder useCaseModelBuilder;
	private Actor systemActor;

	public StepPart(Step useCaseStep, FlowPart useCaseFlowPart) {
		this.useCaseStep = useCaseStep;
		this.useCaseFlowPart = useCaseFlowPart;
		this.useCaseModelBuilder = useCaseFlowPart.useCaseModelBuilder();
		this.systemActor = useCaseModelBuilder.build().systemActor();
	}
	
	/**
	 * Defines which actors (i.e. user groups) can cause the system to react to
	 * the event of this step.
	 * 
	 * @param actors the actors that define the user groups
	 * @return the created as part of this step
	 */
	public UseCaseStepAsPart as(Actor... actors) {
		Objects.requireNonNull(actors);
		return new UseCaseStepAsPart(this, actors);
	}

	/**
	 * Defines the type of user event objects that this step accepts. 
	 * Events of this type can cause a system reaction.
	 * 
	 * Given that the step's predicate is true, and the actor is right, the system reacts 
	 * to objects that are instances of the specified class or instances of any direct or
	 * indirect subclass of the specified class.
	 * 
	 * @param eventClass the class of events the system reacts to in this step
	 * @param <T> the type of the class
	 * @return the created user part of this step
	 */
	public <T> UseCaseStepUserPart<T> user(Class<T> eventClass) {
		Objects.requireNonNull(eventClass);
		Actor userActor = useCaseModelBuilder.build().userActor();
		UseCaseStepUserPart<T> userPart = as(userActor).user(eventClass);
		return userPart;
	}

	/**
	 * Defines the type of system event objects or exceptions that this step handles.
	 * Events of this type can cause a system reaction.
	 * 
	 * Given that the step's predicate is true, and the actor is right, the system reacts 
	 * to objects that are instances of the specified class or instances of any direct or
	 * indirect subclass of the specified class.
	 * 
	 * @param eventOrExceptionClass the class of events the system reacts to in this step
	 * @param <T> the type of the class
	 * @return the created user part of this step
	 */
	public <T> UseCaseStepUserPart<T> handle(Class<T> eventOrExceptionClass) {
		Objects.requireNonNull(eventOrExceptionClass);
		Actor systemActor = useCaseModelBuilder.build().systemActor();
		UseCaseStepUserPart<T> userPart = as(systemActor).user(eventOrExceptionClass);
		return userPart;
	}

	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing an event provided via {@link UseCaseModelRunner#reactTo(Object)}. 
	 * Instead, the use case model runner provides
	 * itself as an event to the system reaction.
	 * 
	 * @param systemReaction the autonomous system reaction
	 * @return the created system part of this step
	 */
	public UseCaseStepSystemPart<UseCaseModelRunner> system(Consumer<UseCaseModelRunner> systemReaction) {
		Objects.requireNonNull(systemReaction);
		UseCaseStepSystemPart<UseCaseModelRunner> systemPart = as(systemActor).system(systemReaction);		
		return systemPart;
	}

	/**
	 * Makes the use case model runner continue after the specified step.
	 * 
	 * @param stepName name of the step to continue after, in this use case.
	 * @return the use case part this step belongs to, to ease creation of further flows
	 * @throws NoSuchElementInModel if no step with the specified stepName is found in the current use case
	 */
	public UseCasePart continueAfter(String stepName) {
		Objects.requireNonNull(stepName);
		UseCasePart useCasePart = as(systemActor).continueAfter(stepName);		
		return useCasePart;
	} 
	
	/**
	 * Makes the use case model runner continue at the specified step. If there are
	 * alternative flows starting at the specified step, one may be entered if
	 * its condition is enabled.
	 * 
	 * @param stepName name of the step to continue at, in this use case.
	 * @return the use case part this step belongs to, to ease creation of further flows
	 * @throws NoSuchElementInModel if no step with the specified stepName is found in the current use case
	 */
	public UseCasePart continueAt(String stepName) {
		Objects.requireNonNull(stepName);
		UseCasePart useCasePart = as(systemActor).continueAt(stepName);		
		return useCasePart;
	}
	
	/**
	 * Makes the use case model runner continue at the specified step. No alternative
	 * flow starting at the specified step is entered, even if its condition is
	 * enabled.
	 * 
	 * @param stepName name of the step to continue at, in this use case.
	 * @return the use case part this step belongs to, to ease creation of further flows
	 * @throws NoSuchElementInModel if no step with the specified stepName is found in the current use case
	 */
	public UseCasePart continueWithoutAlternativeAt(String stepName) {
		Objects.requireNonNull(stepName);
		UseCasePart useCasePart = as(systemActor).continueWithoutAlternativeAt(stepName);		
		return useCasePart;
	}
	
	public Step useCaseStep(){
		return useCaseStep;
	}
	
	public FlowPart useCaseFlowPart(){
		return useCaseFlowPart;
	}
	
	public UseCasePart useCasePart(){
		return useCaseFlowPart().useCasePart();
	}
	
	public UseCaseModelBuilder useCaseModelBuilder(){
		return useCaseModelBuilder;
	}
}
