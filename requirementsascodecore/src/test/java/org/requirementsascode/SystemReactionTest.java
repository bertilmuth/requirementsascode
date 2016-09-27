package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.event.EnteredNumber;
import org.requirementsascode.event.EnteredText;

public class SystemReactionTest extends AbstractTestCase{
	private Actor rightActor;
	private Actor secondActor;
	private Actor actorWithDisabledStep;
	
	private static final String SAY_HELLO_USE_CASE = "Say Hello Use Case";
	private static final String ALTERNATIVE_FLOW = "Alternative Flow";
	private static final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	private static final String SYSTEM_DISPLAYS_TEXT_AGAIN = "System displays text again";

	private static final String CUSTOMER_ENTERS_SOME_TEXT = "Customer enters some text";
	private static final String CUSTOMER_ENTERS_SOME_TEXT_AGAIN = "Customer enters some text again";
	private static final String CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT = "Customer enters some different text";
	private static final String CUSTOMER_ENTERS_NUMBER = "Customer enters number";
	
	private static final String THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL = "This step should be skipped as well";
	private static final String THIS_STEP_SHOULD_BE_SKIPPED = "This step should be skipped";
	
	private static final String REPEAT_STEP_POSTFIX = " (#REPEAT)";
	
	@Before
	public void setup() {
		super.setup();
		this.rightActor = useCaseModel.newActor("Right Actor");
		this.secondActor = useCaseModel.newActor("Second Actor");
		this.actorWithDisabledStep = useCaseModel.newActor("Actor With Disabled Step");
	}
	
