package org.requirementsascode;

import static org.hamcrest.core.Is.isA;

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
import org.requirementsascode.exception.UnhandledException;

public class ExceptionsThrownTest extends AbstractTestCase{		
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup() {
		setupWith(new TestUseCaseRunner());
	}
	
	@Test
	public void throwsExceptionIfAtStepNotExistsInSameUseCase() {		
		thrown.expect(NoSuchElementInUseCase.class);
		thrown.expectMessage(USE_CASE);
		thrown.expectMessage(CUSTOMER_ENTERS_TEXT);
		
		useCaseModel.useCase(USE_CASE)
			.basicFlow().at(CUSTOMER_ENTERS_TEXT);
	}
	
	@Test
	public void throwsExceptionIfAfterStepNotExistsInSameUseCase() {		
		thrown.expect(NoSuchElementInUseCase.class);
		thrown.expectMessage(USE_CASE);
		thrown.expectMessage(CUSTOMER_ENTERS_TEXT);
		
		useCaseModel.useCase(USE_CASE)
			.basicFlow().after(CUSTOMER_ENTERS_TEXT);
	}
	
	@Test
	public void throwsExceptionIfAfterStepNotExistsInOtherUseCase() {		
		thrown.expect(NoSuchElementInUseCase.class);
		thrown.expectMessage(USE_CASE);
		thrown.expectMessage(CUSTOMER_ENTERS_DIFFERENT_TEXT);
		
		useCaseModel
			.useCase(USE_CASE).basicFlow()
				.step(CUSTOMER_ENTERS_TEXT);
		
		useCaseModel
			.useCase(ANOTHER_USE_CASE).basicFlow()
				.after(CUSTOMER_ENTERS_DIFFERENT_TEXT, USE_CASE);
	}
	
	@Test
	public void throwsExceptionIfAfterStepHasNonExistingUseCase() {
		String unknownUseCaseName = "Unknown Use Case";
		 
		thrown.expect(NoSuchElementInModel.class);
		thrown.expectMessage(unknownUseCaseName);
		
		useCaseModel
			.useCase(USE_CASE).basicFlow()
				.step(CUSTOMER_ENTERS_TEXT);
		
		useCaseModel
			.useCase("Another Use Case").basicFlow()
				.after(CUSTOMER_ENTERS_TEXT, unknownUseCaseName);
	}
	
	
	@Test
	public void throwsExceptionIfContinueAfterNotExists() {		
		thrown.expect(NoSuchElementInUseCase.class);
		thrown.expectMessage(USE_CASE);
		thrown.expectMessage(CONTINUE_1);
		
		useCaseModel.useCase(USE_CASE)
			.basicFlow().step("S1").continueAfter(CONTINUE_1);
	}
	
	@Test
	public void throwsExceptionIfActorIsCreatedTwice() {		
		thrown.expect(ElementAlreadyInModel.class);
		thrown.expectMessage(CUSTOMER);
		
		useCaseModel.actor(CUSTOMER);
		useCaseModel.actor(CUSTOMER);
	}
	
	@Test
	public void throwsExceptionIfUseCaseIsCreatedTwice() {		
		thrown.expect(ElementAlreadyInModel.class);
		thrown.expectMessage(USE_CASE);
		
		useCaseModel.useCase(USE_CASE);
		useCaseModel.useCase(USE_CASE);
	}
	
	@Test
	public void throwsExceptionIfFlowIsCreatedTwice() {		
		thrown.expect(ElementAlreadyInModel.class);
		thrown.expectMessage(ALTERNATIVE_FLOW);
		
		useCaseModel.useCase(USE_CASE)
			.flow(ALTERNATIVE_FLOW);
			
		useCaseModel.findUseCase(USE_CASE).get()
			.flow(ALTERNATIVE_FLOW);
	}
	
	@Test
	public void throwsExceptionIfStepIsCreatedTwice() {		
		thrown.expect(ElementAlreadyInModel.class);
		thrown.expectMessage(CUSTOMER_ENTERS_TEXT);
		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).system(displayConstantText())			
				.step(CUSTOMER_ENTERS_TEXT).system(displayConstantText());
	}
	
	@Test
	public void throwsExceptionIfMoreThanOneStepCouldReact() { 	 
		thrown.expect(MoreThanOneStepCanReact.class);
		thrown.expectMessage(CUSTOMER_ENTERS_TEXT);
		thrown.expectMessage(CUSTOMER_ENTERS_DIFFERENT_TEXT);
		
		useCaseModel.useCase(USE_CASE)
			.basicFlow().when(run -> true)
				.step(CUSTOMER_ENTERS_TEXT).system(displayConstantText())
			.flow(ALTERNATIVE_FLOW).when(run -> true)
				.step(CUSTOMER_ENTERS_DIFFERENT_TEXT).system(displayConstantText());
		
		useCaseRunner.run();
	}
	
	@Test
	public void throwsExceptionIfActorPartIsNotSpecified() {		
		thrown.expect(MissingUseCaseStepPart.class);
		thrown.expectMessage(CUSTOMER_ENTERS_TEXT);
		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT);
			
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());		
	}
	
	@Test
	public void throwsExceptionIfSystemPartIsNotSpecified() {		
		thrown.expect(MissingUseCaseStepPart.class);
		thrown.expectMessage(CUSTOMER_ENTERS_TEXT);
		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(customer).user(EnterText.class);
			
		useCaseRunner.runAs(customer);
		useCaseRunner.reactTo(enterText());		
	}
	
	@Test
	public void throwsUncaughtExceptionIfExceptionIsNotHandled() {		
		thrown.expect(UnhandledException.class);
		thrown.expectCause(isA(IllegalStateException.class));
		
		useCaseModel.useCase(USE_CASE)
		.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT)
				.system(() -> {throw new IllegalStateException();});
		
		useCaseRunner.run();
	}
}
