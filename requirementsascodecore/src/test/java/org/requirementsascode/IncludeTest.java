package org.requirementsascode;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class IncludeTest extends AbstractTestCase{
    @Before
    public void setup() {
      setupWith(new TestUseCaseModelRunner());
    }

    @Test
    public void includeBasicFlowAfterFirstStep() {
      UseCaseModel useCaseModel = useCaseModelBuilder
        .useCase("Included Use Case")
          .basicFlow()
            .step(SYSTEM_DISPLAYS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
        .useCase(USE_CASE)
          .basicFlow()
            .step(SYSTEM_DISPLAYS_TEXT).user(EnterText.class).system(displayEnteredText())
            .include("Included Use Case")
        .build();
      
      useCaseModelRunner.run(useCaseModel);
      useCaseModelRunner.reactTo(enterText(), enterNumber());
      
      assertEquals(SYSTEM_DISPLAYS_TEXT + ";" + SYSTEM_DISPLAYS_NUMBER + ";", runStepNames());
    }
}
