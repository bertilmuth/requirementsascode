package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.requirementsascode.event.EnterTextEvent;
import org.requirementsascode.exception.ElementAlreadyInModelException;
import org.requirementsascode.exception.MissingUseCaseStepPartException;
import org.requirementsascode.exception.MoreThanOneStepCouldReactException;
import org.requirementsascode.exception.NoSuchElementInModelException;
import org.requirementsascode.exception.NoSuchElementInUseCaseException;

public class ExceptionsThrownTest extends AbstractTestCase{
	private static final String SAY_HELLO_USE_CASE = "Say Hello Use Case";
	private static final String EXCEPTION_THROWING_USE_CASE = "Exception Throwing Use Case";
	
	private static final String ALTERNATIVE_FLOW_STEP = "Alternative Flow Step";
	private static final String BASIC_FLOW_STEP = "Basic Flow Step";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void shouldThrowExceptionIfAfterStepNotExistsInSameUseCase() {
		String unknownStepName = "Unknown Step";
		
		thrown.expect(NoSuchElementInUseCaseException.class);
		thrown.expectMessage(SAY_HELLO_USE_CASE);
		thrown.expectMessage(unknownStepName);
		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow().after(unknownStepName);
	}
	
	
	@Test
	public void shouldThrowExceptionIfAfterStepNotExistsInOtherUseCase() {
		String unknwonStepName = "Unknown Step";
		
		thrown.expect(NoSuchElementInUseCaseException.class);
		thrown.expectMessage(SAY_HELLO_USE_CASE);
		thrown.expectMessage(unknwonStepName);
		
		useCaseModel
			.newUseCase(SAY_HELLO_USE_CASE).basicFlow()
				.newStep(BASIC_FLOW_STEP);
		
		useCaseModel
			.newUseCase("Another Use Case").basicFlow()
				.after(unknwonStepName, SAY_HELLO_USE_CASE);
	}
	
	@Test
	public void shouldThrowExceptionIfAfterStepHasNonExistingUseCase() {
		String unknownUseCaseName = "Unknown Use Case";
		 
		thrown.expect(NoSuchElementInModelException.class);
		thrown.expectMessage(unknownUseCaseName);
		
		useCaseModel
			.newUseCase(SAY_HELLO_USE_CASE).basicFlow()
				.newStep(BASIC_FLOW_STEP);
		
		useCaseModel
			.newUseCase("Another Use Case").basicFlow()
				.after(BASIC_FLOW_STEP, unknownUseCaseName);
	}
	
	
	@Test
	public void shouldThrowExceptionIfContinueAfterNotExists() {
		String unknownStepName = "Unknown Step";
		
		thrown.expect(NoSuchElementInUseCaseException.class);
		thrown.expectMessage(SAY_HELLO_USE_CASE);
		thrown.expectMessage(unknownStepName);
		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow().continueAfter(unknownStepName);
	}
	
	@Test
	public void shouldThrowExceptionIfActorIsCreatedTwice() {
		String name = "Duplicate Actor";
		
		thrown.expect(ElementAlreadyInModelException.class);
		thrown.expectMessage(name);
		
		useCaseModel.newActor(name);
		useCaseModel.newActor(name);
	}
	
	@Test
	public void shouldThrowExceptionIfUseCaseIsCreatedTwice() {
		String duplicateUseCaseName = "Duplicate Use Case";
		
		thrown.expect(ElementAlreadyInModelException.class);
		thrown.expectMessage(duplicateUseCaseName);
		
		useCaseModel.newUseCase(duplicateUseCaseName);
		useCaseModel.newUseCase(duplicateUseCaseName);
	}
	
	@Test
	public void shouldThrowExceptionIfFlowIsCreatedTwice() {
		String duplicateFlowName = "Duplicate Flow";
		
		thrown.expect(ElementAlreadyInModelException.class);
		thrown.expectMessage(duplicateFlowName);
		
		useCaseModel.newUseCase(EXCEPTION_THROWING_USE_CASE)
			.newFlow(duplicateFlowName);
			
		useCaseModel.findUseCase(EXCEPTION_THROWING_USE_CASE).get()
			.newFlow(duplicateFlowName);
	}
	
	@Test
	public void shouldThrowExceptionIfStepIsCreatedTwice() {
		String duplicateStepName = "Duplicate Step";
		
		thrown.expect(ElementAlreadyInModelException.class);
		thrown.expectMessage(duplicateStepName);
		
		useCaseModel.newUseCase(EXCEPTION_THROWING_USE_CASE)
			.basicFlow()
				.newStep(duplicateStepName).system(displayConstantText())			
				.newStep(duplicateStepName).system(displayConstantText());
	}
	
	@Test
	public void shouldThrowExceptionIfMoreThanOneStepIsEnabled() { 	 
		thrown.expect(MoreThanOneStepCouldReactException.class);
		thrown.expectMessage(BASIC_FLOW_STEP);
		thrown.expectMessage(ALTERNATIVE_FLOW_STEP);
		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow().when(run -> true)
				.newStep(BASIC_FLOW_STEP).system(displayConstantText())
			.newFlow("Alternative Flow: Enabled as well").when(run -> true)
				.newStep(ALTERNATIVE_FLOW_STEP).system(displayConstantText());
		
		useCaseRunner.run();
	}
	
	@Test
	public void shouldThrowExceptionIfActorPartIsNotSpecified() {
		String stepWithoutActor = "Step without actor";
		
		thrown.expect(MissingUseCaseStepPartException.class);
		thrown.expectMessage(stepWithoutActor);
		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(stepWithoutActor);
			
		useCaseRunner.run();
		useCaseRunner.reactTo(enterTextEvent());
		
		assertEquals(0, getRunStepNames().size());
	}
	
	@Test
	public void shouldThrowExceptionIfSystemPartIsNotSpecified() {
		String stepWithoutSystemReaction = "Step without system";
		
		thrown.expect(MissingUseCaseStepPartException.class);
		thrown.expectMessage(stepWithoutSystemReaction);
		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(stepWithoutSystemReaction)
					.actor(customer).handle(EnterTextEvent.class);
			
		useCaseRunner.runAs(customer);
		useCaseRunner.reactTo(enterTextEvent());
		
		assertEquals(0, getRunStepNames().size());
	}
}
