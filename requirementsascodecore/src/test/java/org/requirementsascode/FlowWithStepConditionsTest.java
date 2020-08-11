package org.requirementsascode;

import org.junit.Before;
import org.junit.Test;

public class FlowWithStepConditionsTest extends AbstractTestCase {
  @Before
  public void setup() {
    setupWithRecordingModelRunner();
  }
  
  @Test
  public void wtoStepsInFlowWithTrueConditionReact() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow().condition(() -> true)
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .condition(() -> true).step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .build();
        
    modelRunner.run(model);
    reactToAndAssertEvents(entersText(), entersNumber());   
  }
}
