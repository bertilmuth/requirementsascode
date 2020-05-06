package org.requirementsascode.builder;

import org.requirementsascode.Condition;
import org.requirementsascode.Flow;
import org.requirementsascode.FlowStep;
import org.requirementsascode.Model;
import org.requirementsascode.UseCase;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.flowposition.FlowPosition;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see Flow#getCondition()
 * @author b_muth
 */
public class FlowConditionPart {
	private FlowPositionPart flowPositionPart;
	private Condition optionalCondition;

	FlowConditionPart(FlowPositionPart flowPositionPart, Condition condition) {
		this.flowPositionPart = flowPositionPart;
		this.optionalCondition = condition;
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
		FlowPart flowPart = flowPositionPart.getFlowPart();
		UseCasePart useCasePart = flowPart.getUseCasePart();
		UseCase useCase = useCasePart.getUseCase();
		Flow flow = flowPart.getFlow();
		FlowPosition flowPosition = flowPositionPart.getOptionalFlowPosition();
		FlowStep step = useCase.newInterruptingFlowStep(stepName, flow, flowPosition, getOptionalCondition());
		StepPart stepPart = new StepPart(step, flowPart);
		return stepPart;
	}
	
	Condition getOptionalCondition() {
		return optionalCondition;
	}
}