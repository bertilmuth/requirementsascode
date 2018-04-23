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
		.handles(EntersText.class).with(displaysEnteredText())
	.build();

	modelRunner.run(model);
	Optional<Step> latestStepRun = modelRunner.reactTo(entersText());

	assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }

    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInRightOrder() {
	Model model = modelBuilder.useCase(USE_CASE)
		.handles(EntersText.class).with(displaysEnteredText())
		.handles(EntersNumber.class).with(displaysEnteredNumber())
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
		.handles(EntersText.class).with(displaysEnteredText())
		.handles(EntersNumber.class).with(displaysEnteredNumber())
	.build();

	modelRunner.run(model);

	Optional<Step> latestStepRun = modelRunner.reactTo(entersNumber());
	assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());

	latestStepRun = modelRunner.reactTo(entersText());
	assertEquals(EntersText.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInFirstStep() {
	Model model = modelBuilder.useCase(USE_CASE)
		.when(r -> true).handles(EntersText.class).with(displaysEnteredText())
		.handles(EntersNumber.class).with(displaysEnteredNumber())
	.build();

	modelRunner.run(model);
	
	Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
	assertEquals(EntersText.class, latestStepRun.get().getEventClass());

	latestStepRun = modelRunner.reactTo(entersNumber());
	assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactWhenConditionIsTrueInSecondStep() {
	Model model = modelBuilder.useCase(USE_CASE)
		.handles(EntersText.class).with(displaysEnteredText())
		.when(r -> true).handles(EntersNumber.class).with(displaysEnteredNumber())
	.build();

	modelRunner.run(model);

	Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
	assertEquals(EntersText.class, latestStepRun.get().getEventClass());

	latestStepRun = modelRunner.reactTo(entersNumber());
	assertEquals(EntersNumber.class, latestStepRun.get().getEventClass());
    }
    
    @Test
    public void twoFlowlessStepsDontReactWhenConditionIsFalseInFirstStep() {
	Model model = modelBuilder.useCase(USE_CASE)
		.when(r -> false).handles(EntersText.class).with(displaysEnteredText())
		.handles(EntersNumber.class).with(displaysEnteredNumber())
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
		.handles(EntersText.class).with(displaysEnteredText())
		.when(r -> false).handles(EntersNumber.class).with(displaysEnteredNumber())
	.build();

	modelRunner.run(model);

	Optional<Step> latestStepRun = modelRunner.reactTo(entersText());
	assertEquals(EntersText.class, latestStepRun.get().getEventClass());

	latestStepRun = modelRunner.reactTo(entersNumber());
	assertFalse(latestStepRun.isPresent());
    }
}
