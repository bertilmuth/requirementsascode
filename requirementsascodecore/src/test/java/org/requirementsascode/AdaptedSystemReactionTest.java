package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Test;

public class AdaptedSystemReactionTest extends AbstractTestCase{
	private static final String SAY_HELLO_USE_CASE = "Say Hello Use Case";
	private static final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	
	private String stepName;
	private Object event;
	
	@Test
	public void printsTextAndPerformsAdaptedSystemReaction() {	
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		useCaseRunner.adaptSystemReaction(withSavingStepNameAndEvent());
		
		setupWith(useCaseRunner);
		stepName = "";
		
		useCaseRunner.useCaseModel()
			.useCase(SAY_HELLO_USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());
		
		useCaseRunner.run();
		
		assertEquals(Arrays.asList(SYSTEM_DISPLAYS_TEXT), getRunStepNames());
		assertEquals(SYSTEM_DISPLAYS_TEXT, stepName);
		assertEquals(SystemEvent.class, event.getClass());
	}

	private Consumer<SystemReactionTrigger> withSavingStepNameAndEvent() {
		return systemReactionTrigger -> {
			stepName = systemReactionTrigger.useCaseStep().name();
			event = systemReactionTrigger.event();
			systemReactionTrigger.trigger();
		};
	}
}
