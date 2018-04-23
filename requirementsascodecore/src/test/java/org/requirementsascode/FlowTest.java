package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.flowposition.Anytime;

public class FlowTest extends AbstractTestCase{
	private Actor secondActor;
	private int timesDisplayed;
		
	@Before
	public void setup() {
		setupWith(new TestModelRunner());
		this.secondActor = modelBuilder.actor("Second Actor");
	}
	
	@Test
	public void printsTextAutonomously() {
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.build();
		
		modelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRightInFirstStep() {
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).as(customer).system(displaysConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(secondActor).system(displaysConstantText())
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRightInSecondStep() {
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(customer).system(displaysConstantText())
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_DISPLAYS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyTwice() {
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText())
			.build();
		
		modelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_DISPLAYS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactToEventIfNotRunning() { 	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.stop();
		modelRunner.reactTo(entersText());
		
		assertEquals("", runStepNames());
	}
	
	@Test
	public void doesNotReactToAlreadyRunStep() { 	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void cantReactIfNotRunning() {	
		modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
						
		boolean canReact = modelRunner.canReactTo(entersText().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void cantReactIfEventIsWrong() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		modelRunner.run(useCaseModel);
		
		boolean canReact = modelRunner.canReactTo(entersNumber().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void oneStepCanReactIfEventIsRight() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		modelRunner.run(useCaseModel);
		
		boolean canReact = modelRunner.canReactTo(entersText().getClass());
		assertTrue(canReact);
		
		Set<Step> stepsThatCanReact = modelRunner.getStepsThatCanReactTo(entersText().getClass());
		assertEquals(1, stepsThatCanReact.size());
		assertEquals(CUSTOMER_ENTERS_TEXT, stepsThatCanReact.iterator().next().getName().toString());
	}
	
	@Test
	public void moreThanOneStepCanReact() { 
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow().anytime()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
				.flow("Alternative Flow: Could react as well").anytime()
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(useCaseModel);
		
		boolean canReact = modelRunner.canReactTo(entersText().getClass());
		assertTrue(canReact);
		
		Set<Step> stepsThatCanReact = modelRunner.getStepsThatCanReactTo(entersText().getClass());
		assertEquals(2, stepsThatCanReact.size());
	}
	
	@Test
	public void oneStepInFlowReacts() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		modelRunner.run(useCaseModel);
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT, latestStepRun.get().getName());
	}
	

	
	@Test
	public void twoSequentialStepsReactToEventsOfDifferentType() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfSameType() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenOneIsUserStepAndOtherIsSystemStep() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class)
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactOnlyWhenActorIsRight() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenActorIsChanged() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText());
		
		modelRunner.as(secondActor).run(useCaseModel);
		modelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtFirstPosition() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(customer, secondActor).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtSecondPosition() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
				.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenRunningWithDifferentActors() { 		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER)
						.as(secondActor).user(EntersNumber.class).system(displaysEnteredNumber())
				.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText());
		
		modelRunner.as(secondActor);
		modelRunner.reactTo(entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithTrueConditionReacts() { 	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())			
				.flow("Alternative Flow: Skipped").when(r -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";", runStepNames());
	}
	
	@Test
	public void stepThasHasTrueConditionReactsEvenIfOtherStepWouldBePerformedBySystem() { 		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())			
				.flow("Alternative Flow: Skipped").when(r -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED).system(r -> {System.out.println("You should not see this!");})
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorReacts() { 		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EntersText.class).system(throwsRuntimeException())
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorInDifferentFlowReacts() { 
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(secondActor).user(EntersText.class).system(throwsRuntimeException())			
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(customer).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		Optional<Step> lastStepRun = modelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT_AGAIN, lastStepRun.get().getName());
	}
	
