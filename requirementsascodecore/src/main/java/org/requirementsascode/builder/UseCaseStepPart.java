package org.requirementsascode.builder;

import java.util.function.Consumer;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.UseCaseStepAs;
import org.requirementsascode.UseCaseStepSystem;
import org.requirementsascode.UseCaseStepUser;

public class UseCaseStepPart {
	private UseCaseStep useCaseStep;
	private UseCasePart useCasePart;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCaseStepPart(UseCaseStep useCaseStep, UseCasePart useCasePart) {
		this.useCaseStep = useCaseStep;
		this.useCasePart = useCasePart;
		this.useCaseModelBuilder = useCasePart.useCaseModelBuilder();
	}
	
	public UseCaseStepAsPart as(Actor... actors) {
		UseCaseStepAs useCaseStepAs = useCaseStep.as(actors);
		return new UseCaseStepAsPart(useCaseStepAs, this);
	}

	public <T> UseCaseStepUserPart<T> user(Class<T> eventOrExceptionClass) {
		UseCaseStepUser<T> useCaseStepUser = useCaseStep.user(eventOrExceptionClass);
		return new UseCaseStepUserPart<T>(useCaseStepUser, this);
	}

	public <T> UseCaseStepUserPart<T> handle(Class<T> eventOrExceptionClass) {
		UseCaseStepUser<T> useCaseStepUser = useCaseStep.handle(eventOrExceptionClass);
		return new UseCaseStepUserPart<>(useCaseStepUser, this);
	}

	public UseCaseStepSystemPart<UseCaseRunner> system(Consumer<UseCaseRunner> systemReaction) {
		UseCaseStepSystem<UseCaseRunner> useCaseStepSystem = useCaseStep.system(systemReaction);
		return new UseCaseStepSystemPart<UseCaseRunner>(useCaseStepSystem, this);
	}

	public UseCasePart continueAfter(String stepName) {
		useCaseStep.continueAfter(stepName);
		return useCasePart;
	} 
	
	public UseCasePart continueAt(String stepName) {
		useCaseStep.continueAt(stepName);
		return useCasePart;
	}
	
	public UseCasePart continueWithoutAlternativeAt(String stepName) {
		useCaseStep.continueWithoutAlternativeAt(stepName);
		return useCasePart;
	}
	
	public UseCasePart useCasePart(){
		return useCasePart;
	}
	
	public UseCaseModelBuilder useCaseModelBuilder(){
		return useCaseModelBuilder;
	}
}
