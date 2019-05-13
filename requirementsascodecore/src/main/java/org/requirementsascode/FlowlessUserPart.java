package org.requirementsascode;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}. Wraps
 * {@link StepUserPart}.
 * 
 * @author b_muth
 */
public class FlowlessUserPart<T> {
	private StepUserPart<T> stepUserPart;
	private long flowlessStepCounter;

	FlowlessUserPart(StepUserPart<T> stepUserPart, long flowlessStepCounter) {
		this.stepUserPart = stepUserPart;
		this.flowlessStepCounter = flowlessStepCounter;
	}

	public FlowlessSystemPart<T> system(Runnable systemReactionObject) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReactionObject);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}

	public FlowlessSystemPart<T> system(Consumer<T> systemReactionObject) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReactionObject);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}

	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing an event provided via {@link ModelRunner#reactTo(Object)}.
	 * After executing the system reaction, the runner will publish the returned
	 * events.
	 *
	 * @param systemReaction the autonomous system reaction, that returns events to
	 *                       be published.
	 * @return the created system part of this step
	 */
	public FlowlessSystemPart<T> systemPublish(Function<T, Object[]> systemReaction) {
		StepSystemPart<T> stepSystemPart = stepUserPart.systemPublish(systemReaction);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}
}
