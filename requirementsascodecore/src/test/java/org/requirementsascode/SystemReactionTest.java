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
	
	private int timesDisplayed;
		
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
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());
		
		useCaseRunner.run();
		
		assertEquals(SYSTEM_DISPLAYS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRight() {
		useCaseModel
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).as(customer).system(displayConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(secondActor).system(displayConstantText());
		
		useCaseRunner.runAs(customer);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyTwice() {
		useCaseModel
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());
		
		useCaseRunner.run();
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_DISPLAYS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactIfRunIsNotCalled() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.reactTo(enterText());
		
		assertEquals("", runStepNames());
	}
	
	@Test
	public void doesNotReactToAlreadyRunStep() { 		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void cannotReactIfRunIsNotCalled() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText());
				
		boolean canReact = useCaseRunner.canReactTo(enterText().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void oneStepCannotReactIfEventIsWrong() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.run();
		
		boolean canReact = useCaseRunner.canReactTo(enterNumber().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void oneStepCanReactIfEventIsRight() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.run();
		
		boolean canReact = useCaseRunner.canReactTo(enterText().getClass());
		assertTrue(canReact);
		
		Set<UseCaseStep> stepsThatCanReact = useCaseRunner.stepsThatCanReactTo(enterText().getClass());
		assertEquals(1, stepsThatCanReact.size());
		assertEquals(CUSTOMER_ENTERS_TEXT, stepsThatCanReact.iterator().next().name().toString());
	}
	
	@Test
	public void moreThanOneStepCanReact() { 	 
		useCaseModel.useCase(USE_CASE)
			.basicFlow().when(run -> true)
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.flow("Alternative Flow: Could react as well").when(run -> true)
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		
		boolean canReact = useCaseRunner.canReactTo(enterText().getClass());
		assertTrue(canReact);
		
		Set<UseCaseStep> stepsThatCanReact = useCaseRunner.stepsThatCanReactTo(enterText().getClass());
		assertEquals(2, stepsThatCanReact.size());
	}
	
	@Test
	public void oneStepReacts() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText());
				
		useCaseRunner.run();
		Optional<UseCaseStep> latestStepRun = useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT, latestStepRun.get().name());
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfSameType() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfDifferentType() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER +";", runStepNames());
	}
	
	@Test
	public void raisesEventAfterFirstStep() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).handle(EnterText.class).system(displayEnteredText()).raise(raiseEnterNumber())
				.step(CUSTOMER_ENTERS_NUMBER).handle(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER +";", runStepNames());
	}
	
	@Test
	public void raisesEventForSpecificActor() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(rightActor).raise(() -> enterText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(rightActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactOnlyWhenActorIsRight() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(secondActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtFirstPosition() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(rightActor, secondActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtSecondPosition() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenRunningWithDifferentActors() { 		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(customer).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER)
					.as(secondActor).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.runAs(customer);
		useCaseRunner.reactTo(enterText());
		
		useCaseRunner.runAs(secondActor);
		useCaseRunner.reactTo(enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithTruePredicateReacts() { 		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())			
			.flow("Alternative Flow: Skipped").when(r -> false)
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";", runStepNames());
	}
	
	@Test
	public void stepThasHasTruePredicateReactsEvenIfOtherStepWouldBePerformedBySystem() { 		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())			
			.flow("Alternative Flow: Skipped").when(r -> false)
				.step(THIS_STEP_SHOULD_BE_SKIPPED).system(() -> {System.out.println("You should not see this!");});
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorReacts() { 		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(secondActor).user(EnterText.class).system(throwRuntimeException());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorInDifferentFlowReacts() { 		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(secondActor).user(EnterText.class).system(throwRuntimeException())			
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(rightActor).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.runAs(rightActor);
		Optional<UseCaseStep> lastStepRun = useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT_AGAIN, lastStepRun.get().name());
	}
	
	@Test
	public void stepWithWrongActorInDifferentFlowDoesNotReact() { 		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
					.as(rightActor).user(EnterText.class).system(displayEnteredText())			
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
				.step(THIS_STEP_SHOULD_BE_SKIPPED)
					.as(secondActor).user(EnterText.class).system(throwRuntimeException());
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText());
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStepName());
	}
	
	@Test
	public void doesNotReactIfStepHasRightActorButFalsePredicate() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
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
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
				.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToSecondStepAlternativeWhen() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(r -> CUSTOMER_ENTERS_TEXT.equals(latestStepName()))
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterAlternativeText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotReenterAlternativeFlowEvenIfItHasTruePredicate() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
				.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(r -> true)
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner
			.reactTo(enterAlternativeText(), enterNumber(), enterAlternativeText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	 
	@Test
	public void reactsToAlternativeAfterFirstStep() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).at(THIS_STEP_SHOULD_BE_SKIPPED)
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterAlternativeText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToAlternativeAtFirstStep() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
				.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).at(THIS_STEP_SHOULD_BE_SKIPPED)
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseRunner.run();
		Optional<UseCaseStep> latestStep = useCaseRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStep.get().name());
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void startsOneOfTwoParallelUseCasesByDifferentEvent() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseModel.useCase(ANOTHER_USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void startsTwoUseCasesSequentially() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText());
		
		useCaseModel.useCase(ANOTHER_USE_CASE)
			.basicFlow().after(CUSTOMER_ENTERS_TEXT, USE_CASE)
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactWhileConditionNotFulfilled() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
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
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.user(EnterText.class).system(displayEnteredText())
						.reactWhile(r -> true)
				.step(CUSTOMER_ENTERS_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber())
			.flow("Interrupt before customer enters text again").at(CUSTOMER_ENTERS_TEXT_AGAIN)
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
					.user(EnterText.class).system(displayEnteredText());					
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactWhileConditionIsFulfilledButInterruptedAfter() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.user(EnterText.class).system(displayEnteredText())
						.reactWhile(r -> true)
				.step(CUSTOMER_ENTERS_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber())
			.flow("Interrupt after customer enters text again").at(CUSTOMER_ENTERS_NUMBER)
				.step(CUSTOMER_ENTERS_NUMBER_AGAIN)
					.user(EnterNumber.class).system(displayEnteredNumber());
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +
			CUSTOMER_ENTERS_NUMBER_AGAIN +";", runStepNames());
	}
	
	@Test
	public void reactToStepOnce() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.reactWhile(r -> true)
				.step(CUSTOMER_ENTERS_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber());
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepTwice() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.user(EnterText.class).system(displayEnteredText())
						.reactWhile(r -> true)
				.step(CUSTOMER_ENTERS_NUMBER)
					.user(EnterNumber.class).system(displayEnteredNumber());
				
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepThreeTimes() {		
		timesDisplayed = 0;
		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
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
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	private Consumer<EnterText> displayEnteredTextAndIncrementCounter(){
		return enteredText -> {
			displayEnteredText().accept(enteredText);
			timesDisplayed++;
		};
	}
	
	@Test
	public void continueAtThirdStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT_AGAIN)
				.step(CONTINUE).continueAt(CUSTOMER_ENTERS_NUMBER);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAtCalledFromFirstStepOfAlternativeFlowWithUserEvent() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT_AGAIN)
				.step(CONTINUE).user(EnterNumber.class).continueAt(CUSTOMER_ENTERS_NUMBER);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber(), enterNumber());
				 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
		
	}
	
	@Test
	public void continueAtCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT)
				.step(CONTINUE).as(rightActor).continueAt(CUSTOMER_ENTERS_TEXT_AGAIN);
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAtNotCalledWhenActorIsWrong() {	
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT)
				.step(CONTINUE).as(rightActor).continueAt(CUSTOMER_ENTERS_TEXT_AGAIN);
		
		useCaseRunner.runAs(secondActor);
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAtCalledFromSecondStepOfAlternativeFlow() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT_AGAIN)
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CONTINUE).continueAt(CUSTOMER_ENTERS_NUMBER);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			 CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAtCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CONTINUE).continueAt(CUSTOMER_ENTERS_NUMBER)		
			.flow(ALTERNATIVE_FLOW_2).at(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsNotAvailable())
				.step("Customer enters alternative number").user(EnterNumber.class).system(displayEnteredNumber())
				.step(CONTINUE_2).continueAt(CUSTOMER_ENTERS_NUMBER);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterSecondStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT_AGAIN)
				.step(CONTINUE).continueAfter(CUSTOMER_ENTERS_TEXT_AGAIN);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterCalledFromFirstStepOfAlternativeFlowWithUserEvent() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW)
				.at(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
					.step(CONTINUE).user(EnterNumber.class).continueAfter(CUSTOMER_ENTERS_TEXT_AGAIN);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterNumber(), enterNumber());
				 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
		
	}
	
	@Test
	public void continueAfterCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT)
				.step(CONTINUE).as(rightActor).continueAfter(CUSTOMER_ENTERS_TEXT);
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterNotCalledWhenActorIsWrong() {	
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT)
				.step(CONTINUE).as(rightActor).continueAfter(CUSTOMER_ENTERS_TEXT);
		
		useCaseRunner.runAs(secondActor);
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterCalledFromSecondStepOfAlternativeFlow() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT_AGAIN)
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CONTINUE).continueAfter(CUSTOMER_ENTERS_TEXT_AGAIN);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			 CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CONTINUE).continueAfter(CUSTOMER_ENTERS_TEXT_AGAIN)		
			.flow(ALTERNATIVE_FLOW_2).at(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsNotAvailable())
				.step("Customer enters alternative number").user(EnterNumber.class).system(displayEnteredNumber())
				.step(CONTINUE_2).continueAfter(CUSTOMER_ENTERS_TEXT_AGAIN);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueExclusivelyAtFirstStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT)
				.step(CONTINUE).continueExclusivelyAt(CUSTOMER_ENTERS_TEXT);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterText());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT + ";" +
				CUSTOMER_ENTERS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void continueExclusivelyAtCalledFromFirstStepOfAlternativeFlowWithUserEvent() {		
		useCaseModel.useCase(USE_CASE)
		.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
			.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
		.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT)
			.step(CONTINUE).user(EnterNumber.class).continueExclusivelyAt(CUSTOMER_ENTERS_TEXT);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterNumber(), enterText(), enterText());
				 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT + ";" +
				CUSTOMER_ENTERS_TEXT_AGAIN + ";", runStepNames());	
	}
	
	@Test
	public void continueExclusivelyAtCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).as(rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT)
				.step(CONTINUE).as(rightActor).continueExclusivelyAt(CUSTOMER_ENTERS_TEXT);
		
		useCaseRunner.runAs(rightActor);
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
				 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueExclusivelyAtNotCalledWhenActorIsWrong() {	
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, rightActor).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, rightActor).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT)
				.step(CONTINUE).as(rightActor).continueExclusivelyAt(CUSTOMER_ENTERS_TEXT);
		
		useCaseRunner.runAs(secondActor);
		useCaseRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueExclusivelyAtCalledFromSecondStepOfAlternativeFlow() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT_AGAIN)
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CONTINUE).continueExclusivelyAt(CUSTOMER_ENTERS_TEXT_AGAIN);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
				
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueExclusivelyAtCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
			.flow(ALTERNATIVE_FLOW).at(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CONTINUE).continueExclusivelyAt(CUSTOMER_ENTERS_TEXT_AGAIN)		
			.flow(ALTERNATIVE_FLOW_2).at(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_NUMBER_AGAIN).user(EnterNumber.class).system(displayEnteredNumber())
				.step(CONTINUE_2).continueExclusivelyAt(CUSTOMER_ENTERS_TEXT_AGAIN);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
}
