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

	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing a message provided via {@link ModelRunner#reactTo(Object)}.
	 *
	 * @param systemReaction the autonomous system reaction
	 * @return the created system part of this step
	 */
	public FlowlessSystemPart<T> system(Runnable systemReaction) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReaction);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}

	/**
	 * Defines the system reaction. The system will react as specified, when
	 * {@link ModelRunner#reactTo(Object)} is called.
	 *
	 * @param systemReaction the specified system reaction
	 * @return the created system part of this step
	 */
	public FlowlessSystemPart<T> system(Consumer<T> systemReaction) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReaction);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}

	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing a message provided via {@link ModelRunner#reactTo(Object)}.
	 * After executing the system reaction, the runner will publish the returned
	 * event.
	 *
	 * @param systemReaction the autonomous system reaction, that returns an event to
	 *                       be published.
	 * @return the created system part of this step
	 */
	public FlowlessSystemPart<T> systemPublish(Function<T, Object> systemReaction) {
		StepSystemPart<T> stepSystemPart = stepUserPart.systemPublish(systemReaction);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}
}
