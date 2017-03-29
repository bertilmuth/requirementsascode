package org.requirementsascode.builder;

import java.util.function.Consumer;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.UseCaseStepAs;

public class UseCaseStepPart {
	private UseCaseStep useCaseStep;
	private UseCaseFlowPart useCaseFlowPart;
	private UseCaseModelBuilder useCaseModelBuilder;
	private Actor systemActor;

	public UseCaseStepPart(UseCaseStep useCaseStep, UseCaseFlowPart useCaseFlowPart) {
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
		UseCaseStepAs as = new UseCaseStepAs(useCaseStep, actors);
		return new UseCaseStepAsPart(as, this);
	}

	public <T> UseCaseStepUserPart<T> user(Class<T> eventOrExceptionClass) {
		Actor userActor = useCaseModelBuilder.build().userActor();
		UseCaseStepUserPart<T> userPart = as(userActor).user(eventOrExceptionClass);
		return userPart;
	}

	public <T> UseCaseStepUserPart<T> handle(Class<T> eventOrExceptionClass) {
		Actor systemActor = useCaseModelBuilder.build().systemActor();
		UseCaseStepUserPart<T> userPart = as(systemActor).user(eventOrExceptionClass);
		return userPart;
	}

	public UseCaseStepSystemPart<UseCaseRunner> system(Consumer<UseCaseRunner> systemReaction) {
		UseCaseStepSystemPart<UseCaseRunner> systemPart = as(systemActor).system(systemReaction);		
		return systemPart;
	}

	public UseCasePart continueAfter(String stepName) {
		UseCasePart useCasePart = as(systemActor).continueAfter(stepName);		
		return useCasePart;
	} 
	
	public UseCasePart continueAt(String stepName) {
		UseCasePart useCasePart = as(systemActor).continueAt(stepName);		
		return useCasePart;
	}
	
	public UseCasePart continueWithoutAlternativeAt(String stepName) {
		UseCasePart useCasePart = as(systemActor).continueWithoutAlternativeAt(stepName);		
		return useCasePart;
	}
	
	public UseCaseStep useCaseStep(){
		return useCaseStep;
	}
	
	public UseCaseFlowPart useCaseFlowPart(){
		return useCaseFlowPart;
	}
	
	public UseCasePart useCasePart(){
		return useCaseFlowPart().useCasePart();
	}
	
	public UseCaseModelBuilder useCaseModelBuilder(){
		return useCaseModelBuilder;
	}
}
