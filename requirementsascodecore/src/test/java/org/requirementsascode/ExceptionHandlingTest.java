package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class ExceptionHandlingTest extends AbstractTestCase{
	private static final String EXCEPTION_THROWING_USE_CASE = "Exception Throwing Use Case";
	private static final String ALTERNATIVE_FLOW = "Alternative Flow";
	private static final String EXCEPTION_HANDLING_FLOW = "Exception Handling Flow";
	private static final String SAY_HELLO = "Say Hello";
	private static final String SYSTEM_THROWS_EXCEPTION = "System throws Exception";
	private static final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	private static final String SYSTEM_HANDLES_EXCEPTION = "System handles exception";

	@Test
	public void shouldNotHandleExceptionIfUserInputDoesNotThrowException() {		
		useCaseModel.newUseCase(SAY_HELLO)
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.newFlow("Exception Handling Flow - Should not be entered as no exception occurs")
				.after(SYSTEM_DISPLAYS_TEXT).when(r -> true)
					.newStep(SYSTEM_HANDLES_EXCEPTION)
						.handle(ArrayIndexOutOfBoundsException.class).system(reactsToArrayIndexOutOfBoundsException());

		useCaseModelRun.as(customer);

		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldNotHandleExceptionIfSystemReactionDoesNotThrowException() {		
		useCaseModel.newUseCase(SAY_HELLO)
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())		
			.newFlow("Exception Handling Flow - Should not be entered as no exception occurs")
				.after(SYSTEM_DISPLAYS_TEXT).when(r -> true)
					.newStep(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(reactsToArrayIndexOutOfBoundsException());
		
		useCaseModelRun.as(customer);
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldHandleExceptionAfterSpecificStep() {				
		useCaseModel.newUseCase(EXCEPTION_THROWING_USE_CASE)
			.basicFlow()
				.newStep(SYSTEM_THROWS_EXCEPTION).system(throwArrayIndexOutOfBoundsException())		
			.newFlow(EXCEPTION_HANDLING_FLOW).after(SYSTEM_THROWS_EXCEPTION)
				.newStep(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(reactsToArrayIndexOutOfBoundsException());
		
		useCaseModelRun.as(customer);
		
		assertEquals(SYSTEM_HANDLES_EXCEPTION, useCaseModelRun.getLatestStep().getName());
	}
	
	@Test
	public void shouldHandleExceptionAtAnyTime() {			
		useCaseModel.newUseCase(EXCEPTION_THROWING_USE_CASE)
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())		
			.newFlow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
				.newStep(SYSTEM_THROWS_EXCEPTION).system(throwArrayIndexOutOfBoundsException())
			.newFlow(EXCEPTION_HANDLING_FLOW).when(r -> true)
				.newStep(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(reactsToArrayIndexOutOfBoundsException());
		
		useCaseModelRun.as(customer);
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT, SYSTEM_THROWS_EXCEPTION, SYSTEM_HANDLES_EXCEPTION), getRunStepNames());
	}
	
	@Test
	public void shouldHandleExceptionIfSystemReactionOfStepContinuedAfterThrowsException() {
		useCaseModel.newUseCase(EXCEPTION_THROWING_USE_CASE)
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
				.newStep(SYSTEM_THROWS_EXCEPTION).system(throwArrayIndexOutOfBoundsException())	
			.newFlow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
				.continueAfter(SYSTEM_DISPLAYS_TEXT)		
			.newFlow(EXCEPTION_HANDLING_FLOW).after(SYSTEM_THROWS_EXCEPTION)
				.newStep(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(reactsToArrayIndexOutOfBoundsException());
		
		useCaseModelRun.as(customer);
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT, SYSTEM_THROWS_EXCEPTION, SYSTEM_HANDLES_EXCEPTION), getRunStepNames());
	}
}
