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
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
	
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void oneFlowlessStepReactsToCommand() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).system(displaysEnteredText())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
	
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }

    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInRightOrder() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInRightOrder() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInWrongOrder() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersText());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInWrongOrder() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersText()); 
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInDifferentUseCases() {
		Model model = modelBuilder
			.useCase(USE_CASE)
				.on(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersText());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInDifferentUseCases() {
		Model model = modelBuilder
			.useCase(USE_CASE)
				.user(EntersText.class).system(displaysEnteredText())
			.useCase(USE_CASE_2)
				.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersText());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> true).on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithCommand() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> true).user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithoutEventAndSecondStepWithEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).system(displaysConstantText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithoutEventAndSecondStepWithCommand() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).system(displaysConstantText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithoutEventButWithModelRunnerArgument() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).system(modelRunner -> displaysConstantText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInSecondStepWithEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> true).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInSecondStepWithoutEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> modelRunner.getLatestStep().isPresent() && modelRunner.getLatestStep().get().getMessageClass().equals(EntersText.class)).system(displaysConstantText())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(ModelRunner.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInFirstStep() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> false).on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersText());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInSecondStep() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> false).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void oneFlowlessStepReactsToEventWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void oneFlowlessStepReactsToCommandWithoutUseCase() {
		Model model = modelBuilder
			.user(EntersText.class).system(displaysEnteredText())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }

    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInRightOrderWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInRightOrderWithoutUseCase() {
		Model model = modelBuilder
			.user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInWrongOrderWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersText());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandsOfDifferentTypeInWrongOrderWithoutUseCase() {
		Model model = modelBuilder
			.user(EntersText.class).system(displaysEnteredText())
			.user(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersText());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToEventWhenConditionIsTrueInFirstStepWithoutUseCase() {
		Model model = modelBuilder
			.condition(() -> true).on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToCommandWhenConditionIsTrueInFirstStepWithoutUseCase() {
		Model model = modelBuilder
			.condition(() -> true).user(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInSecondStepWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> true).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInFirstStepWithoutUseCase() {
		Model model = modelBuilder
			.condition(() -> false).on(EntersText.class).system(displaysEnteredText())
			.on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersText());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInSecondStepWithoutUseCase() {
		Model model = modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
			.condition(() -> false).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		modelRunner.reactTo(entersNumber());
		latestStepRun = modelRunner.getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void publishEnteredTextAsStringWithOnInFirstStep() {
		Model model = modelBuilder.useCase(USE_CASE)
			.on(EntersText.class).systemPublish(super::publishEnteredTextAsString)
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		String actualText = (String)modelRunner.run(model).reactTo(entersText()).get();
		assertEquals(TEXT, actualText);
    }
    
    @Test
    public void publishEnteredTextAsStringWithUserInFirstStep() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).systemPublish(super::publishEnteredTextAsString)
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		String actualText = (String)modelRunner.run(model).reactTo(entersText()).get();
		assertEquals(TEXT, actualText);
    }
    
    @Test
    public void publishIntegerInsteadOfEnteredText() {
    	final Integer EXPECTED_INTEGER = 1234;
    	
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).systemPublish(super::publishEnteredTextAsString)
			.on(String.class).systemPublish(text ->  EXPECTED_INTEGER)
		.build();
	
		Integer actualInteger = (Integer)modelRunner.run(model).reactTo(entersText()).get();
		assertEquals(EXPECTED_INTEGER, actualInteger);
    }
    
    @Test
    public void publishEnteredTextAsStringAfterDifferentEvent() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).systemPublish(super::publishEnteredTextAsString)
			.on(EntersNumber.class).system(new IgnoresIt<>())
		.build();
	
		String actualText = (String)modelRunner.run(model).reactTo(entersNumber(), entersText()).get();
		assertEquals(TEXT, actualText);
    }
    
    @Test
    public void publishConstantTextAsStringWithConditionAndOnInSecondStep() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).systemPublish(publishConstantTextAsString())
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		modelRunner.run(model);
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(String.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void publishConstantTextAsStringWithConditionAndUserInSecondStep() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> !modelRunner.getLatestStep().isPresent()).systemPublish(publishConstantTextAsString())
			.user(String.class).system(new IgnoresIt<>())
		.build();
	
		modelRunner.run(model);
		Optional<Step> latestStepRun = modelRunner.getLatestStep();
		assertEquals(String.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void dontPublishConstantTextAsStringWhenConditionFalse() {
		Model model = modelBuilder.useCase(USE_CASE)
			.condition(() -> modelRunner.getLatestStep().isPresent()).systemPublish(publishConstantTextAsString())
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		Optional<Object> optionalEventObject = modelRunner.run(model).reactTo(entersNumber());
		assertFalse(optionalEventObject.isPresent());
    }
    
    @Test
    public void dontPublishFirstEnteredTextWhenReactToIsCalledTheSecondTime() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).systemPublish(super::publishEnteredTextAsString)
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		modelRunner.run(model).reactTo(entersText());
		Optional<Object> publishedEvent = modelRunner.reactTo(TEXT);
		assertFalse(publishedEvent.isPresent());
    }
    
    @Test
    public void dontPublishOneOfTheEnteredTextsWhenReactToIsCalledTheSecondTime() {
		Model model = modelBuilder.useCase(USE_CASE)
			.user(EntersText.class).systemPublish(super::publishEnteredTextAsString)
			.on(String.class).system(new IgnoresIt<>())
		.build();
	
		modelRunner.run(model).reactTo(entersNumber(), entersText());
		Optional<Object> publishedEvent = modelRunner.reactTo(entersNumber(), entersNumber());
		assertFalse(publishedEvent.isPresent());
    }
}
