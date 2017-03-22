package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import org.junit.Test;

public class AdaptedSystemReactionTest extends AbstractTestCase{
	private String stepName;
	private Object event;
	
	@Test
	public void printsTextAndPerformsAdaptedSystemReaction() {	
		TestUseCaseRunner useCaseRunner = new TestUseCaseRunner();
		useCaseRunner.adaptSystemReaction(withSavingStepNameAndEvent());
		
		setupWith(useCaseRunner);
		stepName = "";
		
		useCaseRunner.useCaseModel()
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());
		
		useCaseRunner.run();
		
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
