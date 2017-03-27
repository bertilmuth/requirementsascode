package org.requirementsascode.builder;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStepAs;
import org.requirementsascode.UseCaseStepUser;

public class UseCaseStepAsPart{
	private UseCaseStepAs useCaseStepAs;
	private UseCaseStepPart useCaseStepPart;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCaseStepAsPart(UseCaseStepAs useCaseStepAs, UseCaseStepPart useCaseStepPart) {
		this.useCaseStepAs = useCaseStepAs;
		this.useCaseStepPart = useCaseStepPart;
		this.useCaseModelBuilder = useCaseStepPart.useCaseModelBuilder();
	}

	public UseCaseModelBuilder system(Consumer<UseCaseRunner> systemReaction) {
		useCaseStepAs.system(systemReaction);
		return useCaseModelBuilder;
	} 

	public <T> UseCaseStepUserPart<T> user(Class<T> eventClass) {
		UseCaseStepUser<T> useCaseStepUser = useCaseStepAs.user(eventClass);
		return new UseCaseStepUserPart<>(useCaseStepUser, useCaseStepPart);
	}
}
