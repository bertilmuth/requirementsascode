package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExceptionHandlingTest extends AbstractTestCase{

	@Before
	public void setup() {
		setupWith(new TestModelRunner());
	}
	
	@Test
	public void doesNotHandleExceptionIfNoExceptionOccurs() {
		Model useCaseModel = 
			modelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
					.step(SYSTEM_HANDLES_EXCEPTION).handles(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();

		modelRunner.run(useCaseModel);

		assertEquals(SYSTEM_DISPLAYS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotHandleExceptionIfSystemReactionDoesNotThrowException() {	
		Model useCaseModel = 
			modelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())		
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
					.step(SYSTEM_HANDLES_EXCEPTION).handles(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();
		
		modelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void handlesExceptionAfterSpecificStep() {	
		Model useCaseModel = 
			modelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_THROWS_EXCEPTION).system(throwsArrayIndexOutOfBoundsException())		
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_THROWS_EXCEPTION)
					.step(SYSTEM_HANDLES_EXCEPTION).handles(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();
		
		modelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_HANDLES_EXCEPTION, latestStepName());
	}
	
	@Test
	public void handlesExceptionAtAnyTime() {
		Model useCaseModel = 
			modelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())		
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
					.step(SYSTEM_THROWS_EXCEPTION).system(throwsArrayIndexOutOfBoundsException())
				.flow(ALTERNATIVE_FLOW_2).anytime()
					.step(SYSTEM_HANDLES_EXCEPTION).handles(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();
		
		modelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_THROWS_EXCEPTION + ";" + SYSTEM_HANDLES_EXCEPTION +";", runStepNames());
	}
}
