package org.requirementsascode;

import org.requirementsascode.condition.Condition;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.flowposition.FlowPosition;

public class FlowPositionPart {
    private FlowPosition flowPosition;
    private FlowPart flowPart;
    private WhenPart whenPart;

    FlowPositionPart(FlowPosition flowPosition, FlowPart flowPart) {
	this.flowPosition = flowPosition;
	this.flowPart = flowPart;
	this.whenPart = new WhenPart();
    }

    /**
     * Constrains the flow's condition: only if the specified condition is true as
     * well (beside the flow position), the flow is started.
     *
     * @param whenCondition
     *            the condition that constrains when the flow is started
     * @return this when part, to ease creation of the first step of the flow
     */
    public WhenPart when(Condition whenCondition) {
	whenPart.setCondition(whenCondition);
	return whenPart;
    }
    
    public class WhenPart {
	private Condition condition;

	private WhenPart() {
	}
	
	private Condition getCondition() {
	    return condition;
	}

	private void setCondition(Condition whenCondition) {
	    this.condition = whenCondition;	    
	}

	/**
	 * Creates the first step of this flow. It can be run when the runner is at the
	 * right position and the flow's condition is fulfilled.
	 *
	 * @param stepName
	 *            the name of the step to be created
	 * @return the newly created step part, to ease creation of further steps
	 * @throws ElementAlreadyInModel
	 *             if a step with the specified name already exists in the use case
	 */
	public StepPart step(String stepName) {
	    return FlowPositionPart.this.step(stepName);
	}
    }

    /**
     * Creates the first step of this flow. It can be run when the
     * runner is at the right position.
     *
     * @param stepName
     *            the name of the step to be created
     * @return the newly created step part, to ease creation of further steps
     * @throws ElementAlreadyInModel
     *             if a step with the specified name already exists in the use case
     */
    public StepPart step(String stepName) {
	UseCasePart useCasePart = flowPart.getUseCasePart();
	UseCase useCase = useCasePart.getUseCase();
	Flow flow = flowPart.getFlow();
	Condition whenCondition = whenPart.getCondition();
        FlowStep step = useCase.newInterruptingFlowStep(stepName, flow, flowPosition, whenCondition);
        StepPart stepPart = new StepPart(step, useCasePart, flowPart);
        return stepPart;
    }
}