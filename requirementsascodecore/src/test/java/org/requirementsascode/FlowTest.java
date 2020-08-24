package org.requirementsascode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.systemreaction.IgnoresIt;

public class FlowTest extends AbstractTestCase{
	private Actor secondActor;
	private int timesDisplayed;
		
	@Before
	public void setup() {
		setupWithRecordingModelRunner();
		this.secondActor = new Actor("Second Actor");
	}
	
	@Test
	public void printsTextAutonomously() {
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.build();
		
		modelRunner.run(model);
		
		assertRecordedStepNames(SYSTEM_DISPLAYS_TEXT);
	}

	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRightInFirstStep() {
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).as(customer).system(displaysConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(secondActor).system(displaysConstantText())
			.build();
		
		modelRunner.as(customer).run(model);
		
		assertRecordedStepNames(SYSTEM_DISPLAYS_TEXT);
	}
	
	@Test
	public void printsTextAutonomouslyOnlyIfActorIsRightInSecondStep() {
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).as(customer).system(displaysConstantText())
			.build();
		
		modelRunner.as(customer).run(model);
		
		assertRecordedStepNames(SYSTEM_DISPLAYS_TEXT, SYSTEM_DISPLAYS_TEXT_AGAIN);
	}
	
	@Test
	public void doesntPrintsTextAutonomouslyForActorCalledSystem() {
		Actor systemActor = new Actor("System");
		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).as(systemActor).system(displaysConstantText())
			.build();
		
		modelRunner.run(model);
		
		assertRecordedStepNames();
	}
	
	@Test
	public void printsTextAutonomouslyTwice() {
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
					.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText())
			.build();
		
		modelRunner.run(model);
		
		assertRecordedStepNames(SYSTEM_DISPLAYS_TEXT, SYSTEM_DISPLAYS_TEXT_AGAIN);
	}
	
	@Test
	public void doesNotReactToCommandIfNotRunning() { 	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(model).stop();
		modelRunner.reactTo(entersText());
		
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		
		assertFalse(latestStepRun.isPresent());
		assertRecordedStepNames(new String[0]);
	}
	
	@Test
	public void doesNotReactToMultipleCommandsIfNotRunning() { 	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(model).stop();
		modelRunner.reactTo(entersText(), entersNumber());
		
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		
		assertFalse(latestStepRun.isPresent());
		assertRecordedStepNames(new String[0]);
	}
	
	@Test
	public void doesNotReactToEmptyCommands() { 	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(model).reactTo();
		
		assertRecordedStepNames(new String[0]);
	}
	
	@Test
	public void doesNotReactToEmptyCommandList() { 	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(model).reactTo(new ArrayList<Object>());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();

		assertFalse(latestStepRun.isPresent());
		assertRecordedStepNames(new String[0]);
	}
	
	@Test
	public void doesNotReactToAlreadyRunStep() { 	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(model).reactTo(entersText(), entersText());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT);
	}
	
	
	@Test
	public void oneStepInFlowReactsWithEnteredText() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		modelRunner.run(model);
		reactToAndAssertEvents(entersText());		
	}
	
	@Test
	public void oneStepInFlowReactsWithConstantText() {		
		Model model = buildModel();
				
		modelRunner.run(model);
		reactToAndAssertEvents(entersText());		
	}
	
	@Test
	public void oneStepInFlowReactsWithConstantTextWhenModelIsRebuiltAndRerun() {		
		Model model = buildModel();
		modelRunner.run(model);
		
		model = buildModel();
		modelRunner.run(model);
		reactToAndAssertEvents(entersText());		
	}
	private Model buildModel() {
		return Model.builder()
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysConstantText())
			.build();
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfDifferentType() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(model);
		reactToAndAssertEvents(entersText(), entersNumber());		
	}
	
	@Test
	public void secondStepReactsWhenFirstStepPublishesAnyMessage() {
		final String PROCESS_PUBLISHED_EVENT = "Process published event";
	    
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(super.publishAnyMessageAsString())
					.step(PROCESS_PUBLISHED_EVENT).user(String.class).system(new IgnoresIt<String>())
			.build();
		
		Optional<String> optionalActualText = modelRunner.run(model).reactTo(entersText());
		String actualText = optionalActualText.get();		
		assertEquals(TEXT, actualText);
	}
	
	@Test
	public void secondStepReactsWhenFirstStepPublishesSpecificEvent() {
		final String PROCESS_PUBLISHED_EVENT = "Process published event";
	    
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(super.publishEnteredTextAsString())
					.step(PROCESS_PUBLISHED_EVENT).user(String.class).system(new IgnoresIt<String>())
			.build();
		
		Optional<String> optionalActualText = modelRunner.run(model).reactTo(entersText());
		String actualText = optionalActualText.get();		
		assertEquals(TEXT, actualText);
	}
	
	 @Test
	  public void secondStepReactsWhenFirstStepPublishesViaSupplier() {
	    final String PROCESS_PUBLISHED_EVENT = "Process published event";
	      
	    Model model = modelBuilder
	      .useCase(USE_CASE)
	        .basicFlow()
	          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(super.publishConstantTextAsString())
	          .step(PROCESS_PUBLISHED_EVENT).user(String.class).system(new IgnoresIt<String>())
	      .build();
	    
	    Optional<String> optionalActualText = modelRunner.run(model).reactTo(entersText());
	    String actualText = optionalActualText.get();   
	    assertEquals(TEXT, actualText);
	  }
	
	@Test
	public void twoSequentialStepsReactToEventsOfDifferentTypeAsList() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(model);
		Object[] eventArray = new Object[] {entersText(), entersNumber()};
		List<? extends Object> events = Arrays.asList(eventArray);
		modelRunner.reactTo(events);
		assertArrayEquals(eventArray, modelRunner.getRecordedMessages());		
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfSameType() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(model);
		reactToAndAssertEvents(entersText(), entersText());		
	}
	
	@Test
	public void twoSequentialStepsReactToEventsOfSameTypeAsList() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(model);
		
		EntersText[] eventArray = new EntersText[] {entersText(), entersText()};
		List<EntersText> events = Arrays.asList(eventArray);
		modelRunner.reactTo(events);
		assertArrayEquals(eventArray, modelRunner.getRecordedMessages());		
	}
	
	@Test
	public void twoSequentialStepsReactWhenOneIsUserStepAndOtherIsSystemStep() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class)
					.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.build();
		
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();

		assertTrue(latestStepRun.map(step -> step.getName().equals(SYSTEM_DISPLAYS_TEXT)).orElse(false));
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, SYSTEM_DISPLAYS_TEXT);
	}
	
	@Test
	public void twoSequentialStepsReactOnlyWhenActorIsRight() {		
		Model model = modelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.build();
		
		modelRunner.as(customer).run(model).reactTo(entersText(), entersText());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT);
	}
	
  @Test
  public void twoSequentialStepsReactyOnlyWhenActorHasSameName() {
    Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT)
            .as(customer).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_TEXT_AGAIN)
            .as(secondActor).user(EntersText.class).system(displaysEnteredText())
          .build();

    Actor actorWithSameName = new Actor(customer.getName());
    modelRunner.as(actorWithSameName).run(model).reactTo(entersText(), entersText());

    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT);
  }

  @Test
  public void twoSequentialStepsReactWhenDefaultActorIsRight() {
    Model model = modelBuilder
			.useCase(USE_CASE).as(customer)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor).user(EntersText.class).system(displaysEnteredText())
			.build();
		
    modelRunner.as(customer).run(model).reactTo(entersText(), entersText());

    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT);
  }

  @Test
  public void doesntReactWhenDefaultActorIsWrong() {
    Model model = modelBuilder
      .useCase(USE_CASE).as(secondActor)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor).user(EntersText.class).system(displaysEnteredText())
      .build();

    modelRunner.as(customer).run(model).reactTo(entersText(), entersText());

    assertRecordedStepNames();
  }

  @Test
  public void twoSequentialStepsReactWhenNoActorHasBeenSpecified() {
    Model model = modelBuilder
      .useCase(USE_CASE).basicFlow()
        .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
        .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
      .build();

    modelRunner.as(customer).run(model).reactTo(entersText(), entersText());

    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN);
  }

  @Test
  public void twoSequentialStepsReactWhenActorIsChanged() {
    Model model = modelBuilder
      .useCase(USE_CASE).basicFlow()
        .step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
        .step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor).user(EntersText.class).system(displaysEnteredText())
      .build();

    modelRunner.as(customer).run(model).reactTo(entersText());
    modelRunner.as(secondActor).run(model).reactTo(entersText());

    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN);
  }
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtFirstPosition() {	
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(customer, secondActor).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.as(customer).run(model);
		reactToAndAssertEvents(entersText(), entersText());
	}
	
	@Test
	public void twoSequentialStepsReactWhenSeveralActorsContainRightActorAtSecondPosition() {		
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
				.build();
		
		modelRunner.as(customer).run(model);
		reactToAndAssertEvents(entersText(), entersText());		
	}
	
	@Test
	public void twoSequentialStepsReactWhenRunningWithDifferentActors() { 		
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER)
						.as(secondActor).user(EntersNumber.class).system(displaysEnteredNumber())
				.build();
		
		modelRunner.as(customer).run(model).reactTo(entersText());
		
		modelRunner.as(secondActor).reactTo(entersNumber());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_NUMBER);
	}
	
	@Test
	public void onlyStepWithTrueConditionReacts() { 	
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())			
				.flow("Alternative Flow: Skipped").condition(() -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
			.build();
		
		modelRunner.run(model).reactTo(entersText(), entersText());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT);
	}
	
	@Test
	public void stepThasHasTrueConditionReactsEvenIfOtherStepWouldBePerformedBySystem() { 		
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())			
				.flow("Alternative Flow: Skipped").condition(() -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED).system(() -> System.out.println("You should not see this!"))
			.build();
		
		modelRunner.run(model).reactTo(entersText(), entersText());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT);
	}
	
	@Test
	public void onlyStepWithRightActorInDifferentFlowReacts() { 
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(secondActor).user(EntersText.class).system(throwsRuntimeException())			
			.flow(ALTERNATIVE_FLOW).condition(this::textIsNotAvailable)
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(customer).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.as(customer).run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		
		assertEquals(CUSTOMER_ENTERS_TEXT_AGAIN, latestStepRun.get().getName());
	}
	
	@Test
	public void onlyStepWithDefaultActorInDifferentFlowReacts() { 
		Model model = modelBuilder
			.useCase(USE_CASE).as(customer)	
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT)
						.as(secondActor).user(EntersText.class).system(throwsRuntimeException())			
			.flow(ALTERNATIVE_FLOW).condition(this::textIsNotAvailable)
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.as(customer).run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		
		assertEquals(CUSTOMER_ENTERS_TEXT_AGAIN, latestStepRun.get().getName());
	}
	
	@Test
	public void doesNotReactIfStepHasWrongActorInDifferentFlow() { 
		Model model = modelBuilder
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
		
		modelRunner.as(customer).run(model).reactTo(entersText(), entersText());
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStepName());
	}
	
	@Test
	public void doesNotReactIfStepHasRightActorButFalseCondition() {	
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(secondActor).user(EntersText.class).system(displaysEnteredText())				
				.flow(ALTERNATIVE_FLOW).condition(() -> false)
					.step(THIS_STEP_SHOULD_BE_SKIPPED)
						.as(customer).user(EntersText.class).system(displaysEnteredText())
			.build();
				
		modelRunner.as(customer).run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		
		assertFalse(latestStepRun.isPresent());
	}
	
	@Test
	public void reactsToFirstStepAlternativeWhen() {		
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()					
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EntersText.class).system(throwsRuntimeException())		
			.flow(ALTERNATIVE_FLOW).condition(this::textIsNotAvailable)
				.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
		
		modelRunner.run(model).reactTo(entersText());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_ALTERNATIVE_TEXT);
	}
	
	@Test
	public void reactsToSecondStepAlternativeWhen() {		
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).condition(() -> CUSTOMER_ENTERS_TEXT.equals(latestStepName()))
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(model).reactTo(entersText(), entersAlternativeText());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_ALTERNATIVE_TEXT);
	}
	
	@Test
	public void doesNotReenterAlternativeFlowEvenIfItHasTrueCondition() {		
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).anytime()
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(model)
			.reactTo(entersText(), entersNumber(), entersText(), entersNumber());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, CUSTOMER_ENTERS_NUMBER);
	}
	 
	@Test
	public void reactsToAlternativeAfterFirstStep() {		
		Model model = modelBuilder
			.useCase(USE_CASE)		
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).insteadOf(THIS_STEP_SHOULD_BE_SKIPPED)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(model);
		
    reactToAndAssertEvents(entersText(), entersAlternativeText());		
	}
	
	 @Test
	  public void reactsToAlternativeOfAlternative() {   
	    Model model = modelBuilder
	      .useCase(USE_CASE)    
	        .basicFlow()
	          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
	          .step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())    
	        .flow(ALTERNATIVE_FLOW).insteadOf(THIS_STEP_SHOULD_BE_SKIPPED)
	          .step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
	        .flow(ALTERNATIVE_FLOW_2).insteadOf(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
            .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
	      .build();
	    
	    modelRunner.run(model).reactTo(entersNumber(), entersText(), entersNumber());
	    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_NUMBER);
	  }
	
	@Test
	public void reactsToAlternativeAtFirstStep() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(THIS_STEP_SHOULD_BE_SKIPPED).user(EntersText.class).system(throwsRuntimeException())
					.step(THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL).user(EntersText.class).system(throwsRuntimeException())		
				.flow(ALTERNATIVE_FLOW).insteadOf(THIS_STEP_SHOULD_BE_SKIPPED)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();
		
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		
		assertEquals(CUSTOMER_ENTERS_ALTERNATIVE_TEXT, latestStepRun.get().getName());
		assertRecordedStepNames(CUSTOMER_ENTERS_ALTERNATIVE_TEXT);
	}
	
	@Test
	public void startsOneOfTwoParallelUseCasesByDifferentEvent() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.basicFlow()
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(model);
		reactToAndAssertEvents(entersNumber());		
	}
	
	@Test
	public void startsTwoUseCasesSequentially() {	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.basicFlow().condition(this::textIsAvailable)
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
		modelRunner.run(model);
		reactToAndAssertEvents(entersText(), entersNumber());		
	}
	
	@Test
	public void doesNotReactWhileConditionNotFulfilled() {		
		Model model = modelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(() -> false)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();

		modelRunner.run(model).reactTo(entersText(), entersNumber());
		
		assertRecordedStepNames(new String[0]);
	}
	
	@Test
	public void interruptsReactWhileBefore() {		
		Model model = modelBuilder
			.useCase(USE_CASE)			
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(() -> true)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
			.build();
				
		modelRunner.run(model).reactTo(entersText(), entersText(), entersNumber());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_ALTERNATIVE_TEXT);
	}
	
	@Test
	public void interruptsReactWhileAfter() {
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(() -> true)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_NUMBER)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		modelRunner.run(model).reactTo(entersText(), entersText(), entersNumber());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN, CUSTOMER_ENTERS_ALTERNATIVE_NUMBER);
	}
	
	@Test
	public void reactToStepOnce() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(() -> true)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		modelRunner.run(model);
		reactToAndAssertEvents(entersText(), entersNumber());		
	}
	
	@Test
	public void reactToStepTwice() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredText())
							.reactWhile(() -> true)
					.step(CUSTOMER_ENTERS_NUMBER)
						.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		modelRunner.run(model);
		reactToAndAssertEvents(entersText(), entersText(), entersNumber());		
	}
	
	@Test
	public void reactToStepThreeTimes() {		
		timesDisplayed = 0;
		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.user(EntersText.class).system(displaysEnteredTextAndIncrementCounter())
							.reactWhile(() -> timesDisplayed < 3)
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
				
		// Create way to many events to see if the repeat stops after three events
		modelRunner.run(model)
			.reactTo(entersText(), entersText(), entersText(),
				entersText(), entersText(), entersText(),
				entersText(), entersText(), entersText(),
				entersNumber());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT,
			CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_NUMBER);
	}
	private Consumer<EntersText> displaysEnteredTextAndIncrementCounter(){
		return enteredText -> {
			displaysEnteredText().accept(enteredText);
			timesDisplayed++;
		};
	}
	
	@Test
	public void continuesAtThirdStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())    
			
        .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
          .step(CONTINUE).continuesAt(CUSTOMER_ENTERS_NUMBER)

			.build();
		
    modelRunner.run(model).reactTo(entersText(), entersNumber());

    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CONTINUE, CUSTOMER_ENTERS_NUMBER);
  }
	
	 @Test
	  public void continuesWithPreferredFlow() {    
	    Model model = modelBuilder
	      .useCase(USE_CASE)
          .basicFlow()
            .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
            .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
            .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
            
	        .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
	          .step(CONTINUE).continuesAt(CUSTOMER_ENTERS_NUMBER)
	          
	        .flow("Preferred Flow").insteadOf(CUSTOMER_ENTERS_NUMBER)
            .step(CUSTOMER_ENTERS_ALTERNATIVE_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())    

	      .build();
	    
	    modelRunner.run(model).reactTo(entersText(), entersNumber());

      assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CONTINUE, CUSTOMER_ENTERS_ALTERNATIVE_NUMBER);
	  }
	 
   @Test
   public void doesntContinueWithIgnoredFlowIfItIsAnAfterFlow() {    
     Model model = modelBuilder
       .useCase(USE_CASE)
         .basicFlow()
           .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
           .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
           .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
           
         .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
           .step(CONTINUE).continuesAt(CUSTOMER_ENTERS_NUMBER)
           
         .flow("Ignored Flow").after(CUSTOMER_ENTERS_TEXT_AGAIN)
           .step(CUSTOMER_ENTERS_ALTERNATIVE_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())    

       .build();
     
     modelRunner.run(model).reactTo(entersText(), entersNumber());

     assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CONTINUE, CUSTOMER_ENTERS_NUMBER);
   }
   
   @Test
   public void doesntContinueWithIgnoredFlowIfConditionFalse() {    
     Model model = modelBuilder
       .useCase(USE_CASE)
         .basicFlow()
           .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
           .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
           .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
           
         .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
           .step(CONTINUE).continuesAt(CUSTOMER_ENTERS_NUMBER)
           
         .flow("Ignored Flow").insteadOf(CUSTOMER_ENTERS_NUMBER).condition(() -> false)
           .step(CUSTOMER_ENTERS_ALTERNATIVE_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())    

       .build();
     
     modelRunner.run(model).reactTo(entersText(), entersNumber());

     assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CONTINUE, CUSTOMER_ENTERS_NUMBER);
   }
	
	@Test
  public void continuesAtThirdStepCalledFromFirstStepOfAlternativeFlowWithEvent() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
          .step(CONTINUE).on(EntersText.class).continuesAt(CUSTOMER_ENTERS_NUMBER)
          
        .flow("Main Flow")
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersNumber.class).system(displaysEnteredNumber())
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())    

      .build();
    
    modelRunner.run(model).reactTo(entersText(), entersText(), entersNumber());
     
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CONTINUE,
      CUSTOMER_ENTERS_NUMBER);
  }
	
	@Test
  public void doesntContinueAtThirdStepCalledFromFirstStepOfAlternativeFlowWithWrongEvent() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
          .step(CONTINUE).on(EntersNumber.class).continuesAt(CUSTOMER_ENTERS_NUMBER)
          
        .flow("Main Flow")
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())    
      .build();
    
    modelRunner.run(model).reactTo(entersText(), entersText(), entersNumber());

    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER);
  }

  @Test
	public void continuesAfterSecondStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
        .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
          .step(CONTINUE).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
				
        .flow("Main Flow")
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		

			.build();
		
		modelRunner.run(model).reactTo(entersText(), entersNumber());
		 
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CONTINUE, CUSTOMER_ENTERS_NUMBER);
	}
	
	@Test
	public void continuesAfterCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAfter(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.as(customer).run(model).reactTo(entersText(), entersNumber());
		 
		assertRecordedStepNames(CONTINUE, CUSTOMER_ENTERS_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER);
	}
	
	@Test
	public void continuesAfterNotCalledWhenActorIsWrong() {	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()			
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAfter(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.as(secondActor).run(model).reactTo(entersText(), entersText(), entersNumber());
		 
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER);
	}
	
	@Test
	public void continuesAfterCalledFromSecondStepOfAlternativeFlow() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()	
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.run(model).reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_ALTERNATIVE_TEXT, CONTINUE, 
			 CUSTOMER_ENTERS_NUMBER);
	}
	
	@Test
	public void continuesAfterCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).condition(this::textIsAvailable)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)		
				.flow(ALTERNATIVE_FLOW_2).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).condition(this::textIsNotAvailable)
					.step("Customer enters alternative number").user(EntersNumber.class).system(displaysEnteredNumber())
					.step(CONTINUE_2).continuesAfter(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.run(model).reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_ALTERNATIVE_TEXT, CONTINUE, 
			CUSTOMER_ENTERS_NUMBER);
	}
	
	@Test
	public void continuesAtFirstStepCalledFromFirstStepOfAlternativeFlowWithoutEvent() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).continuesAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.run(model).reactTo(entersText(), entersText());
		 
		assertRecordedStepNames(CONTINUE, CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN);
	}
	
	@Test
  public void continuesAtFirstStepCalledFromFirstStepOfAlternativeFlowWithEvent() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())    
        .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
          .step(CONTINUE).on(EntersText.class).continuesAt(CUSTOMER_ENTERS_TEXT)
      .build();
    
    modelRunner.run(model).reactTo(entersText(), entersText(), entersText());
     
    assertRecordedStepNames(CONTINUE, CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN);
  }
  
  @Test
  public void doesntContinueAtFirstStepCalledFromFirstStepOfAlternativeFlowWithWrongEvent() {    
    Model model = modelBuilder
      .useCase(USE_CASE)
        .basicFlow()
          .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
          .step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
        .flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
          .step(CONTINUE).on(EntersNumber.class).continuesAt(CUSTOMER_ENTERS_TEXT)
      .build();
    
    modelRunner.run(model).reactTo(entersText(), entersText());
     
    assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN);
  }
	
	@Test
	public void continuesAtCalledFromFirstStepOfAlternativeFlowWithRightActor() {	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.as(customer).run(model).reactTo(entersText(), entersText(), entersNumber());
				 
		assertRecordedStepNames(CONTINUE, CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER);
	}
	
	@Test
	public void continuesAtNotCalledWhenActorIsWrong() {	
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).as(secondActor).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(secondActor, customer).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).as(secondActor, customer).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT)
					.step(CONTINUE).as(customer).continuesAt(CUSTOMER_ENTERS_TEXT)
			.build();
		
		modelRunner.as(secondActor).run(model).reactTo(entersText(), entersText(), entersNumber());
		 
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER);
	}
	
	@Test
	public void continuesAtCalledFromSecondStepOfAlternativeFlow() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()				
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.run(model);
		modelRunner.reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
				
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_ALTERNATIVE_TEXT, CONTINUE,
			CUSTOMER_ENTERS_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER);
	}
	
	@Test
	public void continuesAtCalledFromMultipleMutuallyExclusiveAlternativeFlows() {		
		Model model = modelBuilder
			.useCase(USE_CASE)
				.basicFlow()		
					.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())		
				.flow(ALTERNATIVE_FLOW).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).condition(this::textIsAvailable)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
					.step(CONTINUE).continuesAt(CUSTOMER_ENTERS_TEXT_AGAIN)		
				.flow(ALTERNATIVE_FLOW_2).insteadOf(CUSTOMER_ENTERS_TEXT_AGAIN).condition(this::textIsNotAvailable)
					.step(CUSTOMER_ENTERS_ALTERNATIVE_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
					.step(CONTINUE_2).continuesAt(CUSTOMER_ENTERS_TEXT_AGAIN)
			.build();
		
		modelRunner.run(model).reactTo(entersText(), entersAlternativeText(), entersText(), entersNumber());
		
		assertRecordedStepNames(CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_ALTERNATIVE_TEXT, CONTINUE,
			CUSTOMER_ENTERS_TEXT_AGAIN, CUSTOMER_ENTERS_NUMBER);
	}
}
