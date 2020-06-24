package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.requirementsascode.exception.InfiniteRepetition;
import org.requirementsascode.exception.MissingUseCaseStepPart;
import org.requirementsascode.exception.MoreThanOneStepCanReact;

/**
 * A model runner is a highly configurable controller that receives messages and
 * conditionally calls methods that handle them (the "system reactions").
 *
 * <p>
 * The runner is configured by the model it owns. Each real user needs an
 * instance of a runner, as the runner determines the user journey.
 */
public class ModelRunner {
	private Actor runActor;

	private Model model;
	private Step latestStep;
	private Object latestPublishedEvent;
	private boolean isRunning;
	private StepToBeRun stepToBeRun;
	private Consumer<StepToBeRun> messageHandler;
	private Consumer<Object> unhandledMessageHandler;
	private Consumer<Object> eventPublisher;
	private List<String> recordedStepNames;
	private List<Object> recordedMessages;
	private boolean isRecording;
	private Collection<Step> steps;

	/**
	 * Constructor for creating a model runner.
	 */
	public ModelRunner() {
		handleWith(stepToBeRun -> stepToBeRun.run());
		publishWith(this::reactToMessage);
	}

	/**
	 * Define a custom message handler. It can perform tasks before/after running the
	 * step (which will trigger the system reaction method defined in the model).
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
	 * @param unhandledMessageHandler the handler for messages not handled by the runner
	 * @return this model runner, for chaining
	 */
	public ModelRunner handleUnhandledWith(Consumer<Object> unhandledMessageHandler) {
		this.unhandledMessageHandler = Objects.requireNonNull(unhandledMessageHandler);
		return this;
	}

