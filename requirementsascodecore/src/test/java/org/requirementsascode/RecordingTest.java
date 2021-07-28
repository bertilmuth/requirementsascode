package org.requirementsascode;

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
    Model model = modelBuilder.useCase(USE_CASE)
      .on(EntersText.class).system(displaysEnteredText())
      .build();

    modelRunner.run(model); 
    modelRunner.reactTo(entersText());

    assertRecordedStepNames("S1");
  }

  @Test
  public void recordMultipleEvents_startRecordingAfterRunning() {
    Model model = modelBuilder.useCase(USE_CASE)
      .on(EntersText.class).system(displaysEnteredText())
      .on(EntersNumber.class).system(displaysEnteredNumber())
      .build();

    modelRunner.run(model);
    modelRunner.reactTo(entersText(), entersNumber());

    assertRecordedStepNames("S1","S2");
  }

  @Test
  public void recordMultipleEvents() {
    Model model = modelBuilder.useCase(USE_CASE)
      .on(EntersText.class).system(displaysEnteredText())
      .on(EntersNumber.class).system(displaysEnteredNumber())
      .build();

    modelRunner.run(model);
    modelRunner.reactTo(entersText(), entersNumber());

    assertRecordedStepNames("S1","S2");
  }

  @Test
  public void continueRecordingAfterRestart() {
    Model model = modelBuilder.useCase(USE_CASE)
      .on(EntersText.class).system(displaysEnteredText())
      .on(EntersNumber.class).system(displaysEnteredNumber())
      .build();

    modelRunner.run(model);
    modelRunner.reactTo(entersText());
    modelRunner.restart();
    modelRunner.reactTo(entersNumber());

    assertRecordedStepNames("S1","S2");
  }
}
