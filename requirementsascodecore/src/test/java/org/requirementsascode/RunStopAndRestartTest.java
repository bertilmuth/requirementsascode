package org.requirementsascode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RunStopAndRestartTest extends AbstractTestCase {

    @Before
    public void setUp() throws Exception {
	setupWith(new ModelRunner());

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
    public void modelRunnerIsRunningAfterRunCallAndRestart() {
	Model model = modelBuilder.build();
	modelRunner.run(model);
	modelRunner.restart();
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
    
    @Test
    public void modelRunnerIsRunningWhenBeingStoppedAndRestartedAfterRunCall() {
	Model model = modelBuilder.build();
	modelRunner.run(model);
	modelRunner.stop();
	modelRunner.restart();
	assertTrue(modelRunner.isRunning());
    }
}
