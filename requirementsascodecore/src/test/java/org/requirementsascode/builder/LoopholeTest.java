package org.requirementsascode.builder;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.AbstractTestCase;
import org.requirementsascode.Model;

public class LoopholeTest extends AbstractTestCase{

  @Before
  public void setup() {
    setupWithRecordingModelRunner();
  }
  
  @Test
  public void withFlow_createsSingleStepThatHandlesEvent() {
    Model model = 
      modelBuilder
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_TEXT).on(EntersText.class).system(displaysEnteredText())
        .useCase("")
          //.on(Object.class).system(o -> {})
      .build();
  }
}
