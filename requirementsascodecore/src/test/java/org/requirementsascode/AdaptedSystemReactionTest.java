package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

public class AdaptedSystemReactionTest extends AbstractTestCase{
	private String stepName;
	private Object event;
	
	@Before
	public void setup() {
		setupWith(new TestUseCaseModelRunner());
	}
	
	@Test
	public void printsTextAndPerformsAdaptedSystemReaction() {	
		useCaseRunner.adaptSystemReaction(withSavingStepNameAndEvent());
		stepName = "";
		
		UseCaseModel useCaseModel = useCaseModelBuilder
			.useCase(USE_CASE)
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
			.build();
		
		useCaseRunner.run(useCaseModel);
		
		assertEquals(SYSTEM_DISPLAYS_TEXT, stepName);
		assertEquals(TestUseCaseModelRunner.class, event.getClass());
	}

	private Consumer<SystemReactionTrigger> withSavingStepNameAndEvent() {
		return systemReactionTrigger -> {
			stepName = systemReactionTrigger.getUseCaseStep().name();
			event = systemReactionTrigger.getEvent();
			systemReactionTrigger.trigger();
		};
	}
}
