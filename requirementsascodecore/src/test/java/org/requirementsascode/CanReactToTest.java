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

		boolean canReact = modelRunner.canReactTo(EntersNumber.class);
		assertFalse(canReact);
	}

	@Test
	public void cantReactIfConditionIsWrong() {
		Model model = modelBuilder.useCase(USE_CASE)
			.basicFlow().condition(() -> false)
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();

		modelRunner.run(model);

		boolean canReact = modelRunner.canReactTo(EntersText.class);
		assertFalse(canReact);
	}

	@Test
	public void oneStepCanReactIfEventIsRight() {
		Model model = modelBuilder.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();

		modelRunner.run(model);

		boolean canReact = modelRunner.canReactTo(EntersText.class);
		assertTrue(canReact);

		Set<Step> stepsThatCanReact = modelRunner.getStepsThatCanReactTo(EntersText.class);
		assertEquals(1, stepsThatCanReact.size());
		assertEquals(CUSTOMER_ENTERS_TEXT, stepsThatCanReact.iterator().next().getName());
	}

	@Test
	public void oneStepCanReactIfEventIsSubClass() {
		Model model = modelBuilder.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.build();

		modelRunner.run(model);

		boolean canReact = modelRunner.canReactTo(EntersTextSubClass.class);
		assertTrue(canReact);

		Set<Step> stepsThatCanReact = modelRunner.getStepsThatCanReactTo(EntersText.class);
		assertEquals(1, stepsThatCanReact.size());
		assertEquals(CUSTOMER_ENTERS_TEXT, stepsThatCanReact.iterator().next().getName());
	}

	private class EntersTextSubClass extends EntersText {
		public EntersTextSubClass(String text) {
			super(text);
		}
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

		boolean canReact = modelRunner.canReactTo(EntersText.class);
		assertTrue(canReact);

		Set<Step> stepsThatCanReact = modelRunner.getStepsThatCanReactTo(EntersText.class);
		assertEquals(2, stepsThatCanReact.size());
	}
}
