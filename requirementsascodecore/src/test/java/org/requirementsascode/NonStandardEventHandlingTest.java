package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

public class NonStandardEventHandlingTest extends AbstractTestCase {
    private String stepName;
    private Optional<? extends Object> optionalCondition;
    private Optional<? extends Object> optionalEvent;
    private Object systemReaction;

    @Before
    public void setup() {
	setupWithRecordingModelRunner();
    }

    @Test
    public void recordsAutonomousSystemReactionStep() {
	stepName = "";

	ReactionAsRunnable reactionAsRunnable = new ReactionAsRunnable();
	Model model = modelBuilder.useCase(USE_CASE).basicFlow().step(SYSTEM_DISPLAYS_TEXT)
		.system(reactionAsRunnable).build();

	modelRunner.handleWith(recordStepDetails());
	modelRunner.run(model);

	assertRecordedStepNames(SYSTEM_DISPLAYS_TEXT);
	assertEquals(SYSTEM_DISPLAYS_TEXT, stepName);
	assertFalse(optionalEvent.isPresent());
	assertFalse(optionalCondition.isPresent());
	assertEquals(reactionAsRunnable, systemReaction);
    }

    private class ReactionAsRunnable implements Runnable {
	@Override
	public void run() {
	}
    }

    private Consumer<StandardEventHandler> recordStepDetails() {
	return standardEventHandler -> {
	    stepName = standardEventHandler.getStepName();
	    optionalCondition = standardEventHandler.getCondition();
	    optionalEvent = standardEventHandler.getEvent();
	    systemReaction = standardEventHandler.getSystemReaction();
	    standardEventHandler.handleEvent();
	};
    }

    @Test
    public void recordsStepWithEvent() {
	ReactionAsConsumer reactionAsConsumer = new ReactionAsConsumer();
	Model model = modelBuilder.useCase(USE_CASE).condition(new AlwaysTrue()).on(EntersText.class)
		.system(reactionAsConsumer).build();

	modelRunner.handleWith(recordStepDetails());
	modelRunner.run(model);
	modelRunner.reactTo(entersText());

	Object condition = optionalCondition.get();
	assertTrue(condition instanceof AlwaysTrue);

	Object event = optionalEvent.get();
	assertTrue(event instanceof EntersText);
	
	assertEquals(reactionAsConsumer, systemReaction);
    }

    private class AlwaysTrue implements Condition {
	@Override
	public boolean evaluate() {
	    return true;
	}
    }
    private class ReactionAsConsumer implements Consumer<EntersText> {
	@Override
	public void accept(EntersText t) {
	}
    }

    @Test
    public void recordsEventWithUnhandledEventHandler() {
	Model model = modelBuilder.useCase(USE_CASE).on(EntersText.class).system(displaysEnteredText()).build();

	modelRunner.handleUnhandledWith(this::eventRecordingEventHandler);
	modelRunner.run(model);
	modelRunner.reactTo(entersNumber());

	Object event = optionalEvent.get();
	assertTrue(event instanceof EntersNumber);
    }

    public void eventRecordingEventHandler(Object event) {
	this.optionalEvent = Optional.of(event);
    }
}
