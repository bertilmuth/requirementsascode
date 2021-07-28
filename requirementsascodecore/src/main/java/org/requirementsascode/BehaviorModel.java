package org.requirementsascode;

/**
 * Implement this interface to specify a model that describes a behavior: which
 * messages are consumed, and which responses are produced. See See
 * <a href="https://github.com/bertilmuth/requirementsascode">the requirements
 * as code website</a> for examples.
 * 
 */
public interface BehaviorModel {
	/**
	 * A model that defines which messages are consumed and which responses are
	 * produced. See See
	 * <a href="https://github.com/bertilmuth/requirementsascode">the requirements
	 * as code website</a> for examples.
	 * 
	 * @return the behavior model
	 */
	Model model();

	/**
	 * The default response is the response that a behavior returns when a message
	 * is just consumed (via a <code>.system(...)</code> definition in the model),
	 * or when a handler function returns null.
	 * 
	 * @return the default response, null by default. Override this method to
	 *         provide a non-null default response.
	 */
	default Object defaultResponse() {
		return null;
	}
}
