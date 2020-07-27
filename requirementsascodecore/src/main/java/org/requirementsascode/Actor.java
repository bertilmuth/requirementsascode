package org.requirementsascode;

import java.util.Objects;

/**
 * An actor with dynamically attachable behavior.
 *
 * @author b_muth
 */
public class Actor extends AbstractActor{
	private Model behavior;
	
	/**
	 * Creates an actor with a name equal to the current class' simple name.
	 *
	 */
	public Actor() {
		super();
	}

	/**
	 * Creates an actor with the specified name.
	 *
	 * @param name  the name of the actor
	 */
	public Actor(String name) {
		super(name);
	}
	
	/**
	 * Attach the specified behavior to the actor.
	 * 
	 * @param behaviorModel the behvior the actor will show from now on
	 * 
	 * @return this actor
	 */
	public Actor withBehavior(Model behaviorModel) {
		this.behavior = Objects.requireNonNull(behaviorModel);
		createRunnerWithBehaviorIfPresent();
		return this;
	}
	
	@Override
	public Model behavior() {
		return behavior;
	}
}
