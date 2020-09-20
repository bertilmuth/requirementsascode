package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.exception.InfiniteRepetition;
import org.requirementsascode.exception.MissingUseCaseStepPart;
import org.requirementsascode.exception.MoreThanOneStepCanReact;
import org.requirementsascode.exception.NestedCallOfReactTo;

/**
 * A model runner is a highly configurable controller that receives messages and
 * conditionally calls methods that handle them (the "system reactions").
 *
 * <p>
 * The runner is configured by the model it owns. Each real user needs an
 * instance of a runner, as the runner determines the user journey.
 */
public class ModelRunner {
  private static final Class<?> SYSTEM_EVENT_CLASS = ModelRunner.class;

  private AbstractActor owningActor;
  private AbstractActor runActor;

  private Model model;
  private Step latestStep;
  private Object latestPublishedEvent;
  private boolean isRunning;
  private StepToBeRun stepToBeRun;
  private Consumer<StepToBeRun> messageHandler;
  private Consumer<Object> unhandledMessageHandler;
  private Consumer<Object> messagePublisher;
  private List<String> recordedStepNames;
  private List<Object> recordedMessages;
  private boolean isRecording;
  private boolean nestedReactToMessageCallCausesException;

  /**
   * Constructor for creating a model runner.
   */
  public ModelRunner() {
    handleWith(this::runStep);
    publishWith(this::publishMessage);
  }

  private void runStep(StepToBeRun stepToBeRun) {
    stepToBeRun.run();
  }

  private <T> void publishMessage(T message) {
    Optional<AbstractActor> optionalToActor = getLatestStep().flatMap(ls -> ls.getPublishTo());
    nestedReactToMessageCallCausesException = false;
    if (optionalToActor.isPresent()) {
      AbstractActor owningActor = getOwningActor().orElse(model.getUserActor());
      optionalToActor.get().reactTo(message, owningActor);
    } else {
      this.reactToMessage(message);
    }
  }

  /**
   * Define a custom message handler. It can perform tasks before/after running
   * the step (which will trigger the system reaction method defined in the
   * model).
   * 
   * A custom message handler is useful for cross-cutting concerns, e.g. measuring
   * performance.
   *
   * @param messageHandler the custom message handler
   * @return this model runner, for chaining
   */
  public ModelRunner handleWith(Consumer<StepToBeRun> messageHandler) {
    this.messageHandler = Objects.requireNonNull(messageHandler);
    return this;
  }

  /**
   * Define handler for messages that the runner doesn't react to.
   * 
   * @param unhandledMessageHandler the handler for messages not handled by the
   *                                runner
   * @return this model runner, for chaining
   */
  public ModelRunner handleUnhandledWith(Consumer<Object> unhandledMessageHandler) {
    this.unhandledMessageHandler = Objects.requireNonNull(unhandledMessageHandler);
    return this;
  }

  /**
   * Define a custom publisher for events. It will be called after a system
   * reaction has been run, for the returned event objects of the system reaction.
   *
   * @param eventPublisher the custom event eventPublisher
   * @return this model runner, for chaining
   */
  public ModelRunner publishWith(Consumer<Object> eventPublisher) {
    Objects.requireNonNull(eventPublisher);
    this.messagePublisher = event -> {
      if (isRunning) {
        setLatestPublishedEvent(event);
        eventPublisher.accept(event);
      }
    };
    return this;
  }

  /**
   * Restarts the runner, resetting it to its original defaults ("no flow has been
   * run, no step has been run").
   */
  public void restart() {
    setLatestStep(null);
    run(model);
  }

  /**
   * Configures the runner to use the specified model. After you called this
   * method, the runner will accept messages via {@link #reactTo(Object)}.
   *
   * <p>
   * As a side effect, this method immediately triggers "autonomous system
   * reactions".
   *
   * @param model the model that defines the runner's behavior
   * @return this model runner, for chaining
   */
  public ModelRunner run(Model model) {
    AbstractActor userActor = model.getUserActor();
    as(userActor).run(model);
    return this;
  }

  /**
   * After you called this method, the runner will only react in steps that have
   * explicitly set the specified actor as one of its actors, or that are declared
   * as "autonomous system reactions".
   *
   * @param runActor the actor to run as
   * @return object for method chaining
   */
  public As as(AbstractActor runActor) {
    return new As(runActor);
  }

  public class As {
    private As(AbstractActor runActor) {
      setRunActor(runActor);
    }

    public ModelRunner run(Model model) {
      return runModel(model);
    }

    public <T, U> Optional<U> reactTo(T message) {
      return ModelRunner.this.reactTo(message);
    }

