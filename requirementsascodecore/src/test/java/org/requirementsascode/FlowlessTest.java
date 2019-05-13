package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.systemreaction.IgnoresIt;

public class FlowlessTest extends AbstractTestCase {

    @Before
    public void setUp() throws Exception {
    	setupWithRecordingModelRunner();
    }

    @Test
    public void oneFlowlessStepReactsToEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
		.build();
	
		modelRunner.run(model);
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
	
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void oneFlowlessStepReactsToCommand() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).system(displaysEnteredText())
		.build();
	
		modelRunner.run(model);
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
	
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }

    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInRightOrder() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInRightOrder() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInWrongOrder() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);

		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInWrongOrder() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);

		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInDifferentUseCases() {
		Model model = modelBuilder
			.useCase(USE_CASE)
				.on(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInDifferentUseCases() {
		Model model = modelBuilder
			.useCase(USE_CASE)
				.user(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> true).on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
		
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithCommand() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> true).user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
		
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithoutEventAndSecondStepWithEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).system(displaysConstantText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithoutEventAndSecondStepWithCommand() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).system(displaysConstantText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithoutEventButWithModelRunnerArgument() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).system(modelRunner -> displaysConstantText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInSecondStepWithEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> true).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInSecondStepWithoutEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> modelRunner.getLatestStep().isPresent() && modelRunner.getLatestStep().get().getEventClass().equals(EntersText.class)).system(displaysConstantText())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		assertEquals(ModelRunner.class, modelRunner.getLatestStep().get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInFirstStep() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> false).on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInSecondStep() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> false).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void oneFlowlessStepReactsToEventWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
		.build();
	
		modelRunner.run(model);
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
	
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void oneFlowlessStepReactsToCommandWithoutUseCase() {
		Model model = modelBuilder
			.user(EntersText.class).system(displaysEnteredText())
		.build();
	
		modelRunner.run(model);
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
	
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }

    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInRightOrderWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInRightOrderWithoutUseCase() {
		Model model = modelBuilder
			.user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInWrongOrderWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInWrongOrderWithoutUseCase() {
		Model model = modelBuilder
			.user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToEventWhenConditionIsTrueInFirstStepWithoutUseCase() {
		Model model = modelBuilder
			.condition(() -> true).on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
		
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandWhenConditionIsTrueInFirstStepWithoutUseCase() {
		Model model = modelBuilder
			.condition(() -> true).user(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
		
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInSecondStepWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> true).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInFirstStepWithoutUseCase() {
		Model model = modelBuilder
			.condition(() -> false).on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInSecondStepWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> false).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
	
		latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void secondFlowlessStepReactsWhenFirstStepPublishesOnEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).systemPublish(super::publishEnteredTextAsString)
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(String.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void secondFlowlessStepReactsWhenFirstStepPublishesOnCommand() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).systemPublish(super::publishEnteredTextAsString)
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
		assertEquals(String.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void secondFlowlessStepsReactsToEventWhenConditionIsTrueInFirstPublishingStepWithoutEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).systemPublish(publishConstantText())
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(String.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void secondFlowlessStepsReactsToCommandWhenConditionIsTrueInFirstPublishingStepWithoutEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).systemPublish(publishConstantText())
			.user(String.class).system(new IgnoresIt<>())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertEquals(String.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void secondFlowlessStepDoesntReactWhenConditionIsFalseInFirstPublishingStepWithoutEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> modelRunner.getLatestStep().isPresent()).systemPublish(publishConstantText())
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		modelRunner.run(model);
	
		Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
		assertFalse(latestStepRun.isPresent());
    }
}
