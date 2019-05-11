package org.requirementsascode;

import org.requirementsascode.exception.ElementAlreadyInModel;

public class ConditionPart {
	private FlowPositionPart flowPositionPart;
	private Condition condition;

	ConditionPart(FlowPositionPart flowPositionPart, Condition condition) {
		this.flowPositionPart = flowPositionPart;
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}

	/**
	 * Creates the first step of this flow. It can be run when the runner is at the
	 * right position and the flow's condition is fulfilled.
	 *
	 * @param stepName the name of the step to be created
	 * @return the newly created step part, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already
	 *                               exists in the use case
	 */
	public StepPart step(String stepName) {
		return flowPositionPart.step(stepName);
	}
}