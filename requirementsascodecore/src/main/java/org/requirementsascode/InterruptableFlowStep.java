package org.requirementsascode;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.requirementsascode.flowposition.After;

/**
 * An interruptable flow step is either the first step of a flow without a user
 * specified condition, or a step that is not the first step (in any flow).
 * 
 * This kind of step can be "interrupted" by the first step of a different flow,
 * that has a user specified condition.
 * 
 * So an interruptable flow step can be considered the [else] case, if no other
 * flow starts.
 * 
 * @author b_muth
 */
public class InterruptableFlowStep extends FlowStep{
	/**
	 * Creates unconditional step with the specified name as the last step of the
	 * specified flow.
	 *
	 * @param stepName the name of the step to be created
	 * @param flow     the flow that will contain the new step
	 * @param optionalCondition the condition of the step, or null if it has none
	 */
	InterruptableFlowStep(String stepName, Flow flow, Condition optionalCondition) {
		super(stepName, flow, optionalCondition);
		appendToLastStepOfFlow();
	}

	private void appendToLastStepOfFlow() {
		List<FlowStep> flowSteps = getFlow().getSteps();
		FlowStep lastFlowStep = flowSteps.isEmpty() ? null : flowSteps.get(flowSteps.size() - 1);
		setPreviousStepInFlow(lastFlowStep);
    setFlowPosition(After.afterFlowStep(lastFlowStep));
	}

  @Override
	public Predicate<ModelRunner> getPredicate() {
		Condition reactWhile = getReactWhile();

    Predicate<ModelRunner> predicate = getFlowPosition().and(noStepInterrupts()).and(isConditionTrue());
    if (reactWhile != null) {
      predicate = predicate.and(toPredicate(reactWhile));
    }

    return predicate;
  }

	private Predicate<ModelRunner> noStepInterrupts() {
		return modelRunner -> {
			Class<?> messageClass = getMessageClass();

			boolean noStepInterrupts = true;
			if (modelRunner.isRunning()) {
				Collection<Step> steps = getFlow().getModel().getModifiableSteps();
				
				for (Step step : steps) {
					if(isInterruptingStep(step) && modelRunner.canReactToMessageClass(step, messageClass)) {
						noStepInterrupts = false;
						break;
					}
				}
			}

			return noStepInterrupts;
		};

	}

	private boolean isInterruptingStep(Step step) {
		return InterruptingFlowStep.class.equals(step.getClass());
	}
}
