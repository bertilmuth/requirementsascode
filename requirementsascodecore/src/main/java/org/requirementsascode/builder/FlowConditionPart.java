package org.requirementsascode.builder;

import java.util.Objects;

import org.requirementsascode.Condition;
import org.requirementsascode.Flow;
import org.requirementsascode.Model;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.flowposition.FlowPosition;

import static org.requirementsascode.builder.StepPart.*;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see Flow#getCondition()
 * @author b_muth
 */
public class FlowConditionPart {
	private FlowPart flowPart;
	private Condition optionalCondition;
  private FlowPosition optionalFlowPosition;

  private FlowConditionPart(Condition optionalCondition, FlowPart flowPart, FlowPosition optionalFlowPosition) {
    this.flowPart = Objects.requireNonNull(flowPart);
    this.optionalCondition = optionalCondition;
    this.optionalFlowPosition = optionalFlowPosition;
	}
	
  static FlowConditionPart flowConditionPart(Condition optionalCondition, FlowPart flowPart, FlowPosition optionalFlowPosition) {
    return new FlowConditionPart(optionalCondition, flowPart, optionalFlowPosition);
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
		StepPart stepPart = interruptingFlowStepPart(stepName, flowPart, optionalFlowPosition, getOptionalCondition());
		return stepPart;
	}
	
	Condition getOptionalCondition() {
		return optionalCondition;
	}
}