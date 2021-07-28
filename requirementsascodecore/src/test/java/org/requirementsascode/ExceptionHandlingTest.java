package org.requirementsascode;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExceptionHandlingTest extends AbstractTestCase{

	@BeforeEach
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
   public void handlesExceptionAfterAnyOfSeveralSteps() {
     Model model = 
       modelBuilder.useCase(USE_CASE)
         .basicFlow()
           .step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
           .step(SYSTEM_THROWS_EXCEPTION).system(throwsArrayIndexOutOfBoundsException())
         .flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT, SYSTEM_THROWS_EXCEPTION)
           .step(SYSTEM_HANDLES_EXCEPTION).on(ArrayIndexOutOfBoundsException.class).system(e -> {}).build();

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
					.step(SYSTEM_HANDLES_EXCEPTION).on(Exception.class).system(e -> {})
			.build();
		
		modelRunner.run(model);
		
		assertRecordedStepNames(SYSTEM_DISPLAYS_TEXT, SYSTEM_THROWS_EXCEPTION, SYSTEM_HANDLES_EXCEPTION);
	}
}