	@Test
	public void stepWithWrongActorInDifferentFlowDoesNotReact() { 
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())			
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
					.step(THIS_STEP_SHOULD_BE_SKIPPED)
						.as(secondActor).user(EntersText.class).system(throwsRuntimeException())
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStepName());
	}
	
	@Test
	public void doesNotReactIfStepHasRightActorButFalseCondition() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(secondActor).user(EntersText.class).system(displaysEnteredText())				
				.flow(ALTERNATIVE_FLOW).when(r -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		modelRunner.as(customer).run(useCaseModel);
		Optional<Step> lastStepRun = modelRunner.reactTo(entersText());
		
		assertFalse(lastStepRun.isPresent());
	}
	
	@Test
	public void reactsToFirstStepAlternativeWhen() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()					
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EntersText.class).system(throwsRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToSecondStepAlternativeWhen() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).when(r -> CUSTOMER_ENTERS_TEXT.equals(latestStepName()))
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersAlternativeText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotReenterAlternativeFlowEvenIfItHasTrueCondition() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).anytime()
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner
			.reactTo(entersAlternativeText(), entersNumber(), entersAlternativeText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	 
	@Test
	public void reactsToAlternativeAfterFirstStep() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).insteadOf(THIS_STEP_SHOULD_BE_SKIPPED)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersAlternativeText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToAlternativeAtFirstStep() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).insteadOf(THIS_STEP_SHOULD_BE_SKIPPED)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(useCaseModel);
		Optional<Step> latestStep = modelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStep.get().getName());
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void startsOneOfTwoParallelUseCasesByDifferentEvent() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.basicFlow()
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void startsTwoUseCasesSequentially() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.basicFlow().when(textIsAvailable())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactWhileConditionNotFulfilled() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(r -> false)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();

		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersNumber());
		
		assertEquals("", runStepNames());
	}
	
	@Test
	public void interruptsReactWhileBefore() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(new Anytime())
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
			.build();
				
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void interruptsReactWhileAfter() {
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(new Anytime())
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_NUMBER)
					.step(CUSTOMER_ENTERS_NUMBER_AGAIN)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +
			CUSTOMER_ENTERS_NUMBER_AGAIN +";", runStepNames());
	}
	
	@Test
	public void reactToStepOnce() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(new Anytime())
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepTwice() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(new Anytime())
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepThreeTimes() {		
		timesDisplayed = 0;
		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredTextAndIncrementCounter())
							.reactWhile(r -> timesDisplayed < 3)
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		// Create way to many events to see if the repeat stops after three events
		modelRunner.run(useCaseModel);
		modelRunner
			.reactTo(entersText(), entersText(), entersText(),
					entersText(), entersText(), entersText(),
					entersText(), entersText(), entersText(),
					entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	private Consumer<EntersText> displaysEnteredTextAndIncrementCounter(){
		return enteredText -> {
			displaysEnteredText().accept(enteredText);
			timesDisplayed++;
		};
	}
	
	@Test
	public void continuesAtThirdStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CONTINUE).continuesAt(CUSTOMER_ENTERS_NUMBER)
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAtCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersNumber());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAtNotCalledWhenActorIsWrong() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()				
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.as(secondActor).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAtCalledFromSecondStepOfAlternativeFlow() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAt(CUSTOMER_ENTERS_NUMBER)
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			 CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAtCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAt(CUSTOMER_ENTERS_NUMBER)		
				.flow(ALTERNATIVE_FLOW_2).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsNotAvailable())
					.step("Customer enters alternative number").user(EntersNumber.class).system(displaysEnteredNumber())
					.step(CONTINUE_2).continuesAt(CUSTOMER_ENTERS_NUMBER)
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterSecondStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CONTINUE).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAfter(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersNumber());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterNotCalledWhenActorIsWrong() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAfter(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.as(secondActor).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterCalledFromSecondStepOfAlternativeFlow() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			 CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)		
				.flow(ALTERNATIVE_FLOW_2).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsNotAvailable())
					.step("Customer enters alternative number").user(EntersNumber.class).system(displaysEnteredNumber())
					.step(CONTINUE_2).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtFirstStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT + ";" +
				CUSTOMER_ENTERS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.as(customer).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText(), entersNumber());
				 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtNotCalledWhenActorIsWrong() {	
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.as(secondActor).run(useCaseModel);
		modelRunner.reactTo(entersText(), entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtCalledFromSecondStepOfAlternativeFlow() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()				
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
				
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		Model useCaseModel = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT_AGAIN)		
				.flow(ALTERNATIVE_FLOW_2).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsNotAvailable())
					.step(CUSTOMER_ENTERS_NUMBER_AGAIN).user(EntersNumber.class).system(displaysEnteredNumber())
					.step(CONTINUE_2).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
}
