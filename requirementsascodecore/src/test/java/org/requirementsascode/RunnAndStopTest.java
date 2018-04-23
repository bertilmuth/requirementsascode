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
    public void modelRunnerIsNotRunningAtFirst() {
	assertFalse(modelRunner.isRunning());
    }

    @Test
    public void modelRunnerIsRunningAfterRunCall() {
	Model model = modelBuilder.build();
	modelRunner.run(model);
	assertTrue(modelRunner.isRunning());
    }

    @Test
    public void modelRunnerIsNotRunningWhenBeingStoppedBeforeRunCall() {
	modelRunner.stop();
	assertFalse(modelRunner.isRunning());
    }

    @Test
    public void modelRunnerIsNotRunningWhenBeingStoppedAfterRunCall() {
	Model model = modelBuilder.build();
	modelRunner.run(model);
	modelRunner.stop();
	assertFalse(modelRunner.isRunning());
    }
}