    public <U> Optional<U> reactTo(Object... messages) {
      return ModelRunner.this.reactTo(messages);
    }
  }

  private ModelRunner runModel(Model model) {
    setModel(model);
    initializeStepToBeRun();
    setRunning(true);
    triggerAutonomousSystemReaction();
    return this;
  }

  private void triggerAutonomousSystemReaction() {
    nestedReactToMessageCallCausesException = false;
    reactToMessage(this);
  }

  /**
   * Returns whether the runner is currently running.
   *
   * @see #run(Model)
   * @return true if the runner is running, false otherwise.
   */
  public boolean isRunning() {
    return isRunning;
  }

  /**
   * Stops the runner. It will not be reacting to messages, until
   * {@link #run(Model)} is called again.
   */
  public void stop() {
    setRunning(false);
  }

  /**
   * Call this method to provide several messages to the runner. For each message,
   * {@link #reactTo(Object)} is called.
   *
   * @param <U>      the return type that you as the user expects.
   * 
   * @param messages the message objects
   * @return the event that was published (latest) if the system reacted, or an
   *         empty Optional.
   */
  public <U> Optional<U> reactTo(Object... messages) {
    Objects.requireNonNull(messages);
    clearLatestPublishedEvent();
    return reactToMessages(messages);
  }

  /**
   * Call this method to provide a message (i.e. command or event object) to the
   * runner.
   *
   * <p>
   * If it is running, the runner will then check which steps can react. If a
   * single step can react, the runner will call the message handler with it. If
   * no step can react, the runner will either call the handler defined with
   * {@link #handleUnhandledWith(Consumer)}, or if no such handler exists, consume
   * the message silently.
   * 
   * If more than one step can react, the runner will throw an exception.
   *
   * <p>
   * After that, the runner will trigger "autonomous system reactions".
   * 
   * Note that if you provide an array or a collection as the first and only argument, this
   * will be flattened to the contained objects, and for each object
   * {@link #reactTo(Object)} is called.
   *
   * <p>
   * See {@link #canReactTo(Class)} for a description of what "can react" means.
   *
   * @param <T>     the type of message
   * @param <U>     the return type that you as the user expects.
   * @param message the message object
   * @return the event that was published (latest) if the system reacted, or an
   *         empty Optional.
   * @throws MoreThanOneStepCanReact when more than one step can react
   * @throws InfiniteRepetition      when a step has an always true condition, or
   *                                 there is an infinite loop.
   * @throws ClassCastException      when type of the returned instance isn't U
   */
  public <T, U> Optional<U> reactTo(T message) {
    Objects.requireNonNull(message);

    clearLatestPublishedEvent();
    
    Object[] messages;
    if (message instanceof Collection) {
      messages = ((Collection<?>) message).toArray(new Object[0]);
    } else if(message instanceof Object[]) {
      messages = ((Object[])message);
    } else {
      messages = new Object[] {message};
    }

    return reactToMessages(messages);
  }
  
  @SuppressWarnings("unchecked")
  private <U> Optional<U> reactToMessages(Object[] messages) {
    for (Object message : messages) {
      reactToMessage(message);
    }
    return Optional.ofNullable((U) latestPublishedEvent);
  }

  private <T> void reactToMessage(T message) {
    if (!isRunning) {
      return;
    }
    if (nestedReactToMessageCallCausesException) {
      throw new NestedCallOfReactTo();
    }

    Class<? extends Object> currentMessageClass = message.getClass();

    try {
      int nrOfStepsThatCanReact = 0;
      Step stepThatWillReact = null;
      Collection<Step> steps = model.getModifiableSteps();

      for (Step step : steps) {
        if (canReactToMessageClass(step, currentMessageClass)) {
          stepThatWillReact = step;
          nrOfStepsThatCanReact++;

          if (nrOfStepsThatCanReact > 1) {
            // No more than one step is allowed to react to a message
            throw new MoreThanOneStepCanReact(steps);
          }
        }
      }

      if (nrOfStepsThatCanReact == 1) {
        triggerSystemReaction(message, stepThatWillReact);
      } else if (unhandledMessageHandler != null && !isSystemEvent(message)) {
        unhandledMessageHandler.accept(message);
      } else if (message instanceof RuntimeException) {
        throw (RuntimeException) message;
      }
    } catch (StackOverflowError err) {
      throw new InfiniteRepetition(latestStep);
    }
  }

  private boolean canReact(Step step) {
    boolean stepCanReact = hasRightActor(step) && hasTruePredicate(step);
    return stepCanReact;
  }

