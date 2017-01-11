package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.event.EnterNumber;
import org.requirementsascode.event.EnterText;

public class SystemReactionTest extends AbstractTestCase{
	private Actor rightActor;
	private Actor secondActor;
	private Actor actorWithDisabledStep;
	
	private static final String SAY_HELLO_USE_CASE = "Say Hello Use Case";
	private static final String ANOTHER_USE_CASE = "Another Use Case";

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
		setupWith(new UseCaseRunner());
		this.rightActor = useCaseModel.actor("Right Actor");
		this.secondActor = useCaseModel.actor("Second Actor");
		this.actorWithDisabledStep = useCaseModel.actor("Actor With Disabled Step");
	}
	
	@Test
	public void shouldPrintTextAutonomously() {
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());
		
		useCaseRunner.run();
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldPrintTextAutonomouslyOnlyIfActorIsRight() {
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).as(customer).system(displayConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(secondActor).system(displayConstantText());
		
		useCaseRunner.runAs(customer);
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldPrintTextAutonomouslyTwice() {
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());
		
		useCaseRunner.run();
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT, SYSTEM_DISPLAYS_TEXT_AGAIN), getRunStepNames());
	}
	
	@Test
	public void shouldReactToOneStep() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.run();
		Optional<UseCaseStep> latestStepRun = useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT, latestStepRun.get().name());
	}
	
	@Test
	public void shouldNotReactToStepIfRunIsNotCalled() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.reactTo(enterText());
		
		assertEquals(0, getRunStepNames().size());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsBasedOnSameType() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT_AGAIN), getRunStepNames());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsBasedOnDifferentType() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldRaiseAdditionalEventAfterFirstStep() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText()).raise(raiseEnterNumber())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldRaiseAdditionalEventBySpecificActor() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).raise(() -> enterText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(rightActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT_AGAIN), getRunStepNames());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsOnlyForThoseStepsWhereActorIsRight() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(secondActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsWhenSeveralActorsContainRightActorAtFirstPosition() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(rightActor, secondActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT_AGAIN), getRunStepNames());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsWhenSeveralActorsContainRightActorAtSecondPosition() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT_AGAIN), getRunStepNames());
	}
	
	@Test
	public void shouldReactToTwoSequentialStepsWhenRunningWithDifferentActors() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(customer).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER)
					.as(secondActor).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.runAs(customer);
		useCaseRunner.reactTo(enterText());
		
		useCaseRunner.runAs(secondActor);
		useCaseRunner.reactTo(enterNumber());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldNotReactToAlreadyRunStep() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactOnlyToStepThasHasTruePredicate() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())			
			.flow("Alternative Flow: Skipped").when(r -> false)
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactToStepThasHasTruePredicateEvenIfOtherStepWouldBePerformedBySystem() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())			
			.flow("Alternative Flow: Skipped").when(r -> false)
				.step(THIS_STEP_SHOULD_BE_SKIPPED).system(() -> {System.out.println("You should not see this!");});
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactOnlyToStepWithRightActorInSameFlowAtFirstStep() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(secondActor).user(EnterText.class).system(throwRuntimeException());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactOnlyToStepWithRightActorInDifferentFlow() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(secondActor).user(EnterText.class).system(throwRuntimeException())			
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(rightActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		Optional<UseCaseStep> lastStepRun = useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT_AGAIN, lastStepRun.get().name());
	}
	
	@Test
	public void shouldNotReactToStepWithWrongActorInDifferentFlow() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())			
			.flow(ALTERNATIVE_FLOW).after(CUSTOMER_ENTERS_SOME_TEXT)
				.step(THIS_STEP_SHOULD_BE_SKIPPED)
					.as(secondActor).user(EnterText.class).system(throwRuntimeException());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		assertEquals(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT, getLatestStepName());
	}
	
	@Test
	public void shouldNotReactToStepThasHasFalsePredicateAndSpecificActor() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(customer).user(EnterText.class).system(displayEnteredText())				
			.flow(ALTERNATIVE_FLOW).when(r -> false)
				.step(THIS_STEP_SHOULD_BE_SKIPPED)
					.as(actorWithDisabledStep).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.runAs(actorWithDisabledStep);
		Optional<UseCaseStep> lastStepRun = useCaseRunner.reactTo(enterText());
		
		assertFalse(lastStepRun.isPresent());
	}
	
	@Test
	public void shouldReactToFirstStepAlternativeWhen() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
				.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactToSecondStepAlternativeWhen() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(r -> CUSTOMER_ENTERS_SOME_TEXT.equals(getLatestStepName()))
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldNotReenterAlternativeFlowEvenIfItHasTruePredicate() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
				.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(r -> true)
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner
			.reactTo(enterDifferentText(), enterNumber(), enterDifferentText(), enterNumber());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	 
	@Test
	public void shouldReactToAlternativeAfterFirstStep() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).after(CUSTOMER_ENTERS_SOME_TEXT)
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldReactToAlternativeAtFirstStep() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
				.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).atStart()
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		Optional<UseCaseStep> latestStep = useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT, latestStep.get().name());
	}
	
	@Test
	public void shouldStartOneOfTwoParallelUseCasesByDifferentEvent() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseModel.useCase(ANOTHER_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterNumber());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldStartTwoUseCasesSequentially() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseModel.useCase(ANOTHER_USE_CASE)
			.basicFlow().after(CUSTOMER_ENTERS_SOME_TEXT, SAY_HELLO_USE_CASE)
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldRestartCalledFromBasicFlow() {
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow().when(r -> getRunStepNames().size()<2)
					.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
					.restart();

		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterText());

		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldRestartCalledFromFirstStepOfAlternativeFlow() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that continues with Basic Flow")
				.after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
					.restart();
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterText());
		 
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldRestartCalledFromSecondStepOfAlternativeFlow() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText())
				.restart();
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText(), enterText(), enterDifferentText());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT,
				CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldRestartCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
		.basicFlow()
			.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
			.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
			.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
		.flow("AF1: Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
			.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText())
			.restart()	
		.flow("AF2: Alternative Flow that has a disabled condition").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsNotAvailable())
			.step("Customer enters alternative number").user(EnterNumber.class).system(displayEnteredNumber())
			.restart();
	
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText(), enterText(), enterDifferentText());
	
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT,
			CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT), getRunStepNames());
	}
	
	@Test
	public void shouldNotRepeatStepWhenConditionNotFulfilled() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.repeatWhile(r -> false);
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(1, getRunStepNames().size());
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT, getLatestStepName());
	}
	
	@Test
	public void shouldRepeatStepOnce() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.repeatWhile(r -> true);
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		
		String repeatStepName = CUSTOMER_ENTERS_SOME_TEXT + REPEAT_STEP_POSTFIX;
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, repeatStepName), getRunStepNames());
	}
	
	@Test
	public void shouldRepeatStepTwice() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.repeatWhile(r -> true);
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterText());
		
		
		String repeatStepName = CUSTOMER_ENTERS_SOME_TEXT + REPEAT_STEP_POSTFIX;
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, repeatStepName, repeatStepName), 
			getRunStepNames());
	}
	
	@Test
	public void shouldRepeatStepThreeTimes() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.repeatWhile(r -> getRunStepNames().size() < 3);
				
		// Create way to many events to see if the repeat stops after three events
		useCaseRunner.run();
		useCaseRunner
			.reactTo(enterText(), enterText(), enterText(),
					enterText(), enterText(), enterText(),
					enterText(), enterText(), enterText());
		
		
		String repeatStepName = CUSTOMER_ENTERS_SOME_TEXT + REPEAT_STEP_POSTFIX;
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, repeatStepName, repeatStepName), 
			getRunStepNames());
	}
	
	@Test
	public void shouldRepeatStepThreeTimesAndThenReactToOtherEvent() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.repeatWhile(r -> getRunStepNames().size() < 3)
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
				
		// Create way to many events to see if the repeat stops after three events
		useCaseRunner.run();
		useCaseRunner
			.reactTo(enterText(), enterText(), enterText(),
					enterText(), enterText(), enterText(),
					enterText(), enterText(), enterText(),
					enterNumber());
		
		
		String repeatStepName = CUSTOMER_ENTERS_SOME_TEXT + REPEAT_STEP_POSTFIX;
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, repeatStepName, repeatStepName, CUSTOMER_ENTERS_NUMBER), 
			getRunStepNames());
	}
	
	@Test
	public void shouldContinueAfterCalledFromFirstStepOfAlternativeFlowWithoutActor() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that continues with Basic Flow")
				.after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
					.continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldContinueAfterCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that skips first step").atStart()
				.step("Skip first step").as(rightActor).continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldContinueAfterNotCalledWhenActorIsWrong() {	
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that skips first step").atStart()
				.step("Skip first step").as(rightActor).continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.runAs(secondActor);
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldContinueAfterFirstStepCalledFromSecondStepOfAlternativeFlow() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText())
				.continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText(), enterText(), enterNumber());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT,
			CUSTOMER_ENTERS_SOME_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
	
	@Test
	public void shouldContinueAfterFirstStepCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("AF1: Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText())
				.continueAfter(CUSTOMER_ENTERS_SOME_TEXT)		
			.flow("AF2: Alternative Flow that has false predicate").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsNotAvailable())
				.step("Customer enters alternative number").user(EnterNumber.class).system(displayEnteredNumber())
				.continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText(), enterText(), enterNumber());
		
		assertEquals(Arrays.asList(CUSTOMER_ENTERS_SOME_TEXT, CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT,
				CUSTOMER_ENTERS_SOME_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER), getRunStepNames());
	}
}
