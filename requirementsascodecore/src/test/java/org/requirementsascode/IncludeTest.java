package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IncludeTest extends AbstractTestCase{
    private static final String INCLUDED_USE_CASE = "Included Use Case";

    @Before
    public void setup() {
      setupWith(new TestUseCaseModelRunner());
    }

    @Test
    public void includeBasicFlowOnce() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase(INCLUDED_USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
            .include(INCLUDED_USE_CASE)
            .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
        .build();
      
    useCaseModelRunner.run(useCaseModel);
    useCaseModelRunner.reactTo(enterText(), enterNumber());

    String expectedSteps =
        SYSTEM_DISPLAYS_TEXT + ";"
            + SYSTEM_DISPLAYS_NUMBER + ";"
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
            .include(INCLUDED_USE_CASE)
        .build();
      
      useCaseModelRunner.run(useCaseModel);
      useCaseModelRunner.reactTo(enterNumber());
      
      assertEquals(SYSTEM_DISPLAYS_NUMBER + ";", runStepNames());
    }
}
