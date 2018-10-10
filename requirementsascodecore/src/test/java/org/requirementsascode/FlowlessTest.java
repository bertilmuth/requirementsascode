package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowlessTest extends AbstractTestCase {

    @Before
    public void setUp() throws Exception {
	setupWith(new TestModelRunner());
    }

    @Test
    public void oneFlowlessStepReacts() {
	Model model = modelBuilder.useCase(USE_CASE)
		.on(EntersText.class).system(displaysEnteredText())
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
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithoutEvent() {
	Model model = modelBuilder.useCase(USE_CASE)
		.condition(() -> !modelRunner.getLatestStep().isPresent()).system(displaysConstantText())
		.on(EntersNumber.class).system(displaysEnteredNumber())
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
    public void oneFlowlessStepReactsWithoutUseCase() {
	Model model = modelBuilder
		.on(EntersText.class).system(displaysEnteredText())
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
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithoutUseCase() {
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
}
