package org.requirementsascode.builder;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;

public class UseCaseStepUserPart<T>{
	private UseCaseStepPart useCaseStepPart;
	private UseCaseStep useCaseStep;

	public UseCaseStepUserPart(UseCaseStepPart useCaseStepPart, Class<T> eventClass) {
		this.useCaseStepPart = useCaseStepPart;
		this.useCaseStep = useCaseStepPart.useCaseStep();

		useCaseStep.setEventClass(eventClass);
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
		return new UseCaseStepSystemPart<>(useCaseStepPart, systemReaction);
	}

}
