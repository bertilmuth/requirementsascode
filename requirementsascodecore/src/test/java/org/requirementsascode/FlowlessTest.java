package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStepWithEvent() {
	Model model = modelBuilder.useCase(USE_CASE)
		.when(r -> true).on(EntersText.class).system(displaysEnteredText())
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
		.when(r -> !r.getLatestStep().isPresent()).system(displaysConstantText())
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
		.when(r -> true).on(EntersNumber.class).system(displaysEnteredNumber())
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
		.when(r -> r.getLatestStep().isPresent() && r.getLatestStep().get().getEventClass().equals(EntersText.class)).system(displaysConstantText())
	.build();

	modelRunner.run(model);

	modelRunner.reactTo(entersText());
	assertEquals(ModelRunner.class, modelRunner.getLatestStep().get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInFirstStep() {
	Model model = modelBuilder.useCase(USE_CASE)
		.when(r -> false).on(EntersText.class).system(displaysEnteredText())
		.on(EntersNumber.class).system(displaysEnteredNumber())
	.build();

	modelRunner.run(model);

	Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
	assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());

	latestStepRun = modelRunner.reactTo(entersText());
	assertFalse(latestStepRun.isPresent());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInSecondStep() {
	Model model = modelBuilder.useCase(USE_CASE)
		.on(EntersText.class).system(displaysEnteredText())
		.when(r -> false).on(EntersNumber.class).system(displaysEnteredNumber())
	.build();

	modelRunner.run(model);

	Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
	assertEquals(EntersText.class, latestStepRun.get().getEventClass());

	latestStepRun = modelRunner.reactTo(entersNumber());
	assertFalse(latestStepRun.isPresent());
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
		.when(r -> true).on(EntersText.class).system(displaysEnteredText())
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
		.when(r -> true).on(EntersNumber.class).system(displaysEnteredNumber())
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
		.when(r -> false).on(EntersText.class).system(displaysEnteredText())
		.on(EntersNumber.class).system(displaysEnteredNumber())
	.build();

	modelRunner.run(model);

	Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
	assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());

	latestStepRun = modelRunner.reactTo(entersText());
	assertFalse(latestStepRun.isPresent());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInSecondStepWithoutUseCase() {
	Model model = modelBuilder
		.on(EntersText.class).system(displaysEnteredText())
		.when(r -> false).on(EntersNumber.class).system(displaysEnteredNumber())
	.build();

	modelRunner.run(model);

	Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
	assertEquals(EntersText.class, latestStepRun.get().getEventClass());

	latestStepRun = modelRunner.reactTo(entersNumber());
	assertFalse(latestStepRun.isPresent());
    }
}
