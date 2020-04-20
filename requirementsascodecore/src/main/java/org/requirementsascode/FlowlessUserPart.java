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
	 * Defines the system reaction. The system will react as specified to the
	 * message passed in, when {@link ModelRunner#reactTo(Object)} is called.
	 *
	 * @param systemReaction the specified system reaction
	 * @return the created flowless system part
	 */
	public FlowlessSystemPart<T> system(Consumer<? super T> systemReaction) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReaction);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}
	
	/**
	 * Defines the system reaction. The system will react as specified to the
	 * message passed in, when you call {@link ModelRunner#reactTo(Object)}. After
	 * executing the system reaction, the runner will publish the returned events.
	 *
	 * @param systemReaction the specified system reaction, that returns an event to
	 *                       be published.
	 * @return the created flowless system part
	 */
	public FlowlessSystemPart<T> systemPublish(Function<? super T, Object> systemReaction) {
		StepSystemPart<T> stepSystemPart = stepUserPart.systemPublish(systemReaction);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}
	
	/**
	 * Defines the system reaction. The system will react as specified, but it will
	 * ignore the message passed in, when {@link ModelRunner#reactTo(Object)} is
	 * called.
	 *
	 * @param systemReaction the specified system reaction
	 * @return the created flowless system part
	 */
	public FlowlessSystemPart<T> system(Runnable systemReaction) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReaction);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}
}
