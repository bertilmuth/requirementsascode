package org.requirementsascode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
public class InterruptableFlowStep extends FlowStep implements Serializable {
	private static final long serialVersionUID = -2926490717985964131L;

	/**
	 * Creates unconditional step with the specified name as the last step of the
	 * specified flow.
	 *
	 * @param stepName the name of the step to be created
	 * @param flow     the flow that will contain the new step
	 */
	InterruptableFlowStep(String stepName, Flow flow) {
		super(stepName, flow, null);
		appendToLastStepOfFlow();
	}

	private void appendToLastStepOfFlow() {
		List<FlowStep> flowSteps = getFlow().getSteps();
		FlowStep lastFlowStep = flowSteps.isEmpty() ? null : flowSteps.get(flowSteps.size() - 1);
		setPreviousStepInFlow(lastFlowStep);
		setFlowPosition(new After(lastFlowStep));
	}

	@Override
	public Predicate<ModelRunner> getPredicate() {
		Condition reactWhile = getReactWhile();

		Predicate<ModelRunner> predicate = getFlowPosition().and(noStepInterrupts());
		if (reactWhile != null) {
			predicate = predicate.and(toPredicate(reactWhile));
		}

		return predicate;
	}

	private Predicate<ModelRunner> noStepInterrupts() {
		return modelRunner -> {
			Class<?> theStepsMessageClass = getMessageClass();

			boolean isInterrupted = false;
			if (modelRunner.isRunning()) {
				Stream<Step> interruptingStepsStream = modelRunner.getRunningStepStream().filter(isInterruptingStep());
				Set<Step> interruptingStepsThatCanReact = modelRunner.getStepsInStreamThatCanReactTo(theStepsMessageClass,
					interruptingStepsStream);
				isInterrupted = interruptingStepsThatCanReact.isEmpty();
			}

			return isInterrupted;
		};

	}

	private Predicate<Step> isInterruptingStep() {
		return step -> InterruptingFlowStep.class.equals(step.getClass());
	}
}
