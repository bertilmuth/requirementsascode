package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class CanReactToTest extends AbstractTestCase {

    @Before
    public void setup() {
	setupWithRecordingModelRunner();
    }

    @Test
    public void cantReactIfNotRunning() {
		boolean canReact = modelRunner.canReactTo(entersText().getClass());
		assertFalse(canReact);
    }

    @Test
    public void cantReactIfEventIsWrong() {
		Model model = modelBuilder.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
	
		modelRunner.run(model);
	
		boolean canReact = modelRunner.canReactTo(entersNumber().getClass());
		assertFalse(canReact);
    }
    
    @Test
    public void cantReactIfConditionIsWrong() {
		Model model = modelBuilder.useCase(USE_CASE)
			.basicFlow().condition(() -> false)
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText()).build();
	
		modelRunner.run(model);
	
		boolean canReact = modelRunner.canReactTo(entersText().getClass());
		assertFalse(canReact);
	}

    @Test
    public void oneStepCanReactIfEventIsRight() {
		Model model = modelBuilder.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText()).build();
	
		modelRunner.run(model);
	
		boolean canReact = modelRunner.canReactTo(entersText().getClass());
		assertTrue(canReact);
	
		Set<Step> stepsThatCanReact = modelRunner.getStepsThatCanReactTo(entersText().getClass());
		assertEquals(1, stepsThatCanReact.size());
		assertEquals(CUSTOMER_ENTERS_TEXT, stepsThatCanReact.iterator().next().getName().toString());
    }

    @Test
    public void moreThanOneStepCanReact() {
	Model model = modelBuilder.useCase(USE_CASE)
		.basicFlow().anytime()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.flow("Alternative Flow: Could react as well").anytime()
			.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();

	modelRunner.run(model);

	boolean canReact = modelRunner.canReactTo(entersText().getClass());
	assertTrue(canReact);

	Set<Step> stepsThatCanReact = modelRunner.getStepsThatCanReactTo(entersText().getClass());
	assertEquals(2, stepsThatCanReact.size());
    }
}
