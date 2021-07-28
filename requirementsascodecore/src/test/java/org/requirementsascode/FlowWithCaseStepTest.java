package org.requirementsascode;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FlowWithCaseStepTest {
  private String actualResult;
  private ModelRunner modelRunner;
  
  @BeforeEach
  public void setup() {
    this.modelRunner = new ModelRunner().startRecording();
  }
  
  @Test
  public void runsTrueStepWithoutEvent() {  
    Model model = Model.builder()
      .useCase("inCase Test")
        .basicFlow()
          .step("step1").inCase(() -> true).system(() -> actualResult = "step1 run")
      .build();
        
    modelRunner.run(model);
    assertEquals("step1 run", actualResult);
    assertRecordedStepNames("step1");
  }
  
  @Test
  public void runsTrueStepWithEvent() {    
    Model model = Model.builder()
      .useCase("inCase Test")
        .basicFlow()
          .step("step1").on(String.class).inCase(() -> true).system(str -> actualResult = str)
      .build();
        
    modelRunner.run(model).reactTo("event");
    assertEquals("event", actualResult);
    
    assertRecordedStepNames("step1");
  }
  
  @Test
  public void runsFalseStepTrueStep() {    
    Model model = Model.builder()
      .useCase("inCase Test")
        .basicFlow()
          .step("step1").on(String.class).inCase(() -> false).system(str -> actualResult = str)
          .step("step2").on(String.class).inCase(() -> true).system(str -> actualResult = str)
      .build();
        
    modelRunner.run(model).reactTo("event1", "event2");
    assertEquals("event2", actualResult);
    
    assertRecordedStepNames("step2");
  }
  
  @Test
  public void runsFalseStepFalseStep() {    
    Model model = Model.builder()
      .useCase("inCase Test")
        .basicFlow()
          .step("step1").user(String.class).inCase(() -> false).system(str -> actualResult = str)
          .step("step2").inCase(() -> false).system(str -> actualResult = "not evaluated")
      .build();
        
    modelRunner.run(model).reactTo("event1", "event2");
    assertNull(actualResult);
    
    assertRecordedStepNames();
  }
  
  protected void assertRecordedStepNames(String... expectedStepNames) {
    String[] actualStepNames = modelRunner.getRecordedStepNames();
    assertArrayEquals(expectedStepNames, actualStepNames);
  }
}
