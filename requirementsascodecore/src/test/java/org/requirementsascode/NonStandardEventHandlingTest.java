package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

public class NonStandardEventHandlingTest extends AbstractTestCase {
    private String stepName;
    private Object event;

    @Before
    public void setup() {
	setupWith(new TestModelRunner());
    }

    @Test
    public void printsTextAndHandlesWithSavingStepNameAndEvent() {
	modelRunner.handleWith(savingStepNameAndEvent());
	stepName = "";

	Model model = 
		modelBuilder.useCase(USE_CASE)
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT)
					.system(displaysConstantText())
		.build();

	modelRunner.run(model);

	assertTrue(modelRunner.hasRun(SYSTEM_DISPLAYS_TEXT));
	assertEquals(SYSTEM_DISPLAYS_TEXT, stepName);
	assertEquals(TestModelRunner.class, event.getClass());
    }

    private Consumer<StandardEventHandler> savingStepNameAndEvent() {
	return standardEventHandler -> {
	    stepName = standardEventHandler.getStep().getName();
	    event = standardEventHandler.getEvent();
	    standardEventHandler.handleEvent();
	};
    }
    
    @Test
    public void reactsToUnhandledEvent() {
	Model model = modelBuilder.useCase(USE_CASE)
		.handles(EntersText.class).with(displaysEnteredText())
	.build();

	modelRunner.handleUnhandledWith(this::errorHandler); 
	modelRunner.run(model); 
	modelRunner.reactTo(entersNumber());
	
	assertTrue(event instanceof EntersNumber);
    }
    
    public void errorHandler(Object event) {
	this.event = event;
    }
}