	@Test
	public void shouldPrintText() {
		useCaseModel
			.newUseCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.newStep(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText());
		
		useCaseModelRun.as(customer);
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT), getRunStepNames());
	}
	@Test
	public void shouldPrintTextTwice() {
		useCaseModel
			.newUseCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.newStep(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
					.newStep(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());
		
		useCaseModelRun.as(customer);
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT, SYSTEM_DISPLAYS_TEXT_AGAIN), getRunStepNames());
	}
	
	@Test
	public void shouldReactToOneStep() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
				
		UseCaseStep latestStepRun = 
				useCaseModelRun.as(customer).reactTo(enteredTextEvent());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT, latestStepRun.getName());
	}
	
	@Test
	public void shouldNotRepeatStepWhenConditionNotFulfilled() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT)
					.actor(customer, EnteredText.class).system(displaysEnteredText())
						.repeatWhile(r -> false);
				
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredTextEvent());
		
		assertEquals(1, getRunStepNames().size());
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT, getLatestStepName());
	}
	
	@Test
	public void shouldRepeatStepOnce() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT)
					.actor(customer, EnteredText.class).system(displaysEnteredText())
						.repeatWhile(r -> true);
				
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredTextEvent());
		
		
		String repeatStepName = CUSTOMER_ENTERS_SOME_TEXT + REPEAT_STEP_POSTFIX;
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, repeatStepName), getRunStepNames());
	}
	
	@Test
	public void shouldRepeatStepTwice() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT)
					.actor(customer, EnteredText.class).system(displaysEnteredText())
						.repeatWhile(r -> true);
				
		useCaseModelRun.as(customer)
			.reactTo(enteredTextEvent(), enteredTextEvent(), enteredTextEvent());
		
		
		String repeatStepName = CUSTOMER_ENTERS_SOME_TEXT + REPEAT_STEP_POSTFIX;
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, repeatStepName, repeatStepName), 
			getRunStepNames());
	}
	
	@Test
	public void shouldRepeatStepThreeTimes() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT)
					.actor(customer, EnteredText.class).system(displaysEnteredText())
						.repeatWhile(r -> getRunStepNames().size() < 3);
				
		// Create way to many events to see if the repeat stops after three events
		useCaseModelRun.as(customer)
			.reactTo(enteredTextEvent(), enteredTextEvent(), enteredTextEvent(),
					enteredTextEvent(), enteredTextEvent(), enteredTextEvent(),
					enteredTextEvent(), enteredTextEvent(), enteredTextEvent());
		
		
		String repeatStepName = CUSTOMER_ENTERS_SOME_TEXT + REPEAT_STEP_POSTFIX;
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, repeatStepName, repeatStepName), 
			getRunStepNames());
	}
	
	@Test
	public void shouldRepeatStepThreeTimesAndThenReactToOtherEvent() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT)
					.actor(customer, EnteredText.class).system(displaysEnteredText())
						.repeatWhile(r -> getRunStepNames().size() < 3)
				.newStep(CUSTOMER_ENTERS_NUMBER).actor(customer, EnteredNumber.class).system(displaysEnteredNumber());
				
		// Create way to many events to see if the repeat stops after three events
		useCaseModelRun.as(customer)
			.reactTo(enteredTextEvent(), enteredTextEvent(), enteredTextEvent(),
					enteredTextEvent(), enteredTextEvent(), enteredTextEvent(),
					enteredTextEvent(), enteredTextEvent(), enteredTextEvent(),
					enteredNumberEvent());
		
		
		String repeatStepName = CUSTOMER_ENTERS_SOME_TEXT + REPEAT_STEP_POSTFIX;
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, repeatStepName, repeatStepName, CUSTOMER_ENTERS_NUMBER), 
			getRunStepNames());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsBasedOnSameType() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).actor(customer, EnteredText.class).system(displaysEnteredText());
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT_AGAIN), getRunStepNames());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsBasedOnDifferentType() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_NUMBER).actor(customer, EnteredNumber.class).system(displaysEnteredNumber());
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredNumberEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsOnlyForThoseStepsWhereActorIsRight() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(rightActor, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).actor(secondActor, EnteredText.class).system(displaysEnteredText());
		
		useCaseModelRun.as(rightActor).reactTo(enteredTextEvent(), enteredTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsWhenRunningWithDifferentActors() { 		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_NUMBER).actor(secondActor, EnteredNumber.class).system(displaysEnteredNumber());
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent());
		useCaseModelRun.as(secondActor).reactTo(enteredNumberEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldContinueWithBasicFlowCalledFromFirstStepOfAlternativeFlow() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_NUMBER).actor(customer, EnteredNumber.class).system(displaysEnteredNumber());
		
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow("Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailablePredicate())
				.continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredTextEvent(), enteredNumberEvent());
		 
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldContinueWithBasicFlowCalledFromSecondStepOfAlternativeFlow() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_NUMBER).actor(customer, EnteredNumber.class).system(displaysEnteredNumber());
		
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow("Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailablePredicate())
				.newStep(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredDifferentTextEvent(), enteredTextEvent(), enteredNumberEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT,
			CUSTOMER_ENTERS_SOME_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldContinueWithBasicFlowCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_NUMBER).actor(customer, EnteredNumber.class).system(displaysEnteredNumber());
		
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow("AF1: Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailablePredicate())
				.newStep(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow("AF2: Alternative Flow that has a disabled condition").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsNotAvailablePredicate())
				.newStep("Customer enters alterative number").actor(customer, EnteredNumber.class).system(displaysEnteredNumber())
				.continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredDifferentTextEvent(), enteredTextEvent(), enteredNumberEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT,
				CUSTOMER_ENTERS_SOME_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldNotReactToAlreadyRunStep() { 		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactOnlyToEnabledStep() { 		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
			
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow("Alternative Flow: Skipped").when(r -> false)
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED).actor(customer, EnteredText.class).system(throwRuntimeException());
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactToEnabledStepEvenIfDisabledStepWouldBePerformedBySystem() { 		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
			
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow("Alternative Flow: Skipped").when(r -> false)
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED).system(() -> {System.out.println("You should not see this!");});
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactOnlyToEnabledStepWithRightActorInSameFlowAtFirstStep() { 		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(rightActor, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).actor(secondActor, EnteredText.class).system(throwRuntimeException());
		
		useCaseModelRun.as(rightActor).reactTo(enteredTextEvent(), enteredTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactOnlyToEnabledStepWithRightActorInDifferentFlow() { 		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(secondActor, EnteredText.class).system(throwRuntimeException());
			
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow(ALTERNATIVE_FLOW).when(textIsNotAvailablePredicate())
				.newStep(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).actor(rightActor, EnteredText.class).system(displaysEnteredText());
		
		UseCaseStep lastStepRun = useCaseModelRun.as(rightActor).reactTo(enteredTextEvent());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT_AGAIN, lastStepRun.getName());
	}
	
	@Test
	public void shouldNotReactToEnabledStepWithWrongActorInDifferentFlow() { 		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(rightActor, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).actor(rightActor, EnteredText.class).system(displaysEnteredText());
			
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow(ALTERNATIVE_FLOW).after(CUSTOMER_ENTERS_SOME_TEXT)
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED).actor(secondActor, EnteredText.class).system(throwRuntimeException());
		
		useCaseModelRun.as(rightActor).reactTo(enteredTextEvent(), enteredTextEvent());
		assertEquals(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT, getLatestStepName());
	}
	
	@Test
	public void shouldNotReactToDisabledStepWithSpecificActor() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
				
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow(ALTERNATIVE_FLOW).when(r -> false)
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED).actor(actorWithDisabledStep, EnteredText.class).system(displaysEnteredText());
				
		UseCaseStep lastStepRun = 
			useCaseModelRun.as(actorWithDisabledStep).reactTo(enteredTextEvent());
		
		assertNull(lastStepRun);
	}
	
	@Test
	public void shouldReactToFirstStepAlternativeWhenTextIsNotAvailable() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED).actor(customer, EnteredText.class).system(throwRuntimeException())
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).actor(customer, EnteredText.class).system(throwRuntimeException());
		
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow(ALTERNATIVE_FLOW).when(textIsNotAvailablePredicate())
				.newStep(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactToSecondStepAlternativeWhenThereIsOneSystemReactionBefore() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED).actor(customer, EnteredText.class).system(throwRuntimeException());
		
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow(ALTERNATIVE_FLOW).when(r -> CUSTOMER_ENTERS_SOME_TEXT.equals(getLatestStepName()))
				.newStep(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredDifferentTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldNotReenterAlternativeFlowEvenIfConditionsIsFulfilled() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED).actor(customer, EnteredText.class).system(throwRuntimeException())
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).actor(customer, EnteredText.class).system(throwRuntimeException());
		
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow(ALTERNATIVE_FLOW).when(r -> true)
				.newStep(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(CUSTOMER_ENTERS_NUMBER).actor(customer, EnteredNumber.class).system(displaysEnteredNumber());
		
		useCaseModelRun.as(customer)
			.reactTo(enteredDifferentTextEvent(), enteredNumberEvent(), enteredDifferentTextEvent(), enteredNumberEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldReactToAlternativeAfterFirstStep() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED).actor(customer, EnteredText.class).system(throwRuntimeException());
		
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow(ALTERNATIVE_FLOW).after(CUSTOMER_ENTERS_SOME_TEXT)
				.newStep(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
		
		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredDifferentTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactToAlternativeAtFirstStep() {		
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED).actor(customer, EnteredText.class).system(throwRuntimeException())
				.newStep(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).actor(customer, EnteredText.class).system(throwRuntimeException());
		
		useCaseModel.getUseCase(SAY_HELLO_USE_CASE)
			.newFlow(ALTERNATIVE_FLOW).atFirst()
				.newStep(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
		
		UseCaseStep latestStep = 
			useCaseModelRun.as(customer).reactTo(enteredTextEvent());
		
		assertEquals(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT, latestStep.getName());
	}
	
	@Test
	public void shouldResetUseCaseFromBasicFlow() {
		useCaseModel
			.newUseCase(SAY_HELLO_USE_CASE)
				.basicFlow().when(r -> getRunStepNames().size()<2)
					.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
					.reset();

		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredTextEvent(), enteredTextEvent());

		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldResetUseCaseFromAlternativeFlow() {
		useCaseModel
			.newUseCase(SAY_HELLO_USE_CASE)
				.basicFlow().when(r -> getRunStepNames().size()<4)
					.newStep(CUSTOMER_ENTERS_SOME_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText());
		
		useCaseModel
			.getUseCase(SAY_HELLO_USE_CASE) 
				.newFlow(ALTERNATIVE_FLOW).after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailablePredicate())
					.newStep(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).actor(customer, EnteredText.class).system(displaysEnteredText())
					.reset();

		useCaseModelRun.as(customer).reactTo(enteredTextEvent(), enteredDifferentTextEvent(), enteredTextEvent(), enteredDifferentTextEvent());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT, CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT), getRunStepNames());
	}
}
