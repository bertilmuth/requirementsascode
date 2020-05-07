package org.requirementsascode.builder;

import static org.requirementsascode.builder.FlowConditionPart.flowConditionPart;

import java.util.Objects;

import org.requirementsascode.Condition;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.flowposition.FlowPosition;

public class FlowPositionPart {
	private FlowPosition optionalFlowPosition;
	private FlowPart flowPart;
	private FlowConditionPart conditionPart;

	FlowPositionPart(FlowPosition optionalFlowPosition, FlowPart flowPart) {
		this.optionalFlowPosition = optionalFlowPosition;
		this.flowPart = Objects.requireNonNull(flowPart);
	}

	/**
	 * Constrains the flow's condition: only if the specified condition is true as
	 * well (beside the flow position), the flow is started.
	 *
	 * @param condition the condition that constrains when the flow is started
	 * @return this condition part, to ease creation of the first step of the flow
	 */
	public FlowConditionPart condition(Condition condition) {
		this.conditionPart = flowConditionPart(condition, this);
		return conditionPart;
	}

	/**
	 * Creates the first step of this flow. It can be run when the runner is at the
	 * right position.
	 *
	 * @param stepName the name of the step to be created
	 * @return the newly created step part, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already
	 *                               exists in the use case
	 */
	public StepPart step(String stepName) {
		return condition(null).step(stepName);
	}

	FlowPart getFlowPart() {
		return flowPart;
	}

	FlowPosition getOptionalFlowPosition() {
		return optionalFlowPosition;
	}
}