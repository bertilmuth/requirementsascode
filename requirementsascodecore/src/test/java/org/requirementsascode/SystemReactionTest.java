package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

public class SystemReactionTest extends AbstractTestCase{
	private Actor secondActor;
	private int timesDisplayed;
		
	@Before
	public void setup() {
		setupWith(new TestUseCaseModelRunner());
		this.secondActor = useCaseModelBuilder.actor("Second Actor");
	}
	
	@Test
	public void useCaseModelRunnerIsNotRunningAtFirst(){
		assertFalse(useCaseModelRunner.isRunning());
	}
	
	@Test
	public void useCaseModelRunnerIsRunningAfterRunCall(){
		UseCaseModel model = useCaseModelBuilder.build();
		useCaseModelRunner.run(model);
		assertTrue(useCaseModelRunner.isRunning());
	}
	
	@Test
	public void useCaseModelRunnerIsNotRunningWhenBeingStoppedBeforeRunCall(){
		useCaseModelRunner.stop();
		assertFalse(useCaseModelRunner.isRunning());
	}
	
	@Test
	public void useCaseModelRunnerIsNotRunningWhenBeingStoppedAfterRunCall(){
		UseCaseModel model = useCaseModelBuilder.build();
		useCaseModelRunner.run(model);
		useCaseModelRunner.stop();
		assertFalse(useCaseModelRunner.isRunning());
	}
	
