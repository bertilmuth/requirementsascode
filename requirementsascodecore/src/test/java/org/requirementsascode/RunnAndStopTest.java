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
	assertFalse(modelRunner.isRunning());
    }

    @Test
    public void useCaseModelRunnerIsRunningAfterRunCall() {
	Model model = modelBuilder.build();
	modelRunner.run(model);
	assertTrue(modelRunner.isRunning());
    }

    @Test
    public void useCaseModelRunnerIsNotRunningWhenBeingStoppedBeforeRunCall() {
	modelRunner.stop();
	assertFalse(modelRunner.isRunning());
    }

    @Test
    public void useCaseModelRunnerIsNotRunningWhenBeingStoppedAfterRunCall() {
	Model model = modelBuilder.build();
	modelRunner.run(model);
	modelRunner.stop();
	assertFalse(modelRunner.isRunning());
    }
}
