package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExceptionHandlingTest extends AbstractTestCase{

	@Before
	public void setup() {
		setupWithRecordingModelRunner();
	}
	
	@Test
	public void doesNotHandleExceptionIfNoExceptionOccurs() {
		Model model = 
			modelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
					.step(SYSTEM_HANDLES_EXCEPTION).on(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();

		modelRunner.run(model);

		assertRecordedStepNames(SYSTEM_DISPLAYS_TEXT);
	}
	
	@Test
	public void doesNotHandleExceptionIfSystemReactionDoesNotThrowException() {	
		Model model = 
			modelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())		
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
					.step(SYSTEM_HANDLES_EXCEPTION).on(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();
		
		modelRunner.run(model);
		
		assertRecordedStepNames(SYSTEM_DISPLAYS_TEXT);
	}
	
	@Test
	public void handlesExceptionAfterSpecificStep() {	
		Model model = 
			modelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_THROWS_EXCEPTION).system(throwsArrayIndexOutOfBoundsException())		
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_THROWS_EXCEPTION)
					.step(SYSTEM_HANDLES_EXCEPTION).on(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();
		
		modelRunner.run(model);
		
		assertEquals(SYSTEM_HANDLES_EXCEPTION, latestStepName());
	}
	
	@Test
	public void handlesExceptionAtAnyTime() {
		Model model = 
			modelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())		
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
					.step(SYSTEM_THROWS_EXCEPTION).system(throwsArrayIndexOutOfBoundsException())
				.flow(ALTERNATIVE_FLOW_2).anytime()
					.step(SYSTEM_HANDLES_EXCEPTION).on(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();
		
		modelRunner.run(model);
		
		assertRecordedStepNames(SYSTEM_DISPLAYS_TEXT, SYSTEM_THROWS_EXCEPTION, SYSTEM_HANDLES_EXCEPTION);
	}
}