	/**
	 * Define a custom publisher for events. It will be called after a system
	 * reaction has been run, for the returned event objects of the system
	 * reaction.
	 *
	 * @param eventPublisher the custom event eventPublisher
	 * @return this model runner, for chaining
	 */
	public ModelRunner publishWith(Consumer<Object> eventPublisher) {
		Objects.requireNonNull(eventPublisher);
		this.eventPublisher = event -> {
			setLatestPublishedEvent(event);
			eventPublisher.accept(event);
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
		as(model.getUserActor()).run(model);
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
	public As as(Actor runActor) {
		return new As(runActor);
	}
	
	public class As{
		private As(Actor runActor) {
			setRunActor(runActor);
			if(isRunning()) {
				updateActorStepsFrom(model);
			}
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
		updateActorStepsFrom(model);
		initializeStepToBeRun();
		setRunning(true);
		triggerAutonomousSystemReaction();
		return this;
	}

	private void updateActorStepsFrom(Model model) {
		Predicate<Actor> isSystemOrRunActor = actor -> actor.equals(model.getSystemActor()) || actor.equals(runActor);

		this.steps = model.getModifiableSteps().stream()
			.filter(step -> hasRightActor(step, isSystemOrRunActor))
			.collect(Collectors.toSet());
	}

	private boolean hasRightActor(Step step, Predicate<Actor> actorTest) {
		Actor[] stepActors = step.getActors();
		if (stepActors == null) {
			throw (new MissingUseCaseStepPart(step, "actor"));
		}

		for (Actor stepActor : stepActors) {
			if (actorTest.test(stepActor)) {
				return true;
			}
		}
		return false;
	}
	
	private void triggerAutonomousSystemReaction() {
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
	@SuppressWarnings("unchecked")
	public <U> Optional<U> reactTo(Object... messages) {
		Objects.requireNonNull(messages);

		clearLatestPublishedEvent();
		for (Object message : messages) {
			reactToMessage(message);
		}
		return Optional.ofNullable((U) latestPublishedEvent);
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
	 * Note that if you provide a collection as the first and only argument, this
	 * will be flattened to the objects in the collection, and for each object
	 * {@link #reactTo(Object)} is called.
	 *
	 * <p>
	 * See {@link #canReactTo(Class)} for a description of what "can react" means.
	 *
	 * @param <T>     the type of message
	 * @param <U>     the return type that you as the user expects.
	 * @param message the message object
	 * @return the event that was published (latest) if the system reacted, or an empty Optional.
	 * @throws MoreThanOneStepCanReact when more than one step can react
	 * @throws InfiniteRepetition      when a step has an always true condition, or
	 *                                 there is an infinite loop.
	 * @throws ClassCastException      when type of the returned instance isn't U
	 */
	@SuppressWarnings("unchecked")
	public <T, U> Optional<U> reactTo(T message) {
		Objects.requireNonNull(message);

		if (message instanceof Collection) {
			Object[] messages = ((Collection<?>) message).toArray(new Object[0]);
			return reactTo(messages);
		}

		clearLatestPublishedEvent();
		reactToMessage(message);
		return Optional.ofNullable((U)latestPublishedEvent);
	}

	private <T> void reactToMessage(T message) {
		Class<? extends Object> currentMessageClass = message.getClass();

		if (isRunning) {
			try {
				int nrOfStepsThatCanReact = 0;
				Step stepThatWillReact = null;

				for (Step step : steps) {
					if (stepCanReact(step, currentMessageClass)) {
						stepThatWillReact = step;
						nrOfStepsThatCanReact++;
						
						if(nrOfStepsThatCanReact > 1) {
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
	}

	private boolean stepCanReact(Step step, Class<? extends Object> currentMessageClass) {
		boolean stepCanReact = stepMessageClassIsSameOrSuperclass(step, currentMessageClass)
			&& hasTruePredicate(step);
		return stepCanReact;
	}
	
	private boolean stepMessageClassIsSameOrSuperclass(Step step, Class<?> currentMessageClass) {
		Class<?> stepMessageClass = step.getMessageClass();
		boolean result = stepMessageClass.isAssignableFrom(currentMessageClass);
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

		stepToBeRun.setupWith(step, message, eventPublisher);
		recordStepNameAndMessage(step, message);

		setLatestStep(step);

		try {
			messageHandler.accept(stepToBeRun);
		} catch (Exception e) {
			handleException(e);
		}

		triggerAutonomousSystemReaction();
	}
	
	private <T> boolean isSystemEvent(T message) {
		return message instanceof ModelRunner;
	}

	void recordStepNameAndMessage(Step step, Object message) {
		if (isRecording) {
			recordedStepNames.add(step.getName());
			if (message != null) {
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
	 * c) the step's message class is the same or a superclass of the specified message
	 * class d) the step has a condition that is true
	 *
	 * @param messageClass the type of message to check steps for
	 * @return true if the runner is running and at least one step can react, false
	 *         otherwise
	 */
	public boolean canReactTo(Class<? extends Object> messageClass) {
		Objects.requireNonNull(messageClass);
		Set<Step> stepsThatCanReact = getStepsThatCanReactTo(messageClass);
		boolean canReact = !stepsThatCanReact.isEmpty();
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
		Set<Class<?>> reactToTypes = getRunningStepStream().filter(step -> hasTruePredicate(step))
				.map(step -> step.getMessageClass()).collect(Collectors.toCollection(LinkedHashSet::new));
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

		Set<Step> stepsThatCanReact = getStepsInStreamThatCanReactTo(messageClass, getRunningStepStream());
		return stepsThatCanReact;
	}

	Stream<Step> getRunningStepStream() {
		Stream<Step> stepStream = isRunning ? steps.stream() : Stream.empty();
		return stepStream;
	}

	Set<Step> getStepsInStreamThatCanReactTo(Class<? extends Object> messageClass, Stream<Step> stepStream) {
		Set<Step> steps = stepStream.filter(step -> stepMessageClassIsSameOrSuperclass(step, messageClass))
				.filter(step -> hasTruePredicate(step)).collect(Collectors.toSet());
		return steps;
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
	 * After calling this method, until recording is stopped, messages and step names
	 * are recorded. If messages/step names have been recorded before calling this
	 * method, they are discarded.
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
	 * When calling this method, recording is stopped. No messages and step names are
	 * recorded until {@link #startRecording()} is called again.
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
		if(recordedStepNames == null) {
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
	 * compare the actual messages that caused a reaction (returned by this method) to
	 * the expected messages.
	 *
	 * @return the messages that caused a system reaction, in order of occurrence
	 */
	public Object[] getRecordedMessages() {
		if(recordedMessages == null) {
			recordedMessages = new ArrayList<>();
		}
		Object[] messages = recordedMessages.toArray();
		return messages;
	}
	
	private void initializeStepToBeRun() {
		this.stepToBeRun = new StepToBeRun();
	}
	
	private void setModel(Model model) {
		this.model = Objects.requireNonNull(model);
	}

	private void setRunning(boolean status) {
		isRunning = status;
	}
	
	private void setRunActor(Actor runActor) {
		this.runActor = Objects.requireNonNull(runActor);
	}
	
	private void clearLatestPublishedEvent() {
		latestPublishedEvent = null;
	}
	
	private void setLatestPublishedEvent(Object event) {
		latestPublishedEvent = event;
	}
}
