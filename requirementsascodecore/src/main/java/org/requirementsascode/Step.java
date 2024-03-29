package org.requirementsascode;

import java.util.Arrays;
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
	private Behavior publishTo;
  private Condition aCase;

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
		final AbstractActor[] actorsCopy = Arrays.copyOf(actors, actors.length);
    return actorsCopy;
	}

	public void setActors(AbstractActor[] actors) {
    this.actors = Arrays.copyOf(actors, actors.length);
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

	public <T> void setSystemReaction(Supplier<?> systemReaction) {
		this.systemReaction = new SystemReaction<>(systemReaction);
	}

	protected static Predicate<ModelRunner> toPredicate(Condition condition) {
		return modelRunner -> condition.evaluate();
	}

	public Optional<Behavior> getPublishTo() {
		return Optional.ofNullable(publishTo);
	}

	public void setPublishTo(Behavior recipient) {
		this.publishTo = recipient;
	}

  public void setCase(Condition aCase) {
    this.aCase = aCase;
  }
  
  public Optional<Condition> getCase(){
    return Optional.ofNullable(aCase);
  }
}
