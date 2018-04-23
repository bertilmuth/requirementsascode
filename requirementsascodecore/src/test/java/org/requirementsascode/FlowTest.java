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
		this.secondActor = useCaseModelBuilder.actor("Second Actor");
	}
	
	@Test
	public void printsTextAutonomously() {
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRightInFirstStep() {
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).as(customer).system(displaysConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(secondActor).system(displaysConstantText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRightInSecondStep() {
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(customer).system(displaysConstantText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_DISPLAYS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyTwice() {
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_DISPLAYS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactToEventIfNotRunning() { 	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.stop();
		useCaseModelRunner.reactTo(entersText());
		
		assertEquals("", runStepNames());
	}
	
	@Test
	public void doesNotReactToAlreadyRunStep() { 	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void cantReactIfNotRunning() {	
		useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
						
		boolean canReact = useCaseModelRunner.canReactTo(entersText().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void cantReactIfEventIsWrong() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		
		boolean canReact = useCaseModelRunner.canReactTo(entersNumber().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void oneStepCanReactIfEventIsRight() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		
		boolean canReact = useCaseModelRunner.canReactTo(entersText().getClass());
		assertTrue(canReact);
		
		Set<Step> stepsThatCanReact = useCaseModelRunner.getStepsThatCanReactTo(entersText().getClass());
		assertEquals(1, stepsThatCanReact.size());
		assertEquals(CUSTOMER_ENTERS_TEXT, stepsThatCanReact.iterator().next().getName().toString());
	}
	
	@Test
	public void moreThanOneStepCanReact() { 
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow().anytime()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
				.flow("Alternative Flow: Could react as well").anytime()
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		boolean canReact = useCaseModelRunner.canReactTo(entersText().getClass());
		assertTrue(canReact);
		
		Set<Step> stepsThatCanReact = useCaseModelRunner.getStepsThatCanReactTo(entersText().getClass());
		assertEquals(2, stepsThatCanReact.size());
	}
	
	@Test
	public void oneStepInFlowReacts() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		Optional<Step> latestStepRun = useCaseModelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT, latestStepRun.get().getName());
	}
	

	
	@Test
	public void twoSequentialStepsReactToEventsOfDifferentType() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfSameType() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenOneIsUserStepAndOtherIsSystemStep() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class)
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactOnlyWhenActorIsRight() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenActorIsChanged() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText());
		
		useCaseModelRunner.as(secondActor).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtFirstPosition() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(customer, secondActor).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtSecondPosition() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
				.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenRunningWithDifferentActors() { 		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER)
						.as(secondActor).user(EntersNumber.class).system(displaysEnteredNumber())
				.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText());
		
		useCaseModelRunner.as(secondActor);
		useCaseModelRunner.reactTo(entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithTrueConditionReacts() { 	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())			
				.flow("Alternative Flow: Skipped").when(r -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";", runStepNames());
	}
	
	@Test
	public void stepThasHasTrueConditionReactsEvenIfOtherStepWouldBePerformedBySystem() { 		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())			
				.flow("Alternative Flow: Skipped").when(r -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED).system(r -> {System.out.println("You should not see this!");})
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorReacts() { 		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EntersText.class).system(throwsRuntimeException())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorInDifferentFlowReacts() { 
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(secondActor).user(EntersText.class).system(throwsRuntimeException())			
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(customer).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		Optional<Step> lastStepRun = useCaseModelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT_AGAIN, lastStepRun.get().getName());
	}
	
	@Test
	public void stepWithWrongActorInDifferentFlowDoesNotReact() { 
		Model useCaseModel = useCaseModelBuilder
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
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStepName());
	}
	
	@Test
	public void doesNotReactIfStepHasRightActorButFalseCondition() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(secondActor).user(EntersText.class).system(displaysEnteredText())				
				.flow(ALTERNATIVE_FLOW).when(r -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		useCaseModelRunner.as(customer).run(useCaseModel);
		Optional<Step> lastStepRun = useCaseModelRunner.reactTo(entersText());
		
		assertFalse(lastStepRun.isPresent());
	}
	
	@Test
	public void reactsToFirstStepAlternativeWhen() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()					
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EntersText.class).system(throwsRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToSecondStepAlternativeWhen() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).when(r -> CUSTOMER_ENTERS_TEXT.equals(latestStepName()))
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersAlternativeText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotReenterAlternativeFlowEvenIfItHasTrueCondition() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).anytime()
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner
			.reactTo(entersAlternativeText(), entersNumber(), entersAlternativeText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	 
	@Test
	public void reactsToAlternativeAfterFirstStep() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).insteadOf(THIS_STEP_SHOULD_BE_SKIPPED)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersAlternativeText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToAlternativeAtFirstStep() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).insteadOf(THIS_STEP_SHOULD_BE_SKIPPED)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		Optional<Step> latestStep = useCaseModelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStep.get().getName());
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void startsOneOfTwoParallelUseCasesByDifferentEvent() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.basicFlow()
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void startsTwoUseCasesSequentially() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.basicFlow().when(textIsAvailable())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactWhileConditionNotFulfilled() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(r -> false)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();

		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersNumber());
		
		assertEquals("", runStepNames());
	}
	
	@Test
	public void interruptsReactWhileBefore() {		
		Model useCaseModel = useCaseModelBuilder
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
				
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void interruptsReactWhileAfter() {
		Model useCaseModel = useCaseModelBuilder
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
				
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +
			CUSTOMER_ENTERS_NUMBER_AGAIN +";", runStepNames());
	}
	
	@Test
	public void reactToStepOnce() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(new Anytime())
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepTwice() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(new Anytime())
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepThreeTimes() {		
		timesDisplayed = 0;
		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredTextAndIncrementCounter())
							.reactWhile(r -> timesDisplayed < 3)
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		// Create way to many events to see if the repeat stops after three events
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner
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
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CONTINUE).continuesAt(CUSTOMER_ENTERS_NUMBER)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAtCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersNumber());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAtNotCalledWhenActorIsWrong() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()				
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.as(secondActor).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAtCalledFromSecondStepOfAlternativeFlow() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAt(CUSTOMER_ENTERS_NUMBER)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			 CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAtCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		Model useCaseModel = useCaseModelBuilder
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
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterSecondStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CONTINUE).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAfter(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersNumber());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterNotCalledWhenActorIsWrong() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAfter(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.as(secondActor).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterCalledFromSecondStepOfAlternativeFlow() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			 CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesAfterCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		Model useCaseModel = useCaseModelBuilder
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
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtFirstStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT + ";" +
				CUSTOMER_ENTERS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText(), entersNumber());
				 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtNotCalledWhenActorIsWrong() {	
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.as(secondActor).run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersText(), entersNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtCalledFromSecondStepOfAlternativeFlow() {		
		Model useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()				
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
				
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continuesWithoutAlternativeAtCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		Model useCaseModel = useCaseModelBuilder
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
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
}
