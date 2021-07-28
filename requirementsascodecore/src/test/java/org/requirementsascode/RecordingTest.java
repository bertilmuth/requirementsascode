package org.requirementsascode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecordingTest extends AbstractTestCase {

  @BeforeEach
  public void setup() throws Exception {
    setupWithRecordingModelRunner();
  }

  @Test
  public void recordIsEmptyForFreshlyRunModel() {
    Model model = modelBuilder.useCase(USE_CASE)
      .on(EntersText.class).system(displaysEnteredText())
      .build();

    modelRunner.run(model);
    
    assertRecordedStepNames();
  }

  @Test
  public void recordIsEmptyIfRecordingWasStoppedBeforeBeingStarted() {
    Model model = modelBuilder.useCase(USE_CASE)
      .on(EntersText.class).system(displaysEnteredText())
      .build();

    modelRunner.run(model).stopRecording();
    assertRecordedStepNames();
  }

  @Test
  public void recordSingleEvent() {
    Model model = modelBuilder.useCase(USE_CASE).on(EntersText.class).system(displaysEnteredText()).build();

    modelRunner.run(model).startRecording();
    modelRunner.reactTo(entersText());

    assertEquals(1, modelRunner.getRecordedMessages().length);
    assertEquals(EntersText.class, modelRunner.getRecordedMessages()[0].getClass());

    assertRecordedStepNames("S1");
  }

  @Test
  public void recordMultipleEvents_startRecordingAfterRunning() {
    Model model = modelBuilder.useCase(USE_CASE).on(EntersText.class).system(displaysEnteredText()).on(
      EntersNumber.class).system(displaysEnteredNumber()).build();

    modelRunner.run(model).startRecording();
    modelRunner.reactTo(entersText(), entersNumber());

    assertEquals(2, modelRunner.getRecordedMessages().length);
    assertEquals(EntersText.class, modelRunner.getRecordedMessages()[0].getClass());
    assertEquals(EntersNumber.class, modelRunner.getRecordedMessages()[1].getClass());

    assertRecordedStepNames("S1","S2");
  }

  @Test
  public void recordMultipleEvents_startRecordingBeforeRunning() {
    Model model = modelBuilder.useCase(USE_CASE).on(EntersText.class).system(displaysEnteredText()).on(
      EntersNumber.class).system(displaysEnteredNumber()).build();

    modelRunner.startRecording().run(model);
    modelRunner.reactTo(entersText(), entersNumber());

    assertEquals(2, modelRunner.getRecordedMessages().length);
    assertEquals(EntersText.class, modelRunner.getRecordedMessages()[0].getClass());
    assertEquals(EntersNumber.class, modelRunner.getRecordedMessages()[1].getClass());

    assertRecordedStepNames("S1","S2");
  }

  @Test
  public void noMoreRecordingAfterRecordingIsStopped() {
    Model model = modelBuilder.useCase(USE_CASE).on(EntersText.class).system(displaysEnteredText()).on(
      EntersNumber.class).system(displaysEnteredNumber()).build();

    modelRunner.run(model).startRecording();
    modelRunner.reactTo(entersText());
    modelRunner.stopRecording();
    modelRunner.reactTo(entersNumber());

    assertEquals(1, modelRunner.getRecordedMessages().length);
    assertEquals(EntersText.class, modelRunner.getRecordedMessages()[0].getClass());

    assertRecordedStepNames("S1");
  }

  @Test
  public void continueRecordingAfterRestart() {
    Model model = modelBuilder.useCase(USE_CASE).on(EntersText.class).system(displaysEnteredText()).on(
      EntersNumber.class).system(displaysEnteredNumber()).build();

    modelRunner.run(model).startRecording();
    modelRunner.reactTo(entersText());
    modelRunner.restart();
    modelRunner.reactTo(entersNumber());

    assertEquals(2, modelRunner.getRecordedMessages().length);
    assertEquals(EntersText.class, modelRunner.getRecordedMessages()[0].getClass());
    assertEquals(EntersNumber.class, modelRunner.getRecordedMessages()[1].getClass());

    assertRecordedStepNames("S1","S2");
  }
}
