package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

public class AdaptedSystemReactionTest extends AbstractTestCase {
    private String stepName;
    private Object event;

    @Before
    public void setup() {
	setupWith(new TestModelRunner());
    }

    @Test
    public void printsTextAndPerformsAdaptedSystemReaction() {
	modelRunner.adaptSystemReaction(withSavingStepNameAndEvent());
	stepName = "";

	Model model = 
		modelBuilder.useCase(USE_CASE)
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT)
					.system(displaysConstantText())
		.build();

	modelRunner.run(model);

	assertEquals(SYSTEM_DISPLAYS_TEXT, stepName);
	assertEquals(TestModelRunner.class, event.getClass());
    }

    private Consumer<SystemReactionTrigger> withSavingStepNameAndEvent() {
	return systemReactionTrigger -> {
	    stepName = systemReactionTrigger.getStep().getName();
	    event = systemReactionTrigger.getEvent();
	    systemReactionTrigger.trigger();
	};
    }
}
