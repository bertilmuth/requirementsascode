package org.requirementsascode;

import org.requirementsascode.exception.ElementAlreadyInModel;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see Flow#getCondition()
 * @author b_muth
 */
public class FlowConditionPart {
	private FlowPositionPart flowPositionPart;
	private Condition condition;

	FlowConditionPart(FlowPositionPart flowPositionPart, Condition condition) {
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