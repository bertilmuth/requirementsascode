package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FlowWithStepConditionsTest extends AbstractTestCase {
  @Before
  public void setup() {
    setupWithRecordingModelRunner();
  }
  
  @Test
  public void reactsWhenConditionIsTrue() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow().condition(() -> true)
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .condition(() -> true).step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .build();
        
    modelRunner.run(model);
    reactToAndAssertEvents(entersText(), entersNumber());   
  }
  
  @Test
  public void doesntReactWhenConditionIsFalse() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow().condition(() -> true)
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .condition(() -> false).step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .build();
        
    modelRunner.run(model).reactTo(entersText(), entersNumber());
    assertEquals(CUSTOMER_ENTERS_TEXT, modelRunner.getLatestStep().get().getName());   
  }
}
