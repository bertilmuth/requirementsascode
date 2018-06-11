package org.requirementsascode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Simple model runner for automated tests.
 *
 * @author b_muth
 */
public class TestModelRunner extends ModelRunner {
    private static final long serialVersionUID = 1161211712610795119L;

    private List<Step> recordedSteps;
    private List<Object> recordedEvents;
    private List<String> runStepNames;

    private Consumer<StandardEventHandler> adaptedEventHandler;

    public TestModelRunner() {
	recordedSteps = new ArrayList<>();
	recordedEvents = new ArrayList<>();
	runStepNames = new ArrayList<>();
	super.handleWith(this::stepNameTracking);
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
	if (runStepNames.size() > 0) {
	    runStepNamesSemicolonSeparated += ";";
	}
	return runStepNamesSemicolonSeparated;
    }

    /**
     * Returns the recorded names of the steps that have been run so far.
     * <p>
     * If no step has been run, an empty array is returned. For example, this method
     * can used with the assertArrayEquals method of JUnit to compare the actual
     * names of steps that have been run (returned by this method) to the expected
     * step names.
     *
     * @return the ordered names of steps run by this runner
     */
    public String[] getRecordedStepNames() {
	String[] stepNames = recordedSteps.stream().map(step -> step.getName()).toArray(String[]::new);
	return stepNames;
    }

    /**
     * Returns the recorded events that the runner reacted to so far.
     * <p>
     * If no events have caused a system reaction, an empty array is returned. For
     * example, this method can used with the assertArrayEquals method of JUnit to
     * compare the actual events that caused a reaction (returned by this method) to
     * the expected events.
     *
     * @return the ordered events that caused a system reaction
     */
    public Object[] getRecordedEvents() {
	Object[] events = recordedEvents.toArray();
	System.out.println(recordedEvents.get(0).equals(events[0]));
	return events;
    }

    /**
     * Returns true if the runner has run exactly the specified steps, in the
     * correct order.
     * 
     * @param expectedSteps
     *            the steps to check if they have been run
     * @return true if all the steps have been run, false otherwise.
     */
    public boolean hasRun(String... expectedSteps) {
	String[] actualSteps = runStepNames.toArray(new String[runStepNames.size()]);
	boolean actualEqualsExpected = Arrays.equals(actualSteps, expectedSteps);
	return actualEqualsExpected;
    }

    @Override
    public void handleWith(Consumer<StandardEventHandler> adaptedEventHandler) {
	this.adaptedEventHandler = adaptedEventHandler;
    }

    private void stepNameTracking(StandardEventHandler standardEventHandler) {
	String stepName = standardEventHandler.getStep().getName();
	runStepNames.add(stepName);
	recordedSteps.add(standardEventHandler.getStep());
	recordedEvents.add(standardEventHandler.getEvent());

	if (adaptedEventHandler == null) {
	    standardEventHandler.handleEvent();
	} else {
	    adaptedEventHandler.accept(standardEventHandler);
	}
    }
}
