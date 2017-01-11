package org.requirementsascode;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

public class AdaptedSystemReactionTest extends AbstractTestCase{
	private static final String SAY_HELLO_USE_CASE = "Say Hello Use Case";
	private static final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	
	private boolean adaptedSystemReactionPerformed;
	
	@Before
	public void setup() {
		UseCaseRunner useCaseRunner = new UseCaseRunner(withAdaptedSystemReaction());
		setupWith(useCaseRunner);
		adaptedSystemReactionPerformed = false;
	}
	
	@Test
	public void shouldPrintTextAndPerformAdaptedSystemReaction() {		
		useCaseRunner.useCaseModel()
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());
		
		useCaseRunner.run();
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT), getRunStepNames());
		assertTrue(adaptedSystemReactionPerformed);
	}

	private Consumer<SystemReactionTrigger> withAdaptedSystemReaction() {
		return systemReactionTrigger -> {
			adaptedSystemReactionPerformed = true;
			systemReactionTrigger.trigger();
		};
	}
}
