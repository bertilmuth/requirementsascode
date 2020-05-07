package org.requirementsascode.builder;

import static org.requirementsascode.builder.FlowlessSystemPart.flowlessSystemPart;
import static org.requirementsascode.builder.FlowlessSystemPart.flowlessSystemPublishPart;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}. Wraps
 * {@link StepUserPart}.
 * 
 * @author b_muth
 */
public class FlowlessUserPart<T> {
	private StepUserPart<T> stepUserPart;
	private long flowlessStepCounter;

	private FlowlessUserPart(StepUserPart<T> stepUserPart, long flowlessStepCounter) {
		this.stepUserPart = Objects.requireNonNull(stepUserPart);
		this.flowlessStepCounter = flowlessStepCounter;
	}
	
	static <T> FlowlessUserPart<T> flowlessUserPart(StepUserPart<T> stepUserPart, long flowlessStepCounter) {
		return new FlowlessUserPart<>(stepUserPart, flowlessStepCounter);
	}
	
	/**
	 * Defines the system reaction. The system will react as specified to the
	 * message passed in, when {@link ModelRunner#reactTo(Object)} is called.
	 *
	 * @param systemReaction the specified system reaction
	 * @return the created flowless system part
	 */
	public FlowlessSystemPart<T> system(Consumer<? super T> systemReaction) {
		FlowlessSystemPart<T> flowlessSystemPart = flowlessSystemPart(stepUserPart, systemReaction,
			flowlessStepCounter);
		return flowlessSystemPart;
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
		FlowlessSystemPart<T> flowlessSystemPart = flowlessSystemPart(stepUserPart, systemReaction,
			flowlessStepCounter);
		return flowlessSystemPart;
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
	public FlowlessSystemPart<T> systemPublish(Function<? super T, ?> systemReaction) {
		FlowlessSystemPart<T> flowlessSystemPart = flowlessSystemPublishPart(stepUserPart, systemReaction,
			flowlessStepCounter);
		return flowlessSystemPart;
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
	public FlowlessSystemPart<T> systemPublish(Supplier<? super T> systemReaction) {
		FlowlessSystemPart<T> flowlessSystemPart = flowlessSystemPublishPart(stepUserPart, systemReaction,
			flowlessStepCounter);
		return flowlessSystemPart;
	}
}
