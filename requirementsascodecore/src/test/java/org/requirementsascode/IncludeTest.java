package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IncludeTest extends AbstractTestCase{
    @Before
    public void setup() {
      setupWith(new TestUseCaseModelRunner());
    }
    
  @Test
  public void includeUseCaseWithBasicFlowAtFirstStep_withoutPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterNumber(), enterNumber());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + SYSTEM_DISPLAYS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }  
    
  @Test
  public void includeUseCaseWithBasicFlowAtFirstStep_withAnytimePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().anytime()
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterNumber(), enterNumber());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + SYSTEM_DISPLAYS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";"
        + SYSTEM_DISPLAYS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }

  @Test
  public void includeUseCaseWithBasicFlowAtFirstStep_withFalsePredicate_cantReact() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().when(r -> false)
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterNumber(), enterNumber());

    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includeUseCaseWithAlternativeFlowAtFirstStep_withoutPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .flow(ALTERNATIVE_FLOW)
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterNumber(), enterNumber());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + SYSTEM_DISPLAYS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includeUseCaseWithAlternativeFlowAtFirstStep_witAnytimePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .flow(ALTERNATIVE_FLOW).anytime()
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterNumber(), enterNumber());
    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + SYSTEM_DISPLAYS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";"
        + SYSTEM_DISPLAYS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includeUseCaseWithAlternativeFlowAtFirstStep_withFalsePredicate_cantReact() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .flow(ALTERNATIVE_FLOW).when(r -> false)
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
            .step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
        .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterNumber(), enterNumber());

    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includeUseCaseWithBasicFlowAtSecondStep_withoutPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber(), enterNumber());
  
    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includeUseCaseWithBasicFlowAtSecondStep_withAnytimePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().anytime()
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber(), enterNumber());
  
    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includeUseCaseWithBasicFlowAtSecondStep_withFalsePredicate_cantReact() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow().when(r -> false)
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber(), enterNumber());
  
    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includeUseCaseWithAlternativeFlowAtSecondStep_withoutPredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_HANDLES_EXCEPTION).user(Throwable.class).system(e -> e.printStackTrace())
        .flow(ALTERNATIVE_FLOW)
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber(), enterNumber());
  
    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includeUseCaseWithAlternativeFlowAtSecondStep_withAnytimePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_HANDLES_EXCEPTION).user(Throwable.class).system(e -> e.printStackTrace())
        .flow(ALTERNATIVE_FLOW).anytime()
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber(), enterNumber());
  
    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includeUseCaseWithAlternativeFlowAtSecondStep_withFalsePredicate() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_HANDLES_EXCEPTION).user(Throwable.class).system(e -> e.printStackTrace())
        .flow(ALTERNATIVE_FLOW).when(r -> false)
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber(), enterNumber());
  
    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includeUseCaseTwoConsecutiveTimes() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_INCLUDES_USE_CASE_2).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber(), enterNumber());

    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";"
          + SYSTEM_INCLUDES_USE_CASE_2 + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includeUseCaseTwoNonConsecutiveTimes() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
          .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
          .step(SYSTEM_INCLUDES_USE_CASE_2).includeUseCase(INCLUDED_USE_CASE)
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber(), enterNumber());

    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";"
          + SYSTEM_INCLUDES_USE_CASE_2 + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }
  
  @Test
  public void includeUseCaseFromTwoDifferentUseCases() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
      .useCase(USE_CASE)
        .basicFlow().when(r -> true)
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
      .useCase(USE_CASE_2)
        .basicFlow().when(r -> true)
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE_2).includeUseCase(INCLUDED_USE_CASE)
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterText(), enterNumber(), enterText());

    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE_2 + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includedUseCaseCanBeRunOnItsOwn() {
    UseCaseModel useCaseModel = useCaseModelBuilder
      .useCase(INCLUDED_USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
      .useCase(USE_CASE)
        .basicFlow()
          .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
          .step(SYSTEM_INCLUDES_USE_CASE).includeUseCase(INCLUDED_USE_CASE)
      .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterNumber());

    assertEquals(SYSTEM_DISPLAYS_NUMBER + ";", runStepNames());
  }
}
