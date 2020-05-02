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
    public void withUseCase_oneStepReactsToEvent() {
			Model model = modelBuilder.useCase(USE_CASE)
				.on(EntersText.class).system(displaysEnteredText())
			.build();
		
			modelRunner.run(model).reactTo(entersText());
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
		
			assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    
    @Test
    public void withUseCase_oneStepReactsToCommand() {
			Model model = modelBuilder.useCase(USE_CASE)
				.user(EntersText.class).system(displaysEnteredText())
			.build();
		
			modelRunner.run(model).reactTo(entersText());
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
		
			assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }

    @Test
    public void withUseCase_twoStepsReactToEventsOfDifferentTypeInRightOrder() {
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
    public void withUseCase_twoStepsReactToCommandsOfDifferentTypeInRightOrder() {
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
    public void withUseCase_twoStepsReactToEventsOfDifferentTypeInWrongOrder() {
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
    public void withUseCase_twoStepsReactToCommandsOfDifferentTypeInWrongOrder() {
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
    public void withUseCase_twoStepsReactToEventsOfDifferentTypeInDifferentUseCases() {
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
    public void withUseCase_twoStepsReactToCommandsOfDifferentTypeInDifferentUseCases() {
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
    public void withUseCase_twoStepsReactWhenConditionIsTrueInFirstStepWithEvent() {
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
    public void withUseCase_twoStepsReactWhenConditionIsTrueInFirstStepWithCommand() {
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
    public void withUseCase_twoStepsReactWhenConditionIsTrueInFirstStepWithoutEventAndSecondStepWithEvent() {
			Model model = modelBuilder.useCase(USE_CASE)
				.condition(() -> !modelRunner.getLatestStep().isPresent()).system(displaysConstantText())
				.on(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
			modelRunner.run(model).reactTo(entersNumber());
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
			assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void withUseCase_twoStepsReactWhenConditionIsTrueInFirstStepWithoutEventAndSecondStepWithCommand() {
			Model model = modelBuilder.useCase(USE_CASE)
				.condition(() -> !modelRunner.getLatestStep().isPresent()).system(displaysConstantText())
				.user(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
			modelRunner.run(model).reactTo(entersNumber());
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
			assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void withUseCase_twoStepsReactWhenConditionIsTrueInFirstStepWithoutEventButWithModelRunnerArgument() {
			Model model = modelBuilder.useCase(USE_CASE)
				.condition(() -> !modelRunner.getLatestStep().isPresent()).system(modelRunner -> displaysConstantText())
				.on(EntersNumber.class).system(displaysEnteredNumber())
			.build();
		
			modelRunner.run(model).reactTo(entersNumber());
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
			assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void withUseCase_twoStepsReactWhenConditionIsTrueInSecondStepWithEvent() {
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
    public void withUseCase_twoStepsReactWhenConditionIsTrueInSecondStepWithoutEvent() {
			Model model = modelBuilder.useCase(USE_CASE)
				.on(EntersText.class).system(displaysEnteredText())
				.condition(() -> modelRunner.getLatestStep().isPresent() && modelRunner.getLatestStep().get().getMessageClass().equals(EntersText.class)).system(displaysConstantText())
			.build();
		
			modelRunner.run(model).reactTo(entersText());
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
			assertEquals(ModelRunner.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void withUseCase_twoStepsDontReactWhenConditionIsFalseInFirstStep() {
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
    public void withUseCase_twoStepsDontReactWhenConditionIsFalseInSecondStep() {
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
    public void withUseCase_publishEnteredTextAsStringWithOnInFirstStep() {
			Model model = modelBuilder.useCase(USE_CASE)
				.on(EntersText.class).systemPublish(super.publishEnteredTextAsString())
				.on(String.class).system(new IgnoresIt<>())
			.build();
		
			Optional<String> optionalActualText = modelRunner.run(model).reactTo(entersText());
			String actualText = optionalActualText.get();
			assertEquals(TEXT, actualText);
    }
    
    @Test
    public void withUseCase_publishEnteredTextAsStringWithUserInFirstStep() {
			Model model = modelBuilder.useCase(USE_CASE)
				.user(EntersText.class).systemPublish(super.publishEnteredTextAsString())
				.on(String.class).system(new IgnoresIt<>())
			.build();
		
			Optional<String> optionalActualText = modelRunner.run(model).reactTo(entersText());
			String actualText = optionalActualText.get();
			assertEquals(TEXT, actualText);
    }
    
    @Test
    public void withUseCase_publishIntegerInsteadOfEnteredText() {
	    final Integer EXPECTED_INTEGER = 1234;
	    	
			Model model = modelBuilder.useCase(USE_CASE)
				.user(EntersText.class).systemPublish(super.publishEnteredTextAsString())
				.on(String.class).systemPublish(text ->  EXPECTED_INTEGER)
			.build();
		
			Optional<Integer> optionalInteger = modelRunner.run(model).reactTo(entersText());
			Integer actualInteger = optionalInteger.get();
			assertEquals(EXPECTED_INTEGER, actualInteger);
    }
    
    @Test
    public void withUseCase_publishEnteredTextAsStringAfterDifferentEvent() {
			Model model = modelBuilder.useCase(USE_CASE)
				.user(EntersText.class).systemPublish(super.publishEnteredTextAsString())
				.on(EntersNumber.class).system(new IgnoresIt<>())
			.build();
		
			Optional<String> optionalActualText = modelRunner.run(model).reactTo(entersText());
			String actualText = optionalActualText.get();
			assertEquals(TEXT, actualText);
    }
    
    @Test
    public void withUseCase_publishConstantTextAsStringWithConditionAndOnInSecondStep() {
			Model model = modelBuilder.useCase(USE_CASE)
				.condition(() -> !modelRunner.getLatestStep().isPresent()).systemPublish(publishConstantTextAsString())
				.on(String.class).system(new IgnoresIt<>())
			.build();
		
			modelRunner.run(model);
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
			assertEquals(String.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void withUseCase_publishConstantTextAsStringWithConditionAndUserInSecondStep() {
			Model model = modelBuilder.useCase(USE_CASE)
				.condition(() -> !modelRunner.getLatestStep().isPresent()).systemPublish(publishConstantTextAsString())
				.user(String.class).system(new IgnoresIt<>())
			.build();
		
			modelRunner.run(model);
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
			assertEquals(String.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void withUseCase_dontPublishConstantTextAsStringWhenConditionFalse() {
			Model model = modelBuilder.useCase(USE_CASE)
				.condition(() -> modelRunner.getLatestStep().isPresent()).systemPublish(publishConstantTextAsString())
				.on(String.class).system(new IgnoresIt<>())
			.build();
		
			Optional<Object> optionalEventObject = modelRunner.run(model).reactTo(entersNumber());
			assertFalse(optionalEventObject.isPresent());
    }
    
    @Test
    public void withUseCase_dontPublishFirstEnteredTextWhenReactToIsCalledTheSecondTime() {
			Model model = modelBuilder.useCase(USE_CASE)
				.user(EntersText.class).systemPublish(super.publishEnteredTextAsString())
				.on(String.class).system(new IgnoresIt<>())
			.build();
		
			modelRunner.run(model).reactTo(entersText());
			Optional<Object> publishedEvent = modelRunner.reactTo(TEXT);
			assertFalse(publishedEvent.isPresent());
    }
    
    @Test
    public void withUseCase_dontPublishOneOfTheEnteredTextsWhenReactToIsCalledTheSecondTime() {
			Model model = modelBuilder.useCase(USE_CASE)
				.user(EntersText.class).systemPublish(super.publishEnteredTextAsString())
				.on(String.class).system(new IgnoresIt<>())
			.build();
		
			modelRunner.run(model).reactTo(entersNumber(), entersText());
			Optional<Object> publishedEvent = modelRunner.reactTo(entersNumber(), entersNumber());
			assertFalse(publishedEvent.isPresent());
    }
    
    @Test
    public void noUseCase_oneStepReactsToEvent() {
			Model model = modelBuilder
				.on(EntersText.class).system(displaysEnteredText())
			.build();
		
			modelRunner.run(model).reactTo(entersText());
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
			assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }
    
    @Test
    public void noUseCase_oneStepReactsToCommand() {
			Model model = modelBuilder
				.user(EntersText.class).system(displaysEnteredText())
			.build();
		
			modelRunner.run(model).reactTo(entersText());
			Optional<Step> latestStepRun = modelRunner.getLatestStep();
			assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
    }

    @Test
    public void noUseCase_twoStepsReactToEventsOfDifferentTypeInRightOrder() {
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
    public void noUseCase_twoStepsReactToCommandsOfDifferentTypeInRightOrder() {
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
    public void noUseCase_twoStepsReactToEventsOfDifferentTypeInWrongOrder() {
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
    public void noUseCase_twoStepsReactToCommandsOfDifferentTypeInWrongOrder() {
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
    public void noUseCase_twoStepsReactToEventWhenConditionIsTrueInFirstStep() {
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
    public void noUseCase_twoStepsReactToCommandWhenConditionIsTrueInFirstStep() {
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
    public void noUseCase_twoStepsReactWhenConditionIsTrueInSecondStep() {
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
    public void noUseCase_twoStepsDontReactWhenConditionIsFalseInFirstStep() {
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
    public void noUseCase_twoStepsDontReactWhenConditionIsFalseInSecondStep() {
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
}
