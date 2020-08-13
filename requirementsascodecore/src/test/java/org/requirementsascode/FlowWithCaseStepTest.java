package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class FlowWithCaseStepTest extends AbstractTestCase {
  @Before
  public void setup() {
    setupWithRecordingModelRunner();
  }
  
  @Test
  public void runsTrueCaseStep() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).inCase(() -> true).system(displaysConstantText())
      .build();
        
    modelRunner.run(model);
    assertEquals(TEXT, displayedText);
  }
  
  @Test
  public void runsFalseCaseTrueCaseSteps() {    
    /*Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).inCase(() -> false).user(EntersNumber.class).system(displaysEnteredNumber())
          .step(SYSTEM_DISPLAYS_TEXT).system(super.displaysConstantText())
      .build();
        
    modelRunner.run(model).reactTo(entersNumber());
    assertEquals(TEXT, displayedText);
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, SYSTEM_DISPLAYS_TEXT);*/
  }
  
  @Test
  public void runsFalseCaseFalseCaseSteps() {    
    /*Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).inCase(() -> false).user(EntersNumber.class).system(displaysEnteredNumber())
          .step(SYSTEM_DISPLAYS_TEXT).inCase(() -> false).system(displaysConstantText())
      .build();
        
    modelRunner.run(model).reactTo(entersNumber(), entersText());
    assertNull(displayedText);
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, SYSTEM_DISPLAYS_TEXT);*/
  }
}
