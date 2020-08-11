package org.requirementsascode;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
public abstract class Step extends ModelElement{
	private UseCase useCase;
	private AbstractActor[] actors;
	private Condition condition;
	private Class<?> messageClass;
	private SystemReaction<?> systemReaction;
	private AbstractActor publishTo;

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
	
  protected Predicate<ModelRunner> isConditionTrue() {
    Condition conditionOrElseTrue = getCondition().orElse(() -> true);
    Predicate<ModelRunner> flowCondition = toPredicate(conditionOrElseTrue);
    return flowCondition;
  }

	public AbstractActor[] getActors() {
		return actors;
	}

	public void setActors(AbstractActor[] actors) {
		connectActorsToThisStep(this, actors);
	}
	
	private void connectActorsToThisStep(Step useCaseStep, AbstractActor[] actors) {
		this.actors = actors;
		for (AbstractActor actor : actors) {
			actor.connectToStep(useCaseStep);
		}
	}

	public Class<?> getMessageClass() {
		return messageClass;
	}

	public void setMessageClass(Class<?> eventClass) {
		this.messageClass = eventClass;
	}

	public SystemReaction<?> getSystemReaction() {
		return systemReaction;
	}
	
	public void setSystemReaction(Runnable systemReaction) {
		this.systemReaction = new SystemReaction<>(systemReaction);
	}
	
	public <T> void setSystemReaction(Consumer<? super T> systemReaction) {
		this.systemReaction = new SystemReaction<>(systemReaction);
	}
	
	public <T> void setSystemReaction(Function<? super T, ?> systemReaction) {
		this.systemReaction = new SystemReaction<>(systemReaction);
	}

	public <T> void setSystemReaction(Supplier<? super T> systemReaction) {
		this.systemReaction = new SystemReaction<>(systemReaction);
	}

	protected static Predicate<ModelRunner> toPredicate(Condition condition) {
		return modelRunner -> condition.evaluate();
	}

	public Optional<AbstractActor> getPublishTo() {
		return Optional.ofNullable(publishTo);
	}

	public void setPublishTo(AbstractActor recipient) {
		this.publishTo = recipient;
	}
}
