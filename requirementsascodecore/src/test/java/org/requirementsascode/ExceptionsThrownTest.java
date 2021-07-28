package org.requirementsascode;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.InfiniteRepetition;
import org.requirementsascode.exception.MissingUseCaseStepPart;
import org.requirementsascode.exception.MoreThanOneStepCanReact;
import org.requirementsascode.exception.NestedCallOfReactTo;
import org.requirementsascode.exception.NoSuchElementInModel;

public class ExceptionsThrownTest extends AbstractTestCase {
	@BeforeEach
	public void setup() {
		setupWithRecordingModelRunner();
	}

  @Test
  public void throwsExceptionIfInsteadOfStepNotExistsInSameUseCase() {
    assertThrows(NoSuchElementInModel.class, () -> {
      modelBuilder
        .useCase(USE_CASE)
          .basicFlow().insteadOf(CUSTOMER_ENTERS_TEXT)
            .step("S1").system(displaysConstantText())
          .build();
    }, CUSTOMER_ENTERS_TEXT);
  }

	@Test
	public void throwsExceptionIfAfterStepNotExistsInSameUseCase() {
    assertThrows(NoSuchElementInModel.class, () -> {
      modelBuilder
      .useCase(USE_CASE)
        .basicFlow().after(CUSTOMER_ENTERS_TEXT)
          .step("S1").system(displaysConstantText())
        .build();
    }, CUSTOMER_ENTERS_TEXT);
	}

	@Test
	public void throwsExceptionIfContinueAfterNotExists() {
		assertThrows(NoSuchElementInModel.class, () -> {
      modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step("S1").continuesAfter(CONTINUE)
        .build();
    }, CONTINUE);
	}

	@Test
	public void throwsExceptionIfContinueAtNotExists() {
    assertThrows(NoSuchElementInModel.class, () -> {
      modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step("S1").continuesAt(CONTINUE)
        .build();
    }, CONTINUE);
	}

	@Test
	public void throwsExceptionIfContinueWithoutAlternativeAtNotExists() {
    assertThrows(NoSuchElementInModel.class, () -> {
      modelBuilder.useCase(USE_CASE)
        .basicFlow()
          .step("S1").continuesAt(CONTINUE)
        .build();
    }, CONTINUE);
	}

	@Test
	public void throwsExceptionIfFlowIsCreatedTwice() {
		assertThrows(ElementAlreadyInModel.class, () -> {
  		modelBuilder.useCase(USE_CASE)
  			.flow(ALTERNATIVE_FLOW)
  				.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
  			.flow(ALTERNATIVE_FLOW)
  			  .step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText())
  			.build();
    }, ALTERNATIVE_FLOW);
	}

	@Test
	public void throwsExceptionIfStepIsCreatedTwice() {
    assertThrows(ElementAlreadyInModel.class, () -> {
  		modelBuilder.useCase(USE_CASE)
  			.basicFlow()
  				.step(CUSTOMER_ENTERS_TEXT).system(displaysConstantText())
  				.step(CUSTOMER_ENTERS_TEXT).system(displaysConstantText())
  			.build();
    }, CUSTOMER_ENTERS_TEXT);
	}

	@Test
	public void throwsExceptionWhenConditionIsAlwaysTrue() {
    assertThrows(InfiniteRepetition.class, () -> {
  		Model model = modelBuilder
  		  .condition(() -> true).system(() -> {})
  		.build();
  		
      modelRunner.run(model);
    }, "S1");
	}

  @Test
  public void throwsExceptionWhenReactToIsCalledFromSystemReaction() {
    assertThrows(NestedCallOfReactTo.class, () -> {
      Model model = modelBuilder.useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).system(() -> modelRunner.reactTo(""))
      .build();
  
      modelRunner.run(model);
    });
  }

  @Test
  public void throwsExceptionIfMoreThanOneStepCanReactInSameUseCase() {
    assertThrows(MoreThanOneStepCanReact.class, () -> {
  		Model model = modelBuilder.useCase(USE_CASE)
  			.basicFlow().anytime()
  				.step(CUSTOMER_ENTERS_TEXT).system(displaysConstantText())
  			.flow(ALTERNATIVE_FLOW).anytime()
  				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).system(displaysConstantText())
  			.build();
  
      modelRunner.run(model);
    }, CUSTOMER_ENTERS_TEXT);
  }

	@Test
	public void throwsExceptionIfMoreThanOneStepCanReactInDifferentUseCases() {
		assertThrows(MoreThanOneStepCanReact.class, () -> {
  		Model model = modelBuilder
  			.useCase("Use Case")
  				.basicFlow()
  					.step("Step 1").user(String.class).system(s -> System.out.println(s))
  			.useCase("Another Use Case")
  				.basicFlow()
  					.step("Step 2 with same event as Step 1").user(String.class).system(s -> System.out.println(s))
  			.build();
  
  		modelRunner.run(model);
  		modelRunner.reactTo("Some text");
    }, "Step 2 with same event as Step 1");
	}

	@Test
	public void throwsExceptionIfActorPartIsNotSpecified() {
    assertThrows(MissingUseCaseStepPart.class, () -> {
  		modelBuilder.useCase(USE_CASE).basicFlow().step(CUSTOMER_ENTERS_TEXT);
  
  		Model model = modelBuilder.build();
  
  		modelRunner.run(model);
    }, CUSTOMER_ENTERS_TEXT);
	}

	@Test
	public void throwsExceptionIfSystemPartIsNotSpecified() {
		assertThrows(MissingUseCaseStepPart.class, () -> {
  		modelBuilder.useCase(USE_CASE).basicFlow().step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class);
  
  		Model model = modelBuilder.build();
  
  		modelRunner.as(customer).run(model);
  		modelRunner.reactTo(entersText());
    }, CUSTOMER_ENTERS_TEXT);
	}

	@Test
	public void rethrowsExceptionIfExceptionIsNotHandled() {		
		assertThrows(IllegalStateException.class, () -> {
  		modelBuilder.useCase(USE_CASE)
  		  .basicFlow()
  		    .step(CUSTOMER_ENTERS_TEXT).system(() -> {throw new IllegalStateException();});
  
  		Model model = modelBuilder.build();
  
  		modelRunner.run(model);
    });
	}
}
