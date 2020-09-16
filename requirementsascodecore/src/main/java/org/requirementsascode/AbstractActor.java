package org.requirementsascode;

import java.util.Optional;
import java.util.function.Consumer;

import org.requirementsascode.exception.InfiniteRepetition;
import org.requirementsascode.exception.MoreThanOneStepCanReact;

/**
 * An actor can be anything with a behavior. It can be the system/service you're
 * developing, or a role that an external user plays.
 * 
 * Actors can be senders and receivers of messages to other actors.
 * 
 * Actors enable to distinguish user rights: only an actor that is connected to
 * a particular step is allowed to cause a system reaction for that step.
 *
 * @author b_muth
 */
public abstract class AbstractActor {
  private String name;
  private ModelRunner modelRunner;

  /**
   * Creates an actor with a name equal to the current class' simple name.
   *
   */
  public AbstractActor() {
    createOwnedModelRunner();
    setName(getClass().getSimpleName());
  }

  /**
   * Creates an actor with the specified name.
   *
   * @param name the name of the actor
   */
  public AbstractActor(String name) {
    createOwnedModelRunner();
    setName(name);
  }

  protected void createOwnedModelRunner() {
    this.modelRunner = new ModelRunner();
    this.modelRunner.setOwningActor(this);
  }

  /**
   * Returns the name of the actor.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  private void setName(String name) {
    this.name = name;
  }

  /**
   * Runs the behavior of this actor.
   * 
   * You only need to explicitly call this method if your model starts with a step
   * that has no event/command defined (i.e. no user(...) / on(...)).
   */
  public void run() {
    Model actorBehavior = behavior();
    if (actorBehavior != null) {
      getModelRunner().run(actorBehavior);
    }
  }

  /**
   * Call this method to provide a message (i.e. command or event object) to the
   * actor.
   *
   * <p>
   * The actor will then check which steps can react. If a single step can react,
   * the actor will call the message handler with it. If no step can react, the
   * actor will either call the handler defined with
   * {@link ModelRunner#handleUnhandledWith(Consumer)} on the actor's model
   * runner, or if no such handler exists, consume the message silently.
   * 
   * If more than one step can react, the actor will throw an exception.
   * <p>
   * After that, the actor will trigger "autonomous system reactions" that don't
   * have a message class.
   * 
   * Note that if you provide a collection as the first and only argument, this
   * will be flattened to the objects in the collection, and for each object
   * {@link #reactTo(Object)} is called.
   *
   * @see AbstractActor#getModelRunner()
   * @see ModelRunner#handleUnhandledWith(Consumer)
   * @see ModelRunner#reactTo(Object)
   * 
   * @param <T>     the type of message
   * @param <U>     the return type that you as the user expects.
   * @param message the message object
   * @return the event that was published (latest) if the system reacted, or an
   *         empty Optional.
   * 
   * @throws MoreThanOneStepCanReact when more than one step can react
   * @throws InfiniteRepetition      when a step has an always true condition, or
   *                                 there is an infinite loop.
   * @throws ClassCastException      when type of the returned instance isn't U
   */
  public <T, U> Optional<U> reactTo(T message) {
    return reactTo(message, null);
  }

  /**
   * Same as {@link #reactTo(Object)}, but with the specified actor as the calling
   * user's role.
   * 
   * @param <T>          the type of message
   * @param <U>          the return type that you as the user expects.
   * @param message      the message object
   * @param callingActor the actor as which to call this actor.
   * @return the event that was published (latest) if the system reacted, or an
   *         empty Optional.
   */
  public <T, U> Optional<U> reactTo(Object message, AbstractActor callingActor) {
    if (!getModelRunner().isRunning()) {
      run();
    }
    AbstractActor runActor = callingActorOrDefaultUser(callingActor);
    if (runActor != null) {
      Optional<U> latestPublishedEvent = getModelRunner().as(runActor).reactTo(message);
      return latestPublishedEvent;
    } else {
      return Optional.empty();
    }
  }

  private AbstractActor callingActorOrDefaultUser(AbstractActor callingActor) {
    AbstractActor runActor;
    if (callingActor == null) {
      runActor = getModelRunner().getModel().map(Model::getUserActor).orElse(null);
    } else {
      runActor = callingActor;
    }
    return runActor;
  }

  /**
   * Override this method to provide the model for the actor's behavior.
   * 
   * @return the behavior
   */
  public abstract Model behavior();

  /**
   * Call this method from a subclass to customize the way the actor runs the
   * behavior.
   * 
   * @return the single model runner encapsulated by this actor
   */
  public ModelRunner getModelRunner() {
    return modelRunner;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractActor other = (AbstractActor) obj;
    if (getName() == null) {
      if (other.getName() != null)
        return false;
    } else if (!getName().equals(other.getName()))
      return false;
    return true;
  }
}