package org.requirementsascode.flowposition;

import java.io.Serializable;

import java.util.Objects;

import org.requirementsascode.Step;
import org.requirementsascode.ModelRunner;

/**
 * Tests whether the specified step was the last step run.
 * 
 * @author b_muth
 *
 */
public class After extends FlowPosition implements Serializable {
    private static final long serialVersionUID = -4951912635216926005L;

    /**
     * Tests whether the specified step was the last step run.
     * 
     * @param step, or null to mean: when no step has been run.
     */
    public After(Step step) {
	super(step);
    }

    @Override
    public boolean isRunnerAtRightPositionFor(Step step, ModelRunner modelRunner) {
	Step latestStepRun = modelRunner.getLatestStep().orElse(null);
	boolean stepWasRunLast = Objects.equals(step, latestStepRun);
	return stepWasRunLast;
    }
}
