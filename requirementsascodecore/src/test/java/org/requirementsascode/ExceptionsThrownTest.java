package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.requirementsascode.event.EnterText;
import org.requirementsascode.exception.ElementAlreadyExistsException;
import org.requirementsascode.exception.MoreThanOneStepCouldReactException;
import org.requirementsascode.exception.NoSuchElementExistsInModelException;
import org.requirementsascode.exception.NoSuchElementExistsInUseCaseException;
import org.requirementsascode.exception.MissingUseCaseStepPartException;

public class ExceptionsThrownTest extends AbstractTestCase{
	private static final String SAY_HELLO_USE_CASE = "Say Hello Use Case";
	private static final String EXCEPTION_THROWING_USE_CASE = "Exception Throwing Use Case";
	
	private static final String ALTERNATIVE_FLOW_STEP = "Alternative Flow Step";
	private static final String BASIC_FLOW_STEP = "Basic Flow Step";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void shouldThrowExceptionIfActorNotExists() {
		String name = "Unknown Actor";
		
		thrown.expect(NoSuchElementExistsInModelException.class);
		thrown.expectMessage(name);
		
		useCaseModel.findActor(name);
	}
	
	@Test
	public void shouldThrowExceptionIfUseCaseNotExists() {
		String name = "Unknown Use Case";
		
		thrown.expect(NoSuchElementExistsInModelException.class);
		thrown.expectMessage(name);
		
		useCaseModel.findUseCase(name);
	}
	
	@Test
	public void shouldThrowExceptionIfAfterStepNotExistsInSameUseCase() {
		String name = "Unknown Step";
		
		thrown.expect(NoSuchElementExistsInUseCaseException.class);
		thrown.expectMessage(name);
		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow().after(name);
	}
	
	
	@Test
	public void shouldThrowExceptionIfAfterStepNotExistsInOtherUseCase() {
		String name = "Unknown Step";
		
		thrown.expect(NoSuchElementExistsInUseCaseException.class);
		thrown.expectMessage(name);
		
		useCaseModel
			.newUseCase(SAY_HELLO_USE_CASE).basicFlow()
				.newStep(BASIC_FLOW_STEP);
		
		useCaseModel
			.newUseCase("Another Use Case").basicFlow()
				.after("Unknown Step of other Use Case", SAY_HELLO_USE_CASE);
	}
	
	@Test
	public void shouldThrowExceptionIfContinueAfterNotExists() {
		String name = "Unknown Step";
		
		thrown.expect(NoSuchElementExistsInUseCaseException.class);
		thrown.expectMessage(name);
		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow().continueAfter(name);
	}
	
	@Test
	public void shouldThrowExceptionIfActorIsCreatedTwice() {
		String name = "Duplicate Actor";
		
		thrown.expect(ElementAlreadyExistsException.class);
		thrown.expectMessage(name);
		
		useCaseModel.newActor(name);
		useCaseModel.newActor(name);
	}
	
	@Test
	public void shouldThrowExceptionIfUseCaseIsCreatedTwice() {
		String name = "Duplicate Use Case";
		
		thrown.expect(ElementAlreadyExistsException.class);
		thrown.expectMessage(name);
		
		useCaseModel.newUseCase(name);
		useCaseModel.newUseCase(name);
	}
	
	@Test
	public void shouldThrowExceptionIfFlowIsCreatedTwice() {
		String name = "Duplicate Flow";
		
		thrown.expect(ElementAlreadyExistsException.class);
		thrown.expectMessage(name);
		
		useCaseModel.newUseCase(EXCEPTION_THROWING_USE_CASE)
			.newFlow(name);
			
		useCaseModel.findUseCase(EXCEPTION_THROWING_USE_CASE)
			.newFlow(name);
	}
	
	@Test
	public void shouldThrowExceptionIfStepIsCreatedTwice() {
		String name = "Duplicate Step";
		
		thrown.expect(ElementAlreadyExistsException.class);
		thrown.expectMessage(name);
		
		useCaseModel.newUseCase(EXCEPTION_THROWING_USE_CASE)
			.basicFlow()
				.newStep(name).system(displayConstantText())			
				.newStep(name).system(displayConstantText());
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
			
		useCaseRunner.run().reactTo(enterTextEvent());
		
		assertEquals(0, getRunStepNames().size());
	}
	
	@Test
	public void shouldThrowExceptionIfSystemPartIsNotSpecified() {
		String stepWithoutSystemReaction = "Step without system";
		
		thrown.expect(MissingUseCaseStepPartException.class);
		thrown.expectMessage(stepWithoutSystemReaction);
		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(stepWithoutSystemReaction).actor(customer, EnterText.class);
			
		useCaseRunner.run().as(customer).reactTo(enterTextEvent());
		
		assertEquals(0, getRunStepNames().size());
	}
}
