package org.requirementsascode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple model runner for automated tests.
 *
 * @author b_muth
 */
public class TestModelRunner extends ModelRunner {
    private static final long serialVersionUID = 1161211712610795119L;

    private List<String> runStepNames;

    public TestModelRunner() {
	runStepNames = new ArrayList<>();
	handleWith(this::withStepNameTracking);
    }

    /**
     * Returns the names of the steps that have been run, separated with a semicolon
     * after each step, without spaces.
     *
     * <p>
     * If no step has been run, an empty string is returned. If one step S1 has been
     * run, the string S1; is returned. If two steps, S1 and S2 have been run, the
     * string S1;S2; is returned.
     *
     * @return the step names
     */
    public String getRunStepNames() {
	String runStepNamesSemicolonSeparated = String.join(";", runStepNames);
	if(runStepNames.size() > 0) {
	    runStepNamesSemicolonSeparated += ";";
	}
	return runStepNamesSemicolonSeparated;
    }
    
    /**
     * Returns true if the runner has run exactly the specified steps,
     * in the correct order.
     * 
     * @param expectedSteps the steps to check if they have been run
     * @return true if all the steps have been run, false otherwise.
     */
    public boolean hasRun(String... expectedSteps) {
	String[] actualSteps = runStepNames.toArray(new String[runStepNames.size()]);
	boolean actualEqualsExpected = Arrays.equals(actualSteps, expectedSteps);
	return actualEqualsExpected;
    }

    private void withStepNameTracking(StandardEventHandler eventHandler) {
	String stepName = eventHandler.getStep().getName();
	runStepNames.add(stepName);
	eventHandler.handleEvent();
    }
}
