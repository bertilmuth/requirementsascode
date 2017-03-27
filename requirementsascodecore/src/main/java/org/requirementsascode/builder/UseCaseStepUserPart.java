package org.requirementsascode.builder;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseStepSystem;
import org.requirementsascode.UseCaseStepUser;

public class UseCaseStepUserPart<T>{
	private UseCaseStepUser<T> useCaseStepUser;
	private UseCaseStepPart useCaseStepPart;

	public UseCaseStepUserPart(UseCaseStepUser<T> useCaseStepUser, UseCaseStepPart useCaseStepPart) {
		this.useCaseStepUser = useCaseStepUser;
		this.useCaseStepPart = useCaseStepPart;
	}

	public UseCaseStepSystemPart<T> system(Consumer<T> systemReaction) {
		UseCaseStepSystem<T> useCaseStepSystem = useCaseStepUser.system(systemReaction);
		return new UseCaseStepSystemPart<>(useCaseStepSystem, useCaseStepPart);
	}

}
