package org.requirementsascode;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class FlowWithCaseStepTest extends AbstractTestCase {
  @Before
  public void setup() {
    setupWithRecordingModelRunner();
  }
  
  @Test
  public void runsStepWithTrueCase() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).inCase(() -> true).user(EntersText.class).system(displaysEnteredText())
      .build();
        
    modelRunner.run(model);
    reactToAndAssertEvents(entersText());
  }
  
  @Test
  @Ignore
  public void doesntRunsStepWithFalseCase() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).inCase(() -> false).user(EntersText.class).system(displaysEnteredText())
      .build();
        
    modelRunner.run(model).reactTo(entersText());
    assertFalse(modelRunner.getLatestStep().isPresent());   
  }
}
