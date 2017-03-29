package org.requirementsascode;

/**
 * The part of the step that contains a reference to the event
 * that is allowed to trigger a system reaction for this step.
 * 
 * @author b_muth
 *
 */
public class UseCaseStepUser<T>{
	private Class<T> eventClass;
	
	public UseCaseStepUser(UseCaseStep useCaseStep, Class<T> eventClass) {
		this.eventClass = eventClass;
		useCaseStep.setUser(this);
	}
	
	/**
	 * Returns the class of event or exception objects that the system reacts to in this step.
	 * The system reacts to objects that are instances of the returned class or 
	 * instances of any direct or indirect subclass of the returned class.
	 * 
	 * @return the class of events the system reacts to in this step
	 */
	public Class<T> eventClass() {
		return eventClass;
	}
}
