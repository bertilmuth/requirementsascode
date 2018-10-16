package org.requirementsascode;

import org.junit.Before;
import org.junit.Test;

public class IncludesTest extends AbstractTestCase{
    @Before
    public void setup() {
      setupWithRecordingModelRunner();
    }
    
  @Test
  public void includesUseCaseWithBasicFlowAtFirstStep_withoutCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersNumber(), entersNumber());

    assertRecordedStepNames(SYSTEM_INCLUDES_USE_CASE, 
	    CUSTOMER_ENTERS_NUMBER, SYSTEM_DISPLAYS_TEXT);
  }  
    
  @Test
  public void includesUseCaseWithBasicFlowAtFirstStep_withAnytimeCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().anytime()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersNumber(), entersNumber());
    
    assertRecordedStepNames(SYSTEM_INCLUDES_USE_CASE,
	    CUSTOMER_ENTERS_NUMBER, SYSTEM_DISPLAYS_TEXT, CUSTOMER_ENTERS_NUMBER);
  }

  @Test
  public void includesUseCaseWithBasicFlowAtFirstStep_withFalseCondition_cantReact() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().condition(() -> false)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersNumber(), entersNumber());

    assertRecordedStepNames(SYSTEM_INCLUDES_USE_CASE);
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtFirstStep_withoutCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
        .flow(ALTERNATIVE_FLOW)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersNumber(), entersNumber());

    assertRecordedStepNames(SYSTEM_INCLUDES_USE_CASE, CUSTOMER_ENTERS_NUMBER, SYSTEM_DISPLAYS_TEXT);
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtFirstStep_witAnytimeCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
        .flow(ALTERNATIVE_FLOW).anytime()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersNumber(), entersNumber());

    assertRecordedStepNames(SYSTEM_INCLUDES_USE_CASE, 
	    CUSTOMER_ENTERS_NUMBER, SYSTEM_DISPLAYS_TEXT,
	    CUSTOMER_ENTERS_NUMBER);
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtFirstStep_witInsteadOfCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
        .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersNumber(), entersNumber());

    assertRecordedStepNames(SYSTEM_INCLUDES_USE_CASE, 
	    CUSTOMER_ENTERS_NUMBER, SYSTEM_DISPLAYS_TEXT);
  }
    
  @Test
  public void includesUseCaseWithAlternativeFlowAtFirstStep_withFalseCondition_cantReact() {
      Model model = modelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow()
            .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .flow(ALTERNATIVE_FLOW).condition(() -> false)
            .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
            .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
        .build();
      
    modelRunner.run(model).reactTo(entersNumber(), entersNumber());
    
    assertRecordedStepNames(SYSTEM_INCLUDES_USE_CASE);
  }
    
  @Test
  public void includesUseCaseWithBasicFlowAtSecondStep_withoutCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersNumber());
  
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, 
	    SYSTEM_INCLUDES_USE_CASE, CUSTOMER_ENTERS_NUMBER,
	    SYSTEM_DISPLAYS_TEXT);
  }
  
  @Test
  public void includesUseCaseWithBasicFlowAtSecondStep_withAnytimeCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().anytime()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersNumber());
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, 
	    SYSTEM_INCLUDES_USE_CASE, CUSTOMER_ENTERS_NUMBER,
	    SYSTEM_DISPLAYS_TEXT, CUSTOMER_ENTERS_NUMBER);
  }
  
  @Test
  public void includesUseCaseWithBasicFlowAtSecondStep_withFalseCondition_cantReact() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().condition(() -> false)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersNumber());
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, 
	    SYSTEM_INCLUDES_USE_CASE);
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtSecondStep_withoutCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_HANDLES_EXCEPTION).user(Throwable.class).system(e -> e.printStackTrace())
        .flow(ALTERNATIVE_FLOW)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersNumber());
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, 
	    SYSTEM_INCLUDES_USE_CASE, CUSTOMER_ENTERS_NUMBER,
	    SYSTEM_DISPLAYS_TEXT);
  }
    
  @Test
  public void includesUseCaseWithAlternativeFlowAtSecondStep_withAnytimeCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_HANDLES_EXCEPTION).user(Throwable.class).system(e -> e.printStackTrace())
        .flow(ALTERNATIVE_FLOW).anytime()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersNumber());
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, 
	    SYSTEM_INCLUDES_USE_CASE, CUSTOMER_ENTERS_NUMBER,
	    SYSTEM_DISPLAYS_TEXT, CUSTOMER_ENTERS_NUMBER);    
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtSecondStep_withInsteadOfCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_HANDLES_EXCEPTION).user(Throwable.class).system(e -> e.printStackTrace())
        .flow(ALTERNATIVE_FLOW).insteadOf(SYSTEM_HANDLES_EXCEPTION)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersNumber());
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, 
	    SYSTEM_INCLUDES_USE_CASE, CUSTOMER_ENTERS_NUMBER,
	    SYSTEM_DISPLAYS_TEXT);    
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtSecondStep_withFalseCondition() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_HANDLES_EXCEPTION).user(Throwable.class).system(e -> e.printStackTrace())
        .flow(ALTERNATIVE_FLOW).condition(() -> false)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersNumber());
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, 
	    SYSTEM_INCLUDES_USE_CASE);
  }
    
  @Test
  public void includeUseCaseTwoConsecutiveTimes() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_INCLUDES_USE_CASE_2).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersNumber());
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, 
	    SYSTEM_INCLUDES_USE_CASE, CUSTOMER_ENTERS_NUMBER,
	    SYSTEM_INCLUDES_USE_CASE_2, CUSTOMER_ENTERS_NUMBER,
	    SYSTEM_DISPLAYS_TEXT);
  }
    
  @Test
  public void includeUseCaseTwoNonConsecutiveTimes() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
          .step(SYSTEM_INCLUDES_USE_CASE_2).includesUseCase(INCLUDED_USE_CASE)
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersNumber());
    
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, 
	    SYSTEM_INCLUDES_USE_CASE, CUSTOMER_ENTERS_NUMBER,
	    SYSTEM_DISPLAYS_TEXT, SYSTEM_INCLUDES_USE_CASE_2,
	    CUSTOMER_ENTERS_NUMBER);
  }
  
  @Test
  public void includeUseCaseFromTwoDifferentUseCases() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow().anytime()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE_3).includesUseCase(INCLUDED_USE_CASE)
          .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
      .useCase(USE_CASE_2)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_INCLUDES_USE_CASE_2).includesUseCase(INCLUDED_USE_CASE)
      .build();
      
    modelRunner.run(model).reactTo(entersNumber(), entersNumber(), entersText(), entersNumber(), entersText(), entersNumber());
    
    assertRecordedStepNames(SYSTEM_INCLUDES_USE_CASE, 
	    CUSTOMER_ENTERS_NUMBER, SYSTEM_INCLUDES_USE_CASE_2,
	    CUSTOMER_ENTERS_NUMBER, CUSTOMER_ENTERS_TEXT,
	    SYSTEM_INCLUDES_USE_CASE_3, CUSTOMER_ENTERS_NUMBER,
	    CUSTOMER_ENTERS_TEXT_AGAIN);
  }
    
  @Test
  public void includedUseCaseCanBeRunOnItsOwn() {
    Model model = modelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
      .build();
      
    modelRunner.run(model).reactTo(entersNumber());

    assertRecordedStepNames(CUSTOMER_ENTERS_NUMBER);
  }
  
  @Test
  public void includesUseCaseThatIncludesUseCase_withoutCondition() {
    Model model = modelBuilder
      .useCase(USE_CASE_2)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE_2).includesUseCase(USE_CASE_2)
          .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    modelRunner.run(model).reactTo(entersText(), entersNumber(), entersText());

    assertRecordedStepNames(SYSTEM_INCLUDES_USE_CASE,
	    CUSTOMER_ENTERS_TEXT, SYSTEM_INCLUDES_USE_CASE_2, 
	    CUSTOMER_ENTERS_NUMBER, CUSTOMER_ENTERS_TEXT_AGAIN,
	    SYSTEM_DISPLAYS_TEXT);
  }  
}