  boolean canReactToMessageClass(Step step, Class<? extends Object> currentMessageClass) {
    boolean stepCanReact = stepMessageClassIsSameOrSuperclass(step, currentMessageClass) && canReact(step);
    return stepCanReact;
  }

  private boolean hasRightActor(Step step) {
    final Predicate<AbstractActor> isSystemOrRunActor = actor -> actor.equals(model.getUserActor())
      || actor.equals(model.getSystemActor()) || actor.equals(runActor);

    AbstractActor[] stepActors = step.getActors();
    if (stepActors == null) {
      throw (new MissingUseCaseStepPart(step, "actor"));
    }

    for (AbstractActor stepActor : stepActors) {
      if (isSystemOrRunActor.test(stepActor)) {
        return true;
      }
    }
    return false;
  }

  private boolean stepMessageClassIsSameOrSuperclass(Step step, Class<?> currentMessageClass) {
    Class<?> stepMessageClass = step.getMessageClass();
    if (stepMessageClass == null) {
      throw new MissingUseCaseStepPart(step, "on/user");
    }
    boolean result = hasSystemEventClass(currentMessageClass) ? hasSystemEventClass(stepMessageClass)
      : stepMessageClass.isAssignableFrom(currentMessageClass);
    return result;
  }

  private boolean hasTruePredicate(Step step) {
    Predicate<ModelRunner> predicate = step.getPredicate();
    boolean result = predicate.test(this);
    return result;
  }

  private void triggerSystemReaction(Object message, Step step) {
    if (step.getSystemReaction() == null) {
      throw new MissingUseCaseStepPart(step, "system");
    }

    stepToBeRun.setupWith(step, message);
    setLatestStep(step);

    try {
      nestedReactToMessageCallCausesException = true;

      Condition isTheCase = step.getCase().orElse(() -> true);
      if (isTheCase.evaluate()) {
        recordStepNameAndMessage(step, message);
        messageHandler.accept(stepToBeRun);
        publishReturnedMessage();
      }

    } catch (Exception e) {
      handleException(e);
    }

    triggerAutonomousSystemReaction();
  }

  private void publishReturnedMessage() {
    Optional<Object> messageToBePublished = stepToBeRun.getMessageToBePublished();
    if (messagePublisher != null && messageToBePublished.isPresent()) {
      messagePublisher.accept(messageToBePublished.get());
    }
  }

  private <T> boolean isSystemEvent(T message) {
    return hasSystemEventClass(message.getClass());
  }

  private boolean hasSystemEventClass(Class<?> messageClass) {
    return SYSTEM_EVENT_CLASS.equals(messageClass);
  }

  void recordStepNameAndMessage(Step step, Object message) {
    if (isRecording) {
      recordedStepNames.add(step.getName());
      if (message != null && !isSystemEvent(message)) {
        recordedMessages.add(message);
      }
    }
  }

  /**
   * Returns whether at least one step can react to a message of the specified
   * class.
   * <p>
   * A step "can react" if all of the following conditions are met: a) the runner
   * is running b) one of the step's actors matches the actor the runner is run as
   * c) the step's message class is the same or a superclass of the specified
   * message class d) the step has a condition that is true
   *
   * @param messageClass the type of message to check steps for
   * @return true if the runner is running and at least one step can react, false
   *         otherwise
   */
  public boolean canReactTo(Class<? extends Object> messageClass) {
    Objects.requireNonNull(messageClass);

    if (!isRunning) {
      return false;
    }
    boolean canReact = false;

    Collection<Step> steps = model.getModifiableSteps();
    for (Step step : steps) {
      if (canReactToMessageClass(step, messageClass)) {
        canReact = true;
        break;
      }
    }

    return canReact;
  }

  /**
   * Returns the classes of messages the runner can react to.
   * <p>
   * See {@link #canReactTo(Class)} for a description of what "can react" means.
   * 
   * @return the collection of message types
   */
  public Set<Class<?>> getReactToTypes() {
    Set<Class<?>> reactToTypes;

    if (isRunning) {
      reactToTypes = new LinkedHashSet<>();
      Collection<Step> steps = model.getModifiableSteps();

      for (Step step : steps) {
        if (canReact(step)) {
          Class<?> messageClass = step.getMessageClass();
          reactToTypes.add(messageClass);
        }
      }
    } else {
      reactToTypes = Collections.emptySet();
    }

    return reactToTypes;
  }

