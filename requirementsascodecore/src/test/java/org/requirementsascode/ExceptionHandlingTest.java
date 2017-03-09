package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExceptionHandlingTest extends AbstractTestCase{
	private static final String EXCEPTION_THROWING_USE_CASE = "Exception Throwing Use Case";
	private static final String ALTERNATIVE_FLOW = "Alternative Flow";
	private static final String EXCEPTION_HANDLING_FLOW = "Exception Handling Flow";
	private static final String SAY_HELLO = "Say Hello";
	private static final String SYSTEM_THROWS_EXCEPTION = "System throws Exception";
	private static final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	private static final String SYSTEM_HANDLES_EXCEPTION = "System handles exception";

	@Before
	public void setup() {
		setupWith(new TestUseCaseRunner());
	}
	
	@Test
	public void doesNotHandleExceptionIfNoExceptionOccurs() {		
		useCaseModel.useCase(SAY_HELLO)
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
			.flow("Exception Handling Flow - Should not be entered as no exception occurs")
				.after(SYSTEM_DISPLAYS_TEXT).when(r -> true)
					.step(SYSTEM_HANDLES_EXCEPTION)
						.handle(ArrayIndexOutOfBoundsException.class).system(e -> e.printStackTrace());

		useCaseRunner.run();

		assertEquals(SYSTEM_DISPLAYS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotHandleExceptionIfSystemReactionDoesNotThrowException() {		
		useCaseModel.useCase(SAY_HELLO)
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())		
			.flow("Exception Handling Flow - Should not be entered as no exception occurs")
				.after(SYSTEM_DISPLAYS_TEXT).when(r -> true)
					.step(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(e -> e.printStackTrace());
		
		useCaseRunner.run();
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void handlesExceptionAfterSpecificStep() {				
		useCaseModel.useCase(EXCEPTION_THROWING_USE_CASE)
			.basicFlow()
				.step(SYSTEM_THROWS_EXCEPTION).system(throwArrayIndexOutOfBoundsException())		
			.flow(EXCEPTION_HANDLING_FLOW).after(SYSTEM_THROWS_EXCEPTION)
				.step(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(e -> e.printStackTrace());
		
		useCaseRunner.run();
		
		assertEquals(SYSTEM_HANDLES_EXCEPTION, latestStepName());
	}
	
	@Test
	public void handlesExceptionAtAnyTime() {			
		useCaseModel.useCase(EXCEPTION_THROWING_USE_CASE)
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())		
			.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
				.step(SYSTEM_THROWS_EXCEPTION).system(throwArrayIndexOutOfBoundsException())
			.flow(EXCEPTION_HANDLING_FLOW).when(r -> true)
				.step(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(e -> e.printStackTrace());
		
		useCaseRunner.run();
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_THROWS_EXCEPTION + ";" + SYSTEM_HANDLES_EXCEPTION +";", runStepNames());
	}
}
