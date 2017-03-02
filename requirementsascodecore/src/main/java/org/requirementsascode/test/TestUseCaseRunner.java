package org.requirementsascode.test;

import java.util.function.Consumer;

import org.requirementsascode.SystemReactionTrigger;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;

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
		String trackedStepName = "";
		if(useCaseStep.name().startsWith("S")){
			trackedStepName = useCaseStep.name() + ";";
		}
		return trackedStepName;
	}
}
