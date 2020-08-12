package org.requirementsascode.builder;

import static org.requirementsascode.builder.StepPart.interruptableFlowStepPart;

import java.util.Objects;

import org.requirementsascode.Condition;
import org.requirementsascode.exception.ElementAlreadyInModel;


public class StepConditionPart {
	private FlowPart flowPart;
	private Condition condition;

  private StepConditionPart(Condition condition, FlowPart flowPart) {
    this.flowPart = Objects.requireNonNull(flowPart);
    this.condition = condition;
	}
	
  static StepConditionPart stepConditionPart(Condition condition, FlowPart flowPart) {
    return new StepConditionPart(condition, flowPart);
  }
	
  /**
   * Creates a conditional new step in this flow, with the specified name, that follows the
   * last step in sequence.
   *
   * @param stepName the name of the step to be created
   * @return the newly created step
   * @throws ElementAlreadyInModel if a step with the specified name already
   *                               exists in the use case
   */
  public StepPart step(String stepName) {
    Objects.requireNonNull(stepName);
    StepPart trailingStepInFlowPart = interruptableFlowStepPart(stepName, flowPart, condition);
    return trailingStepInFlowPart;
  }
}