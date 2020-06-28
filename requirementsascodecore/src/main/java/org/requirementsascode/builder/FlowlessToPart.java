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
}
