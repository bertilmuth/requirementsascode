package org.requirementsascode.builder;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseStepSystem;
import org.requirementsascode.UseCaseStepUser;

public class UseCaseStepUserPart<T>{
	private UseCaseStepUser<T> useCaseStepUser;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCaseStepUserPart(UseCaseStepUser<T> useCaseStepUser, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCaseStepUser = useCaseStepUser;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public UseCaseStepSystemPart<T> system(Consumer<T> systemReaction) {
		UseCaseStepSystem<T> useCaseStepSystem = useCaseStepUser.system(systemReaction);
		return new UseCaseStepSystemPart<>(useCaseStepSystem, useCaseModelBuilder);
	}

}
