package org.requirementsascode;

import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.requirementsascode.event.EnterText;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.MissingUseCaseStepPart;
import org.requirementsascode.exception.MoreThanOneStepCanReact;
import org.requirementsascode.exception.NoSuchElementInModel;
import org.requirementsascode.exception.NoSuchElementInUseCase;
import org.requirementsascode.exception.UncaughtException;

public class ExceptionsThrownTest extends AbstractTestCase{
	private static final String SAY_HELLO_USE_CASE = "Say Hello Use Case";
	private static final String EXCEPTION_THROWING_USE_CASE = "Exception Throwing Use Case";
	
	private static final String ALTERNATIVE_FLOW_STEP = "Alternative Flow Step";
	private static final String BASIC_FLOW_STEP = "Basic Flow Step";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup() {
		setupWith(new UseCaseRunner());
	}
	
	@Test
	public void throwsExceptionIfAfterStepNotExistsInSameUseCase() {
		String unknownStepName = "Unknown Step";
		
		thrown.expect(NoSuchElementInUseCase.class);
		thrown.expectMessage(SAY_HELLO_USE_CASE);
		thrown.expectMessage(unknownStepName);
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow().after(unknownStepName);
	}
	
	
	@Test
	public void throwsExceptionIfAfterStepNotExistsInOtherUseCase() {
		String unknwonStepName = "Unknown Step";
		
		thrown.expect(NoSuchElementInUseCase.class);
		thrown.expectMessage(SAY_HELLO_USE_CASE);
		thrown.expectMessage(unknwonStepName);
		
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE).basicFlow()
				.step(BASIC_FLOW_STEP);
		
		useCaseModel
			.useCase("Another Use Case").basicFlow()
				.after(unknwonStepName, SAY_HELLO_USE_CASE);
	}
	
	@Test
	public void throwsExceptionIfAfterStepHasNonExistingUseCase() {
		String unknownUseCaseName = "Unknown Use Case";
		 
		thrown.expect(NoSuchElementInModel.class);
		thrown.expectMessage(unknownUseCaseName);
		
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE).basicFlow()
				.step(BASIC_FLOW_STEP);
		
		useCaseModel
			.useCase("Another Use Case").basicFlow()
				.after(BASIC_FLOW_STEP, unknownUseCaseName);
	}
	
	
	@Test
	public void throwsExceptionIfContinueAfterNotExists() {
		String unknownStepName = "Unknown Step";
		
		thrown.expect(NoSuchElementInUseCase.class);
		thrown.expectMessage(SAY_HELLO_USE_CASE);
		thrown.expectMessage(unknownStepName);
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow().continueAfter(unknownStepName);
	}
	
	@Test
	public void throwsExceptionIfActorIsCreatedTwice() {
		String name = "Duplicate Actor";
		
		thrown.expect(ElementAlreadyInModel.class);
		thrown.expectMessage(name);
		
		useCaseModel.actor(name);
		useCaseModel.actor(name);
	}
	
	@Test
	public void throwsExceptionIfUseCaseIsCreatedTwice() {
		String duplicateUseCaseName = "Duplicate Use Case";
		
		thrown.expect(ElementAlreadyInModel.class);
		thrown.expectMessage(duplicateUseCaseName);
		
		useCaseModel.useCase(duplicateUseCaseName);
		useCaseModel.useCase(duplicateUseCaseName);
	}
	
	@Test
	public void throwsExceptionIfFlowIsCreatedTwice() {
		String duplicateFlowName = "Duplicate Flow";
		
		thrown.expect(ElementAlreadyInModel.class);
		thrown.expectMessage(duplicateFlowName);
		
		useCaseModel.useCase(EXCEPTION_THROWING_USE_CASE)
			.flow(duplicateFlowName);
			
		useCaseModel.findUseCase(EXCEPTION_THROWING_USE_CASE).get()
			.flow(duplicateFlowName);
	}
	
	@Test
	public void throwsExceptionIfStepIsCreatedTwice() {
		String duplicateStepName = "Duplicate Step";
		
		thrown.expect(ElementAlreadyInModel.class);
		thrown.expectMessage(duplicateStepName);
		
		useCaseModel.useCase(EXCEPTION_THROWING_USE_CASE)
			.basicFlow()
				.step(duplicateStepName).system(displayConstantText())			
				.step(duplicateStepName).system(displayConstantText());
	}
	
	@Test
	public void throwsExceptionIfMoreThanOneStepCouldReact() { 	 
		thrown.expect(MoreThanOneStepCanReact.class);
		thrown.expectMessage(BASIC_FLOW_STEP);
		thrown.expectMessage(ALTERNATIVE_FLOW_STEP);
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow().when(run -> true)
				.step(BASIC_FLOW_STEP).system(displayConstantText())
			.flow("Alternative Flow: Could react as well").when(run -> true)
				.step(ALTERNATIVE_FLOW_STEP).system(displayConstantText());
		
		useCaseRunner.run();
	}
	
	@Test
	public void throwsExceptionIfActorPartIsNotSpecified() {
		String stepWithoutActor = "Step without actor";
		
		thrown.expect(MissingUseCaseStepPart.class);
		thrown.expectMessage(stepWithoutActor);
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(stepWithoutActor);
			
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());
		
		assertEquals(0, getRunStepNames().size());
	}
	
	@Test
	public void throwsExceptionIfSystemPartIsNotSpecified() {
		String stepWithoutSystemReaction = "Step without system";
		
		thrown.expect(MissingUseCaseStepPart.class);
		thrown.expectMessage(stepWithoutSystemReaction);
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(stepWithoutSystemReaction)
					.as(customer).user(EnterText.class);
			
		useCaseRunner.runAs(customer);
		useCaseRunner.reactTo(enterText());
		
		assertEquals(0, getRunStepNames().size());
	}
	
	@Test
	public void throwsUncaughtExceptionIfExceptionIsNotHandled() {
		String stepThatThrowsException = "Step that throws exception";
		
		thrown.expect(UncaughtException.class);
		thrown.expectCause(isA(IllegalStateException.class));
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
		.basicFlow()
			.step(stepThatThrowsException)
				.system(() -> {throw new IllegalStateException();});
		
		useCaseRunner.run();
	}
}
