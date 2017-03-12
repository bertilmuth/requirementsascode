package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

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
	private static final String CUSTOMER_ENTERS_SOME_DIFFERENT_NUMBER = "Customer enters some different number";
		
	private static final String THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL = "This step should be skipped as well";
	private static final String THIS_STEP_SHOULD_BE_SKIPPED = "This step should be skipped";
	
	private static final String RESTART_1 = "Restart 1";
	private static final String RESTART_2 = "Restart 2";
	private static final String CONTINUE_1 = "Continue 1";
	private static final String CONTINUE_2 = "Continue 2";
	
	int timesDisplayed;
		
	@Before
	public void setup() {
		setupWith(new TestUseCaseRunner());
		this.rightActor = useCaseModel.actor("Right Actor");
		this.secondActor = useCaseModel.actor("Second Actor");
		this.actorWithDisabledStep = useCaseModel.actor("Actor With Disabled Step");
	}
	
	@Test
	public void useCaseRunnerIsNotRunningAtFirst(){
		assertFalse(useCaseRunner.isRunning());
	}
	
	@Test
	public void useCaseRunnerIsRunningAfterRunCall(){
		useCaseRunner.run();
		assertTrue(useCaseRunner.isRunning());
	}
	
	@Test
	public void useCaseRunnerIsNotRunningWhenBeingStoppedBeforeRunCall(){
		useCaseRunner.stop();
		assertFalse(useCaseRunner.isRunning());
	}
	
	@Test
	public void useCaseRunnerIsNotRunningWhenBeingStoppedAfterRunCall(){
		useCaseRunner.run();
		useCaseRunner.stop();
		assertFalse(useCaseRunner.isRunning());
	}
	
	@Test
	public void printsTextAutonomously() {
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());
		
		useCaseRunner.run();
		
		assertEquals(SYSTEM_DISPLAYS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRight() {
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).as(customer).system(displayConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(secondActor).system(displayConstantText());
		
		useCaseRunner.runAs(customer);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyTwice() {
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());
		
		useCaseRunner.run();
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_DISPLAYS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactIfRunIsNotCalled() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.reactTo(enterText());
		
		assertEquals("", runStepNames());
	}
	
	@Test
	public void doesNotReactToAlreadyRunStep() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";", runStepNames());
	}
	
	@Test
	public void cannotReactIfRunIsNotCalled() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
				
		boolean canReact = useCaseRunner.canReactTo(enterText().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void oneStepCannotReactIfEventIsWrong() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.run();
		
		boolean canReact = useCaseRunner.canReactTo(enterNumber().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void oneStepCanReactIfEventIsRight() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.run();
		
		boolean canReact = useCaseRunner.canReactTo(enterText().getClass());
		assertTrue(canReact);
		
		Set<UseCaseStep> stepsThatCanReact = useCaseRunner.stepsThatCanReactTo(enterText().getClass());
		assertEquals(1, stepsThatCanReact.size());
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT, stepsThatCanReact.iterator().next().name().toString());
	}
	
	@Test
	public void moreThanOneStepCanReact() { 	 
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow().when(run -> true)
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
			.flow("Alternative Flow: Could react as well").when(run -> true)
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		
		boolean canReact = useCaseRunner.canReactTo(enterText().getClass());
		assertTrue(canReact);
		
		Set<UseCaseStep> stepsThatCanReact = useCaseRunner.stepsThatCanReactTo(enterText().getClass());
		assertEquals(2, stepsThatCanReact.size());
	}
	
	@Test
	public void oneStepReacts() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.run();
		Optional<UseCaseStep> latestStepRun = useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT, latestStepRun.get().name());
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfSameType() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";" + CUSTOMER_ENTERS_SOME_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfDifferentType() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";" + CUSTOMER_ENTERS_NUMBER +";", runStepNames());
	}
	
	@Test
	public void raisesEventAfterFirstStep() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).handle(EnterText.class).system(displayEnteredText()).raise(raiseEnterNumber())
				.step(CUSTOMER_ENTERS_NUMBER).handle(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";" + CUSTOMER_ENTERS_NUMBER +";", runStepNames());
	}
	
	@Test
	public void raisesEventForSpecificActor() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).raise(() -> enterText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(rightActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactOnlyWhenActorIsRight() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(secondActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtFirstPosition() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(rightActor, secondActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";" + CUSTOMER_ENTERS_SOME_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtSecondPosition() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";" + CUSTOMER_ENTERS_SOME_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenRunningWithDifferentActors() { 		
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
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithTruePredicateReacts() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())			
			.flow("Alternative Flow: Skipped").when(r -> false)
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";", runStepNames());
	}
	
	@Test
	public void stepThasHasTruePredicateReactsEvenIfOtherStepWouldBePerformedBySystem() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())			
			.flow("Alternative Flow: Skipped").when(r -> false)
				.step(THIS_STEP_SHOULD_BE_SKIPPED).system(() -> {System.out.println("You should not see this!");});
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorReacts() { 		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.as(secondActor).user(EnterText.class).system(throwRuntimeException());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorInDifferentFlowReacts() { 		
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
	public void stepWithWrongActorInDifferentFlowDoesNotReact() { 		
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
		assertEquals(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT, latestStepName());
	}
	
	@Test
	public void doesNotReactIfStepHasRightActorButFalsePredicate() {		
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
	public void reactsToFirstStepAlternativeWhen() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
				.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToSecondStepAlternativeWhen() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(r -> CUSTOMER_ENTERS_SOME_TEXT.equals(latestStepName()))
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotReenterAlternativeFlowEvenIfItHasTruePredicate() {		
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
		
		assertEquals(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	 
	@Test
	public void reactsToAlternativeAfterFirstStep() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).after(CUSTOMER_ENTERS_SOME_TEXT)
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToAlternativeAtFirstStep() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
				.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).atStart()
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		Optional<UseCaseStep> latestStep = useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT, latestStep.get().name());
		assertEquals(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";", runStepNames());
	}
	
	@Test
	public void startsOneOfTwoParallelUseCasesByDifferentEvent() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseModel.useCase(ANOTHER_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void startsTwoUseCasesSequentially() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseModel.useCase(ANOTHER_USE_CASE)
			.basicFlow().after(CUSTOMER_ENTERS_SOME_TEXT, SAY_HELLO_USE_CASE)
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void restartsInBasicFlow() {
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(RESTART_1).restart();

		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());

		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + RESTART_1 + ";" + 
				CUSTOMER_ENTERS_SOME_TEXT + ";" + RESTART_1 + ";", runStepNames());
	}
	
	@Test
	public void restartsInBasicFlowWithUserEvent() {
		useCaseModel
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(RESTART_1).user(EnterNumber.class).restart();

		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber(), enterText());

		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + RESTART_1 + ";" + 
				CUSTOMER_ENTERS_SOME_TEXT + ";", runStepNames());
	}
	
	@Test
	public void restartsInFirstStepOfAlternativeFlow() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that continues with Basic Flow")
				.after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
					.step(RESTART_1).restart();
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterText());
		 
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + RESTART_1 + ";" + 
				CUSTOMER_ENTERS_SOME_TEXT + ";" + RESTART_1 + ";" + 
				CUSTOMER_ENTERS_SOME_TEXT + ";" + RESTART_1 + ";", runStepNames());
	}
	
	@Test
	public void restartsInSecondStepOfAlternativeFlow() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(RESTART_1).restart();
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText(), enterText(), enterDifferentText());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";" + RESTART_1 + ";" + 
				CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";" + RESTART_1 + ";", runStepNames());
	}
	
	@Test
	public void restartsInMultipleMutuallyExclusiveAlternativeFlows() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
		.basicFlow()
			.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
			.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
			.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
		.flow("AF1: Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
			.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText())
			.step(RESTART_1).restart()	
		.flow("AF2: Alternative Flow that has a disabled condition").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsNotAvailable())
			.step("Customer enters alternative number").user(EnterNumber.class).system(displayEnteredNumber())
			.step(RESTART_2).restart();
	
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText(), enterText(), enterDifferentText());
	
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT +";" + CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";" + RESTART_1 + ";" + 
			CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";" + RESTART_1 + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactWhileConditionNotFulfilled() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.reactWhile(r -> false)
				.step(CUSTOMER_ENTERS_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber());

		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		
		assertEquals("", runStepNames());
	}
	
	@Test
	public void doesNotReactWhileConditionIsFulfilledButInterruptedBefore() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.user(EnterText.class).system(displayEnteredText())
						.reactWhile(r -> true)
				.step(CUSTOMER_ENTERS_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber())
			.flow("Interrupt before customer enters text again").after(CUSTOMER_ENTERS_SOME_TEXT)
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT)
					.user(EnterText.class).system(displayEnteredText());					
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactWhileConditionIsFulfilledButInterruptedAfter() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
					.user(EnterText.class).system(displayEnteredText())
						.reactWhile(r -> true)
				.step(CUSTOMER_ENTERS_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber())
			.flow("Interrupt after customer enters text again").after(CUSTOMER_ENTERS_SOME_TEXT_AGAIN)
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber());
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_TEXT_AGAIN + ";" +
			CUSTOMER_ENTERS_SOME_DIFFERENT_NUMBER +";", runStepNames());
	}
	
	@Test
	public void reactToStepOnce() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.reactWhile(r -> true)
				.step(CUSTOMER_ENTERS_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber());
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepTwice() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.reactWhile(r -> true)
				.step(CUSTOMER_ENTERS_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber());
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_TEXT + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepThreeTimes() {		
		timesDisplayed = 0;
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT)
					.user(EnterText.class).system(displayEnteredTextAndIncrementCounter())
						.reactWhile(r -> timesDisplayed < 3)
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
				
		// Create way to many events to see if the repeat stops after three events
		useCaseRunner.run();
		useCaseRunner
			.reactTo(enterText(), enterText(), enterText(),
					enterText(), enterText(), enterText(),
					enterText(), enterText(), enterText(),
					enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_TEXT + ";" +
			CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	private Consumer<EnterText> displayEnteredTextAndIncrementCounter(){
		return enteredText -> {
			displayEnteredText().accept(enteredText);
			timesDisplayed++;
		};
	}
	
	@Test
	public void continueAfterCalledFromFirstStepOfAlternativeFlowWithoutActor() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that continues with Basic Flow")
				.after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
					.step(CONTINUE_1).continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CONTINUE_1 + ";" +
			CUSTOMER_ENTERS_SOME_TEXT_AGAIN + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterCalledFromFirstStepOfAlternativeFlowWithUserEvent() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that continues with Basic Flow")
				.after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
					.step(CONTINUE_1).user(EnterNumber.class).continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber(), enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CONTINUE_1 + ";" +
			CUSTOMER_ENTERS_SOME_TEXT_AGAIN + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}

	@Test
	public void continueAfterCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that skips first step").atStart()
				.step(CONTINUE_1).as(rightActor).continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(CONTINUE_1 + ";" + CUSTOMER_ENTERS_SOME_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void  continueAfterNotCalledWhenActorIsWrong() {	
		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that skips first step").atStart()
				.step(CONTINUE_1).as(rightActor).continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.runAs(secondActor);
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterFirstStepCalledFromSecondStepOfAlternativeFlow() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CONTINUE_1).continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";" + CONTINUE_1 + ";" +
			CUSTOMER_ENTERS_SOME_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterFirstStepCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		useCaseModel.useCase(SAY_HELLO_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_SOME_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_SOME_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow("AF1: Alternative Flow that continues with Basic Flow").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsAvailable())
				.step(CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CONTINUE_1).continueAfter(CUSTOMER_ENTERS_SOME_TEXT)		
			.flow("AF2: Alternative Flow that has false predicate").after(CUSTOMER_ENTERS_SOME_TEXT).when(textIsNotAvailable())
				.step("Customer enters alternative number").user(EnterNumber.class).system(displayEnteredNumber())
				.step(CONTINUE_2).continueAfter(CUSTOMER_ENTERS_SOME_TEXT);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterDifferentText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_SOME_TEXT + ";" + CUSTOMER_ENTERS_SOME_DIFFERENT_TEXT + ";" + CONTINUE_1 + ";" + 
				CUSTOMER_ENTERS_SOME_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
}
