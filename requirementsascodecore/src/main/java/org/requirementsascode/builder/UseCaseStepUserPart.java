package org.requirementsascode.builder;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseStep;
import org.requirementsascode.UseCaseStepSystem;
import org.requirementsascode.UseCaseStepUser;

public class UseCaseStepUserPart<T>{
	private UseCaseStepPart useCaseStepPart;
	private UseCaseStep useCaseStep;

	public UseCaseStepUserPart(UseCaseStepUser<T> useCaseStepUser, UseCaseStepPart useCaseStepPart) {
		this.useCaseStepPart = useCaseStepPart;
		this.useCaseStep = useCaseStepPart.useCaseStep();
	}

	public UseCaseStepSystemPart<T> system(Consumer<T> systemReaction) {
		UseCaseStepSystem<T> useCaseStepSystem = 
			new UseCaseStepSystem<>(useCaseStep, systemReaction);
		return new UseCaseStepSystemPart<>(useCaseStepSystem, useCaseStepPart);
	}

}
