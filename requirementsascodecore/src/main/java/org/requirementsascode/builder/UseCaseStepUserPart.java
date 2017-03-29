package org.requirementsascode.builder;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.UseCaseRunner;
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

	/**
	 * Defines the system reaction. 
	 * The system will react as specified to the current step's events,
	 * when you call {@link UseCaseRunner#reactTo(Object)}.
	 * 
	 * @param systemReaction the specified system reaction
	 * @return the created system part of this step 
	 */
	public UseCaseStepSystemPart<T> system(Consumer<T> systemReaction) {
		Objects.requireNonNull(systemReaction);
		UseCaseStepSystem<T> useCaseStepSystem = 
			new UseCaseStepSystem<>(useCaseStep, systemReaction);
		return new UseCaseStepSystemPart<>(useCaseStepSystem, useCaseStepPart);
	}

}
