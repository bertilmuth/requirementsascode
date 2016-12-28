package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.requirementsascode.event.EnterText;
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
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow().after(unknownStepName);
	}
	
	
	@Test
	public void shouldThrowExceptionIfAfterStepNotExistsInOtherUseCase() {
		String unknwonStepName = "Unknown Step";
		
		thrown.expect(NoSuchElementInUseCaseException.class);
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
	public void shouldThrowExceptionIfAfterStepHasNonExistingUseCase() {
		String unknownUseCaseName = "Unknown Use Case";
		 
		thrown.expect(NoSuchElementInModelException.class);
		thrown.expectMessage(unknownUseCaseName);
		
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE).basicFlow()
				.step(BASIC_FLOW_STEP);
		
		useCaseModel
			.useCase("Another Use Case").basicFlow()
				.after(BASIC_FLOW_STEP, unknownUseCaseName);
	}
	
	
	@Test
	public void shouldThrowExceptionIfContinueAfterNotExists() {
		String unknownStepName = "Unknown Step";
		
		thrown.expect(NoSuchElementInUseCaseException.class);
		thrown.expectMessage(SAY_HELLO_USE_CASE);
		thrown.expectMessage(unknownStepName);
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow().continueAfter(unknownStepName);
	}
	
	@Test
	public void shouldThrowExceptionIfActorIsCreatedTwice() {
		String name = "Duplicate Actor";
		
		thrown.expect(ElementAlreadyInModelException.class);
		thrown.expectMessage(name);
		
		useCaseModel.actor(name);
		useCaseModel.actor(name);
	}
	
	@Test
	public void shouldThrowExceptionIfUseCaseIsCreatedTwice() {
		String duplicateUseCaseName = "Duplicate Use Case";
		
		thrown.expect(ElementAlreadyInModelException.class);
		thrown.expectMessage(duplicateUseCaseName);
		
		useCaseModel.useCase(duplicateUseCaseName);
		useCaseModel.useCase(duplicateUseCaseName);
	}
	
	@Test
	public void shouldThrowExceptionIfFlowIsCreatedTwice() {
		String duplicateFlowName = "Duplicate Flow";
		
		thrown.expect(ElementAlreadyInModelException.class);
		thrown.expectMessage(duplicateFlowName);
		
		useCaseModel.useCase(EXCEPTION_THROWING_USE_CASE)
			.flow(duplicateFlowName);
			
		useCaseModel.findUseCase(EXCEPTION_THROWING_USE_CASE).get()
			.flow(duplicateFlowName);
	}
	
	@Test
	public void shouldThrowExceptionIfStepIsCreatedTwice() {
		String duplicateStepName = "Duplicate Step";
		
		thrown.expect(ElementAlreadyInModelException.class);
		thrown.expectMessage(duplicateStepName);
		
		useCaseModel.useCase(EXCEPTION_THROWING_USE_CASE)
			.basicFlow()
				.step(duplicateStepName).system(displayConstantText())			
				.step(duplicateStepName).system(displayConstantText());
	}
	
	@Test
	public void shouldThrowExceptionIfMoreThanOneStepCouldReact() { 	 
		thrown.expect(MoreThanOneStepCouldReactException.class);
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
	public void shouldThrowExceptionIfActorPartIsNotSpecified() {
		String stepWithoutActor = "Step without actor";
		
		thrown.expect(MissingUseCaseStepPartException.class);
		thrown.expectMessage(stepWithoutActor);
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(stepWithoutActor);
			
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());
		
		assertEquals(0, getRunStepNames().size());
	}
	
	@Test
	public void shouldThrowExceptionIfSystemPartIsNotSpecified() {
		String stepWithoutSystemReaction = "Step without system";
		
		thrown.expect(MissingUseCaseStepPartException.class);
		thrown.expectMessage(stepWithoutSystemReaction);
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(stepWithoutSystemReaction)
					.as(customer).user(EnterText.class);
			
		useCaseRunner.runAs(customer);
		useCaseRunner.reactTo(enterText());
		
		assertEquals(0, getRunStepNames().size());
	}
}