	@Test
	public void printsTextAutonomously() {
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRightInFirstStep() {
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).as(customer).system(displayConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(secondActor).system(displayConstantText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRightInSecondStep() {
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(customer).system(displayConstantText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_DISPLAYS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyTwice() {
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT +";" + SYSTEM_DISPLAYS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactToEventIfNotRunning() { 	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.stop();
		useCaseModelRunner.reactTo(enterText());
		
		assertEquals("", runStepNames());
	}
	
	@Test
	public void doesNotReactToAlreadyRunStep() { 	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void cantReactIfNotRunning() {	
		useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
						
		boolean canReact = useCaseModelRunner.canReactTo(enterText().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void cantReactIfEventIsWrong() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		
		boolean canReact = useCaseModelRunner.canReactTo(enterNumber().getClass());
		assertFalse(canReact);
	}
	
	@Test
	public void oneStepCanReactIfEventIsRight() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		
		boolean canReact = useCaseModelRunner.canReactTo(enterText().getClass());
		assertTrue(canReact);
		
		Set<Step> stepsThatCanReact = useCaseModelRunner.stepsThatCanReactTo(enterText().getClass());
		assertEquals(1, stepsThatCanReact.size());
		assertEquals(CUSTOMER_ENTERS_TEXT, stepsThatCanReact.iterator().next().getName().toString());
	}
	
	@Test
	public void moreThanOneStepCanReact() { 
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow().when(run -> true)
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.flow("Alternative Flow: Could react as well").when(run -> true)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		boolean canReact = useCaseModelRunner.canReactTo(enterText().getClass());
		assertTrue(canReact);
		
		Set<Step> stepsThatCanReact = useCaseModelRunner.stepsThatCanReactTo(enterText().getClass());
		assertEquals(2, stepsThatCanReact.size());
	}
	
	@Test
	public void oneStepReacts() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		Optional<Step> latestStepRun = useCaseModelRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT, latestStepRun.get().getName());
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfSameType() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfDifferentType() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenOneIsUserStepAndOtherIsSystemStep() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class)
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + SYSTEM_DISPLAYS_TEXT +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactOnlyWhenActorIsRight() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenActorIsChanged() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText());
		
		useCaseModelRunner.as(secondActor).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtFirstPosition() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(customer, secondActor).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtSecondPosition() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor, customer).user(EnterText.class).system(displayEnteredText())
				.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_TEXT_AGAIN +";", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactWhenRunningWithDifferentActors() { 		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER)
						.as(secondActor).user(EnterNumber.class).system(displayEnteredNumber())
				.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText());
		
		useCaseModelRunner.as(secondActor);
		useCaseModelRunner.reactTo(enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithTruePredicateReacts() { 	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())			
				.flow("Alternative Flow: Skipped").when(r -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";", runStepNames());
	}
	
	@Test
	public void stepThasHasTruePredicateReactsEvenIfOtherStepWouldBePerformedBySystem() { 		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())			
				.flow("Alternative Flow: Skipped").when(r -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED).system(r -> {System.out.println("You should not see this!");})
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorReacts() { 		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EnterText.class).system(throwRuntimeException())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void onlyStepWithRightActorInDifferentFlowReacts() { 
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(secondActor).user(EnterText.class).system(throwRuntimeException())			
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(customer).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		Optional<Step> lastStepRun = useCaseModelRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT_AGAIN, lastStepRun.get().getName());
	}
	
	@Test
	public void stepWithWrongActorInDifferentFlowDoesNotReact() { 
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())			
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
					.step(THIS_STEP_SHOULD_BE_SKIPPED)
						.as(secondActor).user(EnterText.class).system(throwRuntimeException())
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStepName());
	}
	
	@Test
	public void doesNotReactIfStepHasRightActorButFalsePredicate() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(secondActor).user(EnterText.class).system(displayEnteredText())				
				.flow(ALTERNATIVE_FLOW).when(r -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED)
						.as(customer).user(EnterText.class).system(displayEnteredText())
			.build();
				
		useCaseModelRunner.as(customer).run(useCaseModel);
		Optional<Step> lastStepRun = useCaseModelRunner.reactTo(enterText());
		
		assertFalse(lastStepRun.isPresent());
	}
	
	@Test
	public void reactsToFirstStepAlternativeWhen() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()					
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
			.flow(ALTERNATIVE_FLOW).when(textIsNotAvailable())
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
		.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToSecondStepAlternativeWhen() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())		
				.flow(ALTERNATIVE_FLOW).when(r -> CUSTOMER_ENTERS_TEXT.equals(latestStepName()))
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterAlternativeText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void doesNotReenterAlternativeFlowEvenIfItHasTruePredicate() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
				.flow(ALTERNATIVE_FLOW).when(r -> true)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner
			.reactTo(enterAlternativeText(), enterNumber(), enterAlternativeText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	 
	@Test
	public void reactsToAlternativeAfterFirstStep() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())		
				.flow(ALTERNATIVE_FLOW).insteadOf(THIS_STEP_SHOULD_BE_SKIPPED)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterAlternativeText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void reactsToAlternativeAtFirstStep() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EnterText.class).system(throwRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EnterText.class).system(throwRuntimeException())		
				.flow(ALTERNATIVE_FLOW).insteadOf(THIS_STEP_SHOULD_BE_SKIPPED)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		Optional<Step> latestStep = useCaseModelRunner.reactTo(enterText());
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStep.get().getName());
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void startsOneOfTwoParallelUseCasesByDifferentEvent() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.useCase(USE_CASE_2)
				.basicFlow()
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void startsTwoUseCasesSequentially() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.useCase(USE_CASE_2)
				.basicFlow().when(textIsAvailable())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT +";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void doesNotReactWhileConditionNotFulfilled() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EnterText.class).system(displayEnteredText())
							.reactWhile(r -> false)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EnterNumber.class).system(displayEnteredNumber())
			.build();

		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterNumber());
		
		assertEquals("", runStepNames());
	}
	
	@Test
	public void interruptsReactWhileBefore() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.user(EnterText.class).system(displayEnteredText())
							.reactWhile(r -> true)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EnterNumber.class).system(displayEnteredNumber())
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
						.user(EnterText.class).system(displayEnteredText())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";", runStepNames());
	}
	
	@Test
	public void interruptsReactWhileAfter() {
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.user(EnterText.class).system(displayEnteredText())
							.reactWhile(r -> true)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EnterNumber.class).system(displayEnteredNumber())
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_NUMBER)
					.step(CUSTOMER_ENTERS_NUMBER_AGAIN)
						.user(EnterNumber.class).system(displayEnteredNumber())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +
			CUSTOMER_ENTERS_NUMBER_AGAIN +";", runStepNames());
	}
	
	@Test
	public void reactToStepOnce() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EnterText.class).system(displayEnteredText())
							.reactWhile(r -> true)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EnterNumber.class).system(displayEnteredNumber())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepTwice() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EnterText.class).system(displayEnteredText())
							.reactWhile(r -> true)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EnterNumber.class).system(displayEnteredNumber())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void reactToStepThreeTimes() {		
		timesDisplayed = 0;
		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EnterText.class).system(displayEnteredTextAndIncrementCounter())
							.reactWhile(r -> timesDisplayed < 3)
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())
			.build();
				
		// Create way to many events to see if the repeat stops after three events
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner
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
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CONTINUE).continueAt(CUSTOMER_ENTERS_NUMBER)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAtCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continueAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAtNotCalledWhenActorIsWrong() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()				
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continueAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.as(secondActor).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAtCalledFromSecondStepOfAlternativeFlow() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CONTINUE).continueAt(CUSTOMER_ENTERS_NUMBER)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			 CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAtCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CONTINUE).continueAt(CUSTOMER_ENTERS_NUMBER)		
				.flow(ALTERNATIVE_FLOW_2).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsNotAvailable())
					.step("Customer enters alternative number").user(EnterNumber.class).system(displayEnteredNumber())
					.step(CONTINUE_2).continueAt(CUSTOMER_ENTERS_NUMBER)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterSecondStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CONTINUE).continueAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continueAfter(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterNumber());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterNotCalledWhenActorIsWrong() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continueAfter(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.as(secondActor).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterCalledFromSecondStepOfAlternativeFlow() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CONTINUE).continueAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			 CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueAfterCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CONTINUE).continueAfter(CUSTOMER_ENTERS_TEXT_AGAIN)		
				.flow(ALTERNATIVE_FLOW_2).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsNotAvailable())
					.step("Customer enters alternative number").user(EnterNumber.class).system(displayEnteredNumber())
					.step(CONTINUE_2).continueAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueWithoutAlternativeAtFirstStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).continueWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText());
		 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT + ";" +
				CUSTOMER_ENTERS_TEXT_AGAIN + ";", runStepNames());
	}
	
	@Test
	public void continueWithoutAlternativeAtCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continueWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.as(customer).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText(), enterNumber());
				 
		assertEquals(CONTINUE + ";" + CUSTOMER_ENTERS_TEXT + ";" +
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueWithoutAlternativeAtNotCalledWhenActorIsWrong() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continueWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		useCaseModelRunner.as(secondActor).run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterText(), enterNumber());
		 
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_TEXT_AGAIN + ";" +  CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueWithoutAlternativeAtCalledFromSecondStepOfAlternativeFlow() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()				
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CONTINUE).continueWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
				
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" +
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
	
	@Test
	public void continueWithoutAlternativeAtCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EnterText.class).system(displayEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EnterNumber.class).system(displayEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsAvailable())
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EnterText.class).system(displayEnteredText())
					.step(CONTINUE).continueWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT_AGAIN)		
				.flow(ALTERNATIVE_FLOW_2).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).when(textIsNotAvailable())
					.step(CUSTOMER_ENTERS_NUMBER_AGAIN).user(EnterNumber.class).system(displayEnteredNumber())
					.step(CONTINUE_2).continueWithoutAlternativeAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText(), enterAlternativeText(), enterText(), enterNumber());
		
		assertEquals(CUSTOMER_ENTERS_TEXT + ";" + CUSTOMER_ENTERS_ALTERNATIVE_TEXT + ";" + CONTINUE + ";" + 
			CUSTOMER_ENTERS_TEXT_AGAIN + ";" + CUSTOMER_ENTERS_NUMBER + ";", runStepNames());
	}
}