  /**
   * Returns the steps in the model that can react to the specified message class.
   *
   * @param messageClass the class of messages
   * @return the steps that can react to the class of messages
   */
  public Set<Step> getStepsThatCanReactTo(Class<? extends Object> messageClass) {
    Objects.requireNonNull(messageClass);

    Set<Step> stepsThatCanReact = new HashSet<>(2);

    if (isRunning) {
      Collection<Step> steps = model.getModifiableSteps();
      for (Step step : steps) {
        if (canReactToMessageClass(step, messageClass)) {
          stepsThatCanReact.add(step);
        }
      }
    }

    return stepsThatCanReact;
  }

  /**
   * Overwrite this method to control what happens exactly when an exception is
   * thrown by a system reaction. The behavior implemented in runner: the model
   * runner reacts to the exception. You may replace this with a more
   * sophisticated behavior, that for example involves some kind of logging.
   *
   * @param e the exception that has been thrown by the system reaction
   */
  protected void handleException(Exception e) {
    nestedReactToMessageCallCausesException = false;
    reactToMessage(e);
  }

  /**
   * Returns the latest step that has been run by this runner.
   *
   * @return the latest step run
   */
  public Optional<Step> getLatestStep() {
    return Optional.ofNullable(latestStep);
  }

  /**
   * Sets the latest step run by the runner.
   *
   * <p>
   * Use this method if you want to restore some previous state, normally you
   * should influence the behavior of the runner by calling
   * {@link #reactTo(Object)}.
   *
   * @param latestStep the latest step run
   */
  public void setLatestStep(Step latestStep) {
    this.latestStep = latestStep;
  }

  /**
   * Returns the flow the latest step that has been run is contained in.
   *
   * @return the latest flow run
   */
  public Optional<Flow> getLatestFlow() {
    return getLatestStep().filter(step -> step instanceof FlowStep).map(step -> ((FlowStep) step).getFlow());
  }

  /**
   * After calling this method, until recording is stopped, messages and step
   * names are recorded. If messages/step names have been recorded before calling
   * this method, they are discarded.
   * 
   * @return this model runner for method chaining
   */
  public ModelRunner startRecording() {
    recordedStepNames = new ArrayList<>();
    recordedMessages = new ArrayList<>();
    isRecording = true;
    return this;
  }

  /**
   * When calling this method, recording is stopped. No messages and step names
   * are recorded until {@link #startRecording()} is called again.
   * 
   * @return this model runner for method chaining
   */
  public ModelRunner stopRecording() {
    isRecording = false;
    return this;
  }

  /**
   * Returns the recorded names of the steps that have been run so far.
   * <p>
   * If no step has been run, an empty array is returned. For example, this method
   * can used with the assertArrayEquals method of JUnit to compare the actual
   * names of steps that have been run (returned by this method) to the expected
   * step names.
   *
   * @return the ordered names of steps run by this runner
   */
  public String[] getRecordedStepNames() {
    if (recordedStepNames == null) {
      recordedStepNames = new ArrayList<>();
    }
    String[] stepNames = recordedStepNames.stream().toArray(String[]::new);
    return stepNames;
  }

  /**
   * Returns the recorded messages that the runner reacted to so far.
   * <p>
   * If no messages have caused a system reaction, an empty array is returned. For
   * example, this method can used with the assertArrayEquals method of JUnit to
   * compare the actual messages that caused a reaction (returned by this method)
   * to the expected messages.
   *
   * @return the messages that caused a system reaction, in order of occurrence
   */
  public Object[] getRecordedMessages() {
    if (recordedMessages == null) {
      recordedMessages = new ArrayList<>();
    }
    Object[] messages = recordedMessages.toArray();
    return messages;
  }

  /**
   * Returns the actor that owns this model runner for running its behavior.
   * 
   * @return the owning actor, if existent, or an empty optional
   */
  public Optional<AbstractActor> getOwningActor() {
    return Optional.ofNullable(owningActor);
  }

  void setOwningActor(AbstractActor owningActor) {
    this.owningActor = owningActor;
  }

  private void initializeStepToBeRun() {
    this.stepToBeRun = new StepToBeRun();
  }
  
  /**
   * Returns the model currently run by this model runner.
   * 
   * @return the model that is run, or an empty optional
   */
  public Optional<Model> getModel() {
    return Optional.ofNullable(model);
  }

  private void setModel(Model model) {
    this.model = Objects.requireNonNull(model);
  }

  private void setRunning(boolean status) {
    isRunning = status;
  }

  private void setRunActor(AbstractActor runActor) {
    this.runActor = Objects.requireNonNull(runActor);
  }

  private void clearLatestPublishedEvent() {
    latestPublishedEvent = null;
  }

  private void setLatestPublishedEvent(Object event) {
    latestPublishedEvent = event;
  }
}
