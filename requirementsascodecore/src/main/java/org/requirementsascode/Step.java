package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A step is a part of a use case. The steps define the behavior of the use
 * case.
 *
 * <p>
 * A step is the core class of requirementsascode, providing all the necessary
 * configuration information to the {@link ModelRunner} to cause the system to
 * react to messages.
 *
 * @author b_muth
 */
public abstract class Step extends ModelElement implements Serializable {
	private static final long serialVersionUID = -2926490717985964131L;

	private UseCase useCase;
	private Actor[] actors;
	private Class<?> messageClass;
	private SystemReaction<?> systemReaction;
	private Condition condition;

	/**
	 * Creates a step with the specified name that belongs to the specified use
	 * case.
	 *
	 * @param useCase   the use case this step belongs to
	 * @param stepName  the name of the step to be created
	 * @param condition
	 */
	Step(String stepName, UseCase useCase, Condition condition) {
		super(stepName, useCase.getModel());
		this.useCase = useCase;
		this.condition = condition;
	}

	public abstract Predicate<ModelRunner> getPredicate();

	public UseCase getUseCase() {
		return useCase;
	}

	public Optional<Condition> getCondition() {
		return Optional.ofNullable(condition);
	}

	public Actor[] getActors() {
		return actors;
	}

	void setActors(Actor[] actors) {
		this.actors = actors;
	}

	public Class<?> getMessageClass() {
		return messageClass;
	}

	void setMessageClass(Class<?> eventClass) {
		this.messageClass = eventClass;
	}

	public SystemReaction<?> getSystemReaction() {
		return systemReaction;
	}

	void setSystemReaction(SystemReaction<?> systemReaction) {
		this.systemReaction = systemReaction;
	}

	protected static Predicate<ModelRunner> toPredicate(Condition condition) {
		return modelRunner -> condition.evaluate();
	}
}
