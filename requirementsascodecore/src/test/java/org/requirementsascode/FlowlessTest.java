package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowlessTest extends AbstractTestCase {

    @Before
    public void setUp() throws Exception {
	setupWith(new TestUseCaseModelRunner());

    }

    @Test
    public void oneFlowlessStepReacts() {
	UseCaseModel useCaseModel = useCaseModelBuilder.useCase(USE_CASE).handles(EntersText.class)
		.system(displaysEnteredText()).build();

	useCaseModelRunner.run(useCaseModel);
	Optional<Step> latestStepRun = useCaseModelRunner.reactTo(entersText());

	assertEquals(EntersText.class, latestStepRun.get().getUserEventClass());
    }

    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInRightOrder() {
	UseCaseModel useCaseModel = useCaseModelBuilder.useCase(USE_CASE)
		.handles(EntersText.class).system(displaysEnteredText())
		.handles(EntersNumber.class).system(displaysEnteredNumber())
	.build();

	useCaseModelRunner.run(useCaseModel);

	Optional<Step> latestStepRun = useCaseModelRunner.reactTo(entersText());
	assertEquals(EntersText.class, latestStepRun.get().getUserEventClass());

	latestStepRun = useCaseModelRunner.reactTo(entersNumber());
	assertEquals(EntersNumber.class, latestStepRun.get().getUserEventClass());
    }
    
    @Test
    public void twoFlowlessStepsReactToEventsOfDifferentTypeInWrongOrder() {
	UseCaseModel useCaseModel = useCaseModelBuilder.useCase(USE_CASE)
		.handles(EntersText.class).system(displaysEnteredText())
		.handles(EntersNumber.class).system(displaysEnteredNumber())
	.build();

	useCaseModelRunner.run(useCaseModel);

	Optional<Step> latestStepRun = useCaseModelRunner.reactTo(entersNumber());
	assertEquals(EntersNumber.class, latestStepRun.get().getUserEventClass());

	latestStepRun = useCaseModelRunner.reactTo(entersText());
	assertEquals(EntersText.class, latestStepRun.get().getUserEventClass());
    }
}
