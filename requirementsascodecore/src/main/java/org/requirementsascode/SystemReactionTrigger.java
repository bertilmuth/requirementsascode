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
	private Step useCaseStep;
	
	SystemReactionTrigger() {
	}
	
	/**
	 * The system reaction of the step accepts the event 
	 * (both event and step passed in earlier).
	 * @see #setupWith(Object, Step)
	 */
	@SuppressWarnings("unchecked")
	public void trigger(){
		((Consumer<Object>)useCaseStep.getSystem()).accept(event);
	}
	
	void setupWith(Object event, Step useCaseStep) {
		this.event = event;
		this.useCaseStep = useCaseStep;
	}
	
	/**
	 * Returns the event object that will be passed to the system reaction when
	 * {@link #trigger()} is called.
	 * 
	 * @return the event object.
	 */
	public Object getEvent() {
		return event;
	}

	/**
	 * Returns the use case step whose system reaction is performed when
	 * {@link #trigger()} is called.
	 * 
	 * @return the use case step.
	 */
	public Step getUseCaseStep() {
		return useCaseStep;
	}
}
