package org.requirementsascode;

import java.util.function.Consumer;

/**
 * Class used to trigger a system reaction.
 * 
 * Use an instance of this class only if you want to 
 * adapt the system reaction, in order to call the standard 
 * system reaction from your adapted system reaction.
 * 
 * @author b_muth
 *
 */
public class SystemReactionTrigger {
	private Object event;
	private UseCaseStep useCaseStep;
	
	SystemReactionTrigger() {
	}
	
	/**
	 * The system reaction of the step accepts the event 
	 * (both event and step passed in earlier).
	 * @see #setupWith(Object, UseCaseStep)
	 */
	@SuppressWarnings("unchecked")
	public void trigger(){
		((Consumer<Object>)useCaseStep.getSystemReaction()).accept(event);
	}
	
	void setupWith(Object event, UseCaseStep useCaseStep) {
		this.event = event;
		this.useCaseStep = useCaseStep;
	}
	
	/**
	 * Returns the event object that will be passed to the system reaction when
	 * {@link #trigger()} is called.
	 * 
	 * @return the event object.
	 */
	public Object event() {
		return event;
	}

	/**
	 * Returns the use case step whose system reaction is performed when
	 * {@link #trigger()} is called.
	 * 
	 * @return the use case step.
	 */
	public UseCaseStep useCaseStep() {
		return useCaseStep;
	}
}
