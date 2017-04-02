package org.requirementsascode.builder;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.systemreaction.IgnoreIt;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 * 
 * @see UseCaseStep#setEventClass(Class)
 * @author b_muth
 *
 */
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
	
	/**
	 * Creates a new step in this flow, with the specified name, that follows the current step in sequence.
	 * 
	 * @param stepName the name of the step to be created
	 * @return the newly created step
	 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
	 */
	public UseCaseStepPart step(String stepName) {
		return system(new IgnoreIt<T>()).step(stepName);
	}
}
