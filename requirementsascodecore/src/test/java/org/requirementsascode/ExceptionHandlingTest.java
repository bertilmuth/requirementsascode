package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExceptionHandlingTest extends AbstractTestCase{

	@Before
	public void setup() {
		setupWith(new TestUseCaseModelRunner());
	}
	
	@Test
	public void doesNotHandleExceptionIfNoExceptionOccurs() {
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
					.step(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();

		useCaseModelRunner.run(useCaseModel);

		assertEquals(SYSTEM_DISPLAYS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotHandleExceptionIfSystemReactionDoesNotThrowException() {	
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())		
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
					.step(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void handlesExceptionAfterSpecificStep() {	
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_THROWS_EXCEPTION).system(throwArrayIndexOutOfBoundsException())		
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_THROWS_EXCEPTION)
					.step(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_HANDLES_EXCEPTION, latestStepName());
	}
	
	@Test
	public void handlesExceptionAtAnyTime() {
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())		
				.flow(ALTERNATIVE_FLOW).after(SYSTEM_DISPLAYS_TEXT)
					.step(SYSTEM_THROWS_EXCEPTION).system(throwArrayIndexOutOfBoundsException())
				.flow(ALTERNATIVE_FLOW_2).when(r -> true)
					.step(SYSTEM_HANDLES_EXCEPTION).handle(ArrayIndexOutOfBoundsException.class).system(e -> {})
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_THROWS_EXCEPTION + ";" + SYSTEM_HANDLES_EXCEPTION +";", runStepNames());
	}
}
