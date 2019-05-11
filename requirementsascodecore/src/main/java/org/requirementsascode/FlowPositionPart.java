package org.requirementsascode;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.flowposition.FlowPosition;

public class FlowPositionPart {
	private FlowPosition flowPosition;
	private FlowPart flowPart;
	private ConditionPart conditionPart;

	FlowPositionPart(FlowPosition flowPosition, FlowPart flowPart) {
		this.flowPosition = flowPosition;
		this.flowPart = flowPart;
		this.conditionPart = condition(null);
	}

	/**
	 * Constrains the flow's condition: only if the specified condition is true as
	 * well (beside the flow position), the flow is started.
	 *
	 * @param condition the condition that constrains when the flow is started
	 * @return this condition part, to ease creation of the first step of the flow
	 */
	public ConditionPart condition(Condition condition) {
		this.conditionPart = new ConditionPart(this, condition);
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
		UseCasePart useCasePart = flowPart.getUseCasePart();
		UseCase useCase = useCasePart.getUseCase();
		Flow flow = flowPart.getFlow();
		Condition condition = conditionPart.getCondition();
		FlowStep step = useCase.newInterruptingFlowStep(stepName, flow, flowPosition, condition);
		StepPart stepPart = new StepPart(step, useCasePart, flowPart);
		return stepPart;
	}
}