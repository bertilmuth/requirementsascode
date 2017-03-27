package org.requirementsascode.builder;

import static org.junit.Assert.assertEquals;
import static org.requirementsascode.testutil.Names.SYSTEM_DISPLAYS_TEXT;
import static org.requirementsascode.testutil.Names.USE_CASE;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.SystemReactionTrigger;
import org.requirementsascode.TestUseCaseRunner;
import org.requirementsascode.UseCaseModel;

public class AdaptedSystemReactionTest extends AbstractTestCase{
	private String stepName;
	private Object event;
	
	@Before
	public void setup() {
		setupWith(new TestUseCaseRunner());
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
		assertEquals(TestUseCaseRunner.class, event.getClass());
	}

	private Consumer<SystemReactionTrigger> withSavingStepNameAndEvent() {
		return systemReactionTrigger -> {
			stepName = systemReactionTrigger.useCaseStep().name();
			event = systemReactionTrigger.event();
			systemReactionTrigger.trigger();
		};
	}
}
