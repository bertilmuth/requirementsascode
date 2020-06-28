package org.requirementsascode.builder;

import static org.requirementsascode.builder.FlowlessConditionPart.flowlessConditionPart;

import java.util.Objects;

import org.requirementsascode.Actor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

public class FlowlessToPart {
	private UseCasePart useCasePart;
	private StepSystemPart<?> stepSystemPart;
	private long flowlessStepCounter;

	FlowlessToPart(UseCasePart useCasePart, StepSystemPart<?> stepSystemPart, Actor recipient, long flowlessStepCounter) {
		this.useCasePart = Objects.requireNonNull(useCasePart);
		this.stepSystemPart = Objects.requireNonNull(stepSystemPart);
		this.flowlessStepCounter = flowlessStepCounter;
		stepSystemPart.to(recipient);
	}

	public Model build() {
		return stepSystemPart.build();
	}
	
	/**
	 * Constrains the condition for triggering a system reaction: only if the
	 * specified condition is true, a system reaction can be triggered.
	 *
	 * @param condition the condition that constrains when the system reaction is
	 *                  triggered
	 * @return the created condition part
	 */
	public FlowlessConditionPart condition(Condition condition) {
		FlowlessConditionPart conditionPart = flowlessConditionPart(condition, useCasePart, ++flowlessStepCounter);
		return conditionPart;
	}

	/**
	 * Creates a named step.
	 * 
	 * @param stepName the name of the created step
	 * @return the created step part
	 */
	public FlowlessStepPart step(String stepName) {
		Objects.requireNonNull(stepName);
		FlowlessStepPart stepPart = condition(null).step(stepName);
		return stepPart;
	}

	/**
	 * Defines the type of messages or exceptions that will cause a system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param messageClass the class of messages the system reacts to
	 * @param <U>          the type of the class
	 * @return the created user part
	 */
	public <U> FlowlessUserPart<U> on(Class<U> messageClass) {
		Objects.requireNonNull(messageClass);
		FlowlessUserPart<U> flowlessUserPart = condition(null).on(messageClass);
		return flowlessUserPart;
	}

	/**
	 * Defines the type of commands that will cause a system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param commandClass the class of commands the system reacts to
	 * @param <U>          the type of the class
	 * @return the created user part
	 */
	public <U> FlowlessUserPart<U> user(Class<U> commandClass) {
		Objects.requireNonNull(commandClass);
		FlowlessUserPart<U> flowlessUserPart = condition(null).user(commandClass);
		return flowlessUserPart;
	}

	/**
	 * Creates a new use case in the current model, and returns a part for building
	 * its details. If a use case with the specified name already exists, returns a
	 * part for the existing use case.
	 *
	 * @param useCaseName the name of the existing use case / use case to be
	 *                    created.
	 * @return the created / found use case's part.
	 */
	public UseCasePart useCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		UseCasePart newUseCasePart = useCasePart.getModelBuilder().useCase(useCaseName);
		return newUseCasePart;
	}
}
