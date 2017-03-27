package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;


/**
 * The part of the step that contains a reference to the event
 * that is allowed to trigger a system reaction for this step.
 * 
 * @author b_muth
 *
 */
public class UseCaseStepUser<T>{
	private UseCaseStep useCaseStep;
	private Class<T> eventClass;
	
	UseCaseStepUser(UseCaseStep useCaseStep, Class<T> eventClass) {
		this.useCaseStep = useCaseStep;
		this.eventClass = eventClass;
	}
	
	/**
	 * Defines the system reaction. 
	 * The system will react as specified to the current step's events,
	 * via {@link UseCaseRunner#reactTo(Object)}.
	 * 
	 * @param systemReaction the specified system reaction
	 * @return the created system part of this step 
	 */
	public <U> UseCaseStepSystem<T> system(Consumer<T> systemReaction) {
		Objects.requireNonNull(systemReaction);
		
		UseCaseStepSystem<T> newSystemPart = 
			new UseCaseStepSystem<>(useCaseStep, systemReaction);
		useCaseStep.setSystem(newSystemPart);
		return newSystemPart;		
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
