package org.requirementsascode;

import java.util.Optional;

/**
 * A behavior occurs when reacting to a message, and optionally producing a
 * response. Which messages are reacted to and responses produced depends on the
 * behavior's model.
 * 
 * @author b_muth
 *
 */
public interface Behavior {
	/**
	 * Reacts to the specified message.
	 * 
	 * If the message's class is the same or a sub class of a class defined in the
	 * behavior model with <code>.user(...)</code> or <code>.on(...)</code>, the
	 * system will react with the function specified by <code>.system(...)</code> or
	 * <code>.systemPublish(...)</code>.
	 * 
	 * @param <T>     the type you expect (return value will be cast to it)
	 * @param message the incoming message
	 * @return either the optional result of a function, or an optional default
	 *         response, or an empty optional if both are not present
	 */
	<T> Optional<T> reactTo(Object message);

	/**
	 * Returns the behavior's model, that defines which message to react to and
	 * which responses to produce.
	 * 
	 * @return the behavior model
	 */
	BehaviorModel behaviorModel();
}
