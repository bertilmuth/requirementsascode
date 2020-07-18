package org.requirementsascode.flowposition;

import java.util.Objects;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.Step;

/**
 * Tests whether the specified step was the last step run.
 * 
 * @author b_muth
 *
 */
public class After extends FlowPosition{
	/**
	 * Tests whether the specified step was the last step run.
	 * 
	 * @param step, or null to mean: when no step has been run.
	 */
	public After(FlowStep step) {
		super(step);
	}

	@Override
	protected boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner) {
		Step latestStepRun = modelRunner.getLatestStep().orElse(null);
		boolean stepWasRunLast = Objects.equals(step, latestStepRun);
		return stepWasRunLast;
	}
}
