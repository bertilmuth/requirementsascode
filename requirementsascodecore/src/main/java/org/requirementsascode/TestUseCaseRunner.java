package org.requirementsascode;

import java.util.function.Consumer;

/**
 * Simple use case runner for automated tests.
 * 
 * @author b_muth
 *
 */
public class TestUseCaseRunner extends UseCaseRunner{
	private String runStepNames;

	public TestUseCaseRunner() {
		runStepNames = "";
		adaptSystemReaction(withStepNameTracking());
	}
	
	/**
	 * Returns the names of the use case steps, separated with a semicolon
	 * after each step, without spaces.
	 * 
	 * If no step has been run, an empty step is returned.
	 * If one step S1 has been run, the string S1; is returned.
	 * If two steps, S1 and S2 have been run, the string S1;S2; is returned.
	 * 
	 * @return the step names
	 */
	public String runStepNames() {
		return runStepNames;
	}
	
	private Consumer<SystemReactionTrigger> withStepNameTracking() {
		return systemReactionTrigger -> {
			runStepNames += trackStepName(systemReactionTrigger.useCaseStep());
			systemReactionTrigger.trigger();
		};
	}
	private String trackStepName(UseCaseStep useCaseStep) {
		String trackedStepName = useCaseStep.name();
		
		if(useCaseStep.name().endsWith(UseCaseStep.REPEAT_STEP_POSTFIX)){
			trackedStepName = repeatStepNameWithoutPostfix(trackedStepName);
		} else if(useCaseStep.name().endsWith(UseCaseStep.NEXT_LOOP_ITERATION_STEP_POSTFIX)){
			trackedStepName = "";
		} else {
			trackedStepName = trackedStepName + ";";
		}
		return trackedStepName;
	}

	private String repeatStepNameWithoutPostfix(String trackedStepName) {
		int repeatIndex = trackedStepName.indexOf(UseCaseStep.REPEAT_STEP_POSTFIX);
		trackedStepName = trackedStepName.substring(0, repeatIndex) + ";";
		return trackedStepName;
	}
}
