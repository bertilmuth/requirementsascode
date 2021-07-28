package org.requirementsascode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RunStopAndRestartTest extends AbstractTestCase {

  @BeforeEach
  public void setup() throws Exception {
    setupWithRecordingModelRunner();

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
