package org.requirementsascode;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RunnAndStopTest extends AbstractTestCase {

    @Before
    public void setUp() throws Exception {
	setupWith(new TestModelRunner());

    }

    @Test
    public void useCaseModelRunnerIsNotRunningAtFirst() {
	assertFalse(useCaseModelRunner.isRunning());
    }

    @Test
    public void useCaseModelRunnerIsRunningAfterRunCall() {
	Model model = useCaseModelBuilder.build();
	useCaseModelRunner.run(model);
	assertTrue(useCaseModelRunner.isRunning());
    }

    @Test
    public void useCaseModelRunnerIsNotRunningWhenBeingStoppedBeforeRunCall() {
	useCaseModelRunner.stop();
	assertFalse(useCaseModelRunner.isRunning());
    }

    @Test
    public void useCaseModelRunnerIsNotRunningWhenBeingStoppedAfterRunCall() {
	Model model = useCaseModelBuilder.build();
	useCaseModelRunner.run(model);
	useCaseModelRunner.stop();
	assertFalse(useCaseModelRunner.isRunning());
    }
}
