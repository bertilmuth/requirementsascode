package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IncludeTest extends AbstractTestCase{
    private static final String SYSTEM_INCLUDES_USE_CASE = "Step that includes use case";
    private static final String SYSTEM_INCLUDES_USE_CASE_2 = "Another step that includes use case";
    private static final String INCLUDED_USE_CASE = "Included use case";

    @Before
    public void setup() {
      setupWith(new TestUseCaseModelRunner());
    }

    @Test
    public void includeBasicFlowOnceAtFirstStep() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow().when(r -> false)
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_INCLUDES_USE_CASE).include(INCLUDED_USE_CASE)
            .step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
        .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterNumber());

    String expectedSteps =
        SYSTEM_INCLUDES_USE_CASE + ";"
        + SYSTEM_DISPLAYS_NUMBER + ";"
        + SYSTEM_DISPLAYS_TEXT + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
    @Test
    public void includeBasicFlowOnceAtSecondStep() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
            .step(SYSTEM_INCLUDES_USE_CASE).include(INCLUDED_USE_CASE)
            .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
        .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber());

    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
    @Test
    public void includeBasicFlowOnceAtLastStep() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
            .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
            .step(SYSTEM_INCLUDES_USE_CASE).include(INCLUDED_USE_CASE)
        .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber());

    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
          + SYSTEM_DISPLAYS_TEXT_AGAIN + ";"
          + SYSTEM_INCLUDES_USE_CASE + ";"
          + SYSTEM_DISPLAYS_NUMBER + ";";
    assertEquals(expectedSteps, runStepNames());
  }
    
  @Test
  public void includeBasicFlowTwoConsecutiveTimes() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
            .step(SYSTEM_INCLUDES_USE_CASE).include(INCLUDED_USE_CASE)
            .step(SYSTEM_INCLUDES_USE_CASE_2).include(INCLUDED_USE_CASE)
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
  public void includeBasicFlowTwoNonConsecutiveTimes() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
            .step(SYSTEM_INCLUDES_USE_CASE).include(INCLUDED_USE_CASE)
            .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
            .step(SYSTEM_INCLUDES_USE_CASE_2).include(INCLUDED_USE_CASE)
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
  public void includeBasicFlowFromTwoDifferentUseCases() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow().when(r -> false)
            .step(SYSTEM_DISPLAYS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
        .useCase(USE_CASE)
          .basicFlow().when(r -> true)
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
            .step(SYSTEM_INCLUDES_USE_CASE).include(INCLUDED_USE_CASE)
        .useCase(USE_CASE_2)
          .basicFlow().when(r -> true)
            .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
            .step(SYSTEM_INCLUDES_USE_CASE_2).include(INCLUDED_USE_CASE)
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
  public void includedBasicFlowCanBeRunOnItsOwn() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
            .step(SYSTEM_INCLUDES_USE_CASE).include(INCLUDED_USE_CASE)
        .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterNumber());

    assertEquals(SYSTEM_DISPLAYS_NUMBER + ";", runStepNames());
  }
}
