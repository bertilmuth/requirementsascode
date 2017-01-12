package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Test;

public class AdaptedSystemReactionTest extends AbstractTestCase{
	private static final String SAY_HELLO_USE_CASE = "Say Hello Use Case";
	private static final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	
	private String stepForWhichAdaptedSystemReactionHasBeenPerformed;
	private Object eventForWhichAdaptedSystemReactionHasBeenPerformed;
	
	@Test
	public void shouldPrintTextAndPerformAdaptedSystemReaction() {	
		UseCaseRunner useCaseRunner = new UseCaseRunner(withAdaptedSystemReaction());
		setupWith(useCaseRunner);
		stepForWhichAdaptedSystemReactionHasBeenPerformed = "";
		
		useCaseRunner.useCaseModel()
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());
		
		useCaseRunner.run();
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT), getRunStepNames());
		assertEquals(SYSTEM_DISPLAYS_TEXT, stepForWhichAdaptedSystemReactionHasBeenPerformed);
		assertEquals(SystemEvent.class, eventForWhichAdaptedSystemReactionHasBeenPerformed.getClass());
	}

	private Consumer<SystemReactionTrigger> withAdaptedSystemReaction() {
		return systemReactionTrigger -> {
			stepForWhichAdaptedSystemReactionHasBeenPerformed = 
					systemReactionTrigger.useCaseStep().name();
			eventForWhichAdaptedSystemReactionHasBeenPerformed = 
					systemReactionTrigger.event();
			systemReactionTrigger.trigger();
		};
	}
}
