package org.requirementsascode;

import java.util.function.Consumer;


/**
 * The part of the step that contains a reference to the actors
 * that are allowed to trigger a system reaction for this step.
 * 
 * @author b_muth
 *
 **/
public class UseCaseStepAs{
	private Actor[] actors;
	private UseCaseStep useCaseStep;
	
	public UseCaseStepAs(UseCaseStep useCaseStep, Actor... actor) {
		this.useCaseStep = useCaseStep;
		this.actors = actor;
		connectActorsToThisStep(actors);
		useCaseStep.setAs(this);
	}

	private void connectActorsToThisStep(Actor[] actors) {
		for (Actor actor : actors) {
			actor.newStep(useCaseStep);
		}
	}
	
	/**
	 * Defines the class of user event objects that this step accepts,
	 * so that they can cause a system reaction when the step's predicate is true.
	 * 
	 * Given that the step's predicate is true, the system reacts to objects that are
	 * instances of the specified class or instances of any direct or indirect subclass
	 * of the specified class.
	 * 
	 * Note: you must provide the event objects at runtime by calling {@link UseCaseRunner#reactTo(Object)}
	 * 
	 * @param eventClass the class of events the system reacts to in this step
	 * @param <T> the type of the class
	 * @return the created event part of this step
	 */
	public <T> UseCaseStepUser<T> user(Class<T> eventClass) {
		UseCaseStepUser<T> user = new UseCaseStepUser<>(useCaseStep, eventClass);
		useCaseStep.setUser(user);
		return user;
	}
	
	/**
	 * Defines an "autonomous system reaction",
	 * meaning the system will react without
	 * needing an event provided to the use case runner.
	 * 
	 * Instead of the model creator defining the event (via
	 * {@link #user(Class)}), the step implicitly handles the default
	 * system event. Default system events are raised by the 
	 * use case runner itself, causing "autonomous system reactions".
	 * 
	 * @param systemReaction the autonomous system reaction
	 * @return the created system part of this step
	 */
	public UseCaseStepSystem<UseCaseRunner> system(Consumer<UseCaseRunner> systemReaction) {
		UseCaseStepSystem<UseCaseRunner> system = 
			user(UseCaseRunner.class).system(systemReaction);
		return system;
	}
	
	/**
	 * Returns the actors that determine which user groups can 
	 * cause the system to react to the event of this step.  
	 * 
	 * @return the actors
	 */
	public Actor[] actors() {
		return actors;
	}
}