package org.requirementsascode.builder;

import org.requirementsascode.Condition;
import org.requirementsascode.Model;
import org.requirementsascode.UseCase;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see UseCase
 * @author b_muth
 */
public class FlowlessUseCasePart {
  private UseCasePart useCasePart;
	
	public FlowlessUseCasePart(UseCasePart useCasePart) {
	  this.useCasePart = useCasePart;
  }

  static FlowlessUseCasePart useCasePart(UseCasePart useCasePart) {
    return new FlowlessUseCasePart(useCasePart);
	}


	/**
	 * Constrains the condition for triggering a system reaction: only if the
	 * specified condition is true, a system reaction can be triggered.
	 *
	 * @param condition the condition that constrains when the system reaction is
	 *                  triggered
	 * @return the created condition part
	 */
	public FlowlessConditionPart condition(Condition condition) {
		return useCasePart.condition(condition);
	}
	
	/**
	 * Creates a named step.
	 * 
	 * @param stepName the name of the created step
	 * @return the created step part
	 */
	public FlowlessStepPart step(String stepName) {
		return useCasePart.step(stepName);
	}

	/**
	 * Defines the type of commands that will cause a system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param commandClass the class of commands the system reacts to
	 * @param <T>          the type of the class
	 * @return the created user part
	 */
	public <T> FlowlessUserPart<T> user(Class<T> commandClass) {
    return useCasePart.user(commandClass);
	}

	/**
	 * Defines the type of events or exceptions that will cause a system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param eventOrExceptionClass the class of events the system reacts to
	 * @param <T>                   the type of the class
	 * @return the created user part
	 */
	public <T> FlowlessUserPart<T> on(Class<T> eventOrExceptionClass) {
    return useCasePart.on(eventOrExceptionClass);
	}
}
