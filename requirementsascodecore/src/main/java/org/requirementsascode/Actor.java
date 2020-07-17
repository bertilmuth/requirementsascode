package org.requirementsascode;

import java.io.Serializable;
import java.util.Objects;

/**
 * An actor is a role that a user plays. Actors represent different user groups.
 * They enable to distinguish user rights: only an actor that is connected to a
 * particular step is allowed to cause a system reaction for that step.
 *
 * @author b_muth
 */
public class Actor extends AbstractActor implements Serializable {
	private static final long serialVersionUID = 4581179263937622015L;
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
	
	public Actor withBehavior(Model behaviorModel) {
		this.behavior = Objects.requireNonNull(behaviorModel);
		return this;
	}
	
	public Model behavior() {
		return behavior;
	}
}
