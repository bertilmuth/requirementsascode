package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IncludesTest extends AbstractTestCase{
    @Before
    public void setup() {
      setupWith(new TestUseCaseModelRunner());
    }
    
  @Test
  public void includesUseCaseWithBasicFlowAtFirstStep_withoutPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersNumber(), entersNumber());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + CUSTOMER_ENTERS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }  
    
  @Test
  public void includesUseCaseWithBasicFlowAtFirstStep_withAnytimePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().anytime()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersNumber(), entersNumber());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + CUSTOMER_ENTERS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";"
        + CUSTOMER_ENTERS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }

  @Test
  public void includesUseCaseWithBasicFlowAtFirstStep_withFalsePredicate_cantReact() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().when(r -> false)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersNumber(), entersNumber());

    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtFirstStep_withoutPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersNumber(), entersNumber());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + CUSTOMER_ENTERS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtFirstStep_witAnytimePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersNumber(), entersNumber());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + CUSTOMER_ENTERS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";"
        + CUSTOMER_ENTERS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtFirstStep_witInsteadOfPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersNumber(), entersNumber());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + CUSTOMER_ENTERS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includesUseCaseWithAlternativeFlowAtFirstStep_withFalsePredicate_cantReact() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow()
            .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .flow(ALTERNATIVE_FLOW).when(r -> false)
            .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
            .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
        .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersNumber(), entersNumber());

    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includesUseCaseWithBasicFlowAtSecondStep_withoutPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersNumber());
  
    String expectedSteps =
        CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includesUseCaseWithBasicFlowAtSecondStep_withAnytimePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().anytime()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersNumber());
  
    String expectedSteps =
        CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT + ";"
          + CUSTOMER_ENTERS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includesUseCaseWithBasicFlowAtSecondStep_withFalsePredicate_cantReact() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().when(r -> false)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersNumber());
  
    String expectedSteps =
        CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtSecondStep_withoutPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersNumber());
  
    String expectedSteps =
        CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includesUseCaseWithAlternativeFlowAtSecondStep_withAnytimePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersNumber());
  
    String expectedSteps =
        CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT + ";"
          + CUSTOMER_ENTERS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtSecondStep_withInsteadOfPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersNumber());
  
    String expectedSteps =
        CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includesUseCaseWithAlternativeFlowAtSecondStep_withFalsePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_HANDLES_EXCEPTION).user(Throwable.class).system(e -> e.printStackTrace())
        .flow(ALTERNATIVE_FLOW).when(r -> false)
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersNumber());
  
    String expectedSteps =
        CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includeUseCaseTwoConsecutiveTimes() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersNumber());

    String expectedSteps =
        CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + SYSTEM_INCLUDES_USE_CASE_2 + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includeUseCaseTwoNonConsecutiveTimes() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersNumber());

    String expectedSteps =
        CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE_2 + ";"
          + CUSTOMER_ENTERS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includeUseCaseFromTwoDifferentUseCases() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersNumber(), entersNumber(), entersText(), entersNumber(), entersText(), entersNumber());

    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + SYSTEM_INCLUDES_USE_CASE_2 + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + CUSTOMER_ENTERS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE_3 + ";"
          + CUSTOMER_ENTERS_NUMBER + ";"
          + CUSTOMER_ENTERS_TEXT_AGAIN + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includedUseCaseCanBeRunOnItsOwn() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includesUseCase(INCLUDED_USE_CASE)
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersNumber());

    assertEquals(CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
  }
  
  @Test
  public void includesUseCaseThatIncludesUseCase_withoutPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
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
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(entersText(), entersNumber(), entersText());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + CUSTOMER_ENTERS_TEXT + ";"
        + SYSTEM_INCLUDES_USE_CASE_2 + ";"
        + CUSTOMER_ENTERS_NUMBER + ";"
        + CUSTOMER_ENTERS_TEXT_AGAIN + ";"
        + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }  
}
