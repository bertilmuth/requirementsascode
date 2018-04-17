package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.requirementsascode.predicate.Anytime;

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
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT + ";", runStepNames());
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRightInFirstStep() {
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
	@Ignore
	public void oneFlowlessStepReacts() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.handles(EntersText.class).system(displaysEnteredText())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		Optional<Step> latestStepRun = useCaseModelRunner.reactTo(entersText());
		
		assertEquals(EntersText.class, latestStepRun.get().getUserEventClass());
	}
	
	@Test
	public void oneStepInFlowReacts() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		useCaseModelRunner.run(useCaseModel);
		Optional<Step> latestStepRun = useCaseModelRunner.reactTo(entersText());
		
		assertEquals(CUSTOMER_ENTERS_TEXT, latestStepRun.get().getName());
	}
	
	@Test
	@Ignore
	public void twoFlowlessStepsReactToEventsOfDifferentTypeInRightOrder() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.handles(EntersText.class).system(displaysEnteredText())
				.handles(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		
		Optional<Step> latestStepRun = useCaseModelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getUserEventClass());
		
		latestStepRun = useCaseModelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getUserEventClass());
		
		latestStepRun =  useCaseModelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getUserEventClass());
		
		assertEquals("S1;S2;", runStepNames());
	}
	
	@Test
	@Ignore
	public void twoFlowlessStepsReactToEventsOfDifferentTypeInWrongOrder() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.handles(EntersText.class).system(displaysEnteredText())
				.handles(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersNumber(), entersText());
		Optional<Step> latestStepRun =  useCaseModelRunner.getLatestStep();
		
		assertEquals(EntersText.class, latestStepRun.get().getUserEventClass());
		assertEquals("S1;S2;", runStepNames());
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfDifferentType() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
	public void onlyStepWithTruePredicateReacts() { 	
		UseCaseModel useCaseModel = useCaseModelBuilder
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
	public void stepThasHasTruePredicateReactsEvenIfOtherStepWouldBePerformedBySystem() { 		
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
	public void doesNotReactIfStepHasRightActorButFalsePredicate() {	
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
	public void doesNotReenterAlternativeFlowEvenIfItHasTruePredicate() {		
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
		UseCaseModel useCaseModel = useCaseModelBuilder
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
