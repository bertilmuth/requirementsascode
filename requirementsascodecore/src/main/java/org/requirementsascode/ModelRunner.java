package org.requirementsascode;

import java.io.Serializable;
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
 * A model runner is a highly configurable controller that receives events and
 * conditionally calls methods that handle them (the "system reactions").
 *
 * <p>
 * The runner is configured by the model it owns. Each real user needs an
 * instance of a runner, as the runner determines the user journey.
 */
public class ModelRunner implements Serializable {
    private static final long serialVersionUID = 1787451244764017381L;

    private Actor runActor;

    private Model model;
    private Step latestStep;
    private boolean isRunning;
    private StepToBeRun stepToBeRun;
    private Consumer<StepToBeRun> eventHandler;
    private Consumer<Object> unhandledEventHandler;
    private Consumer<Object> eventPublisher;
    private List<String> recordedStepNames;
    private List<Object> recordedEvents;
    private boolean isRecording;
    private IncludedUseCases includedUseCases;

    private Collection<Step> steps;


    /**
     * Constructor for creating a runner with standard system reaction, that is: the
     * system reaction, as defined in the step, simply accepts an event.
     */
    public ModelRunner() {
	this.stepToBeRun = new StepToBeRun();
	this.recordedStepNames = new ArrayList<>();
	this.recordedEvents = new ArrayList<>();
	handleWith(stepToBeRun -> stepToBeRun.run());
	publishWith(event -> reactTo(event));
    }

    /**
     * Define a custom event handler. It can perform tasks before/after running the
     * step (which will trigger the system reaction method defined in the model).
     * 
     * A custom event handler is useful for cross-cutting concerns, e.g. measuring
     * performance.
     *
     * @param eventHandler
     *                         the custom event handler
     */
    public void handleWith(Consumer<StepToBeRun> eventHandler) {
	this.eventHandler = Objects.requireNonNull(eventHandler);
    }

    /**
     * Define handler for events that the runner doesn't react to.
     * 
     * @param unhandledEventHandler
     *                                  the handler for events not handled by the
     *                                  runner
     */
    public void handleUnhandledWith(Consumer<Object> unhandledEventHandler) {
	this.unhandledEventHandler = Objects.requireNonNull(unhandledEventHandler);
    }
    
    /**
     * Define a custom publisher for events. It will be called after a
     * system reaction has been run, for each of the returned event objects of
     * the system reaction.
     *
     * @param eventPublisher
     *                         the custom event eventPublisher
     */
    public void publishWith(Consumer<Object> eventPublisher) {
	this.eventPublisher = Objects.requireNonNull(eventPublisher);
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
     * method, the runner will accept events via {@link #reactTo(Object)}.
     *
     * <p>
     * As a side effect, this method immediately triggers "autonomous system
     * reactions".
     *
     * @param model
     *                  the model that defines the runner's behavior
     * @return the same model runner, for chaining with @see
     *         {@link #reactTo(Object...)}
     */
    public ModelRunner run(Model model) {
	this.model = Objects.requireNonNull(model);
	this.includedUseCases = new IncludedUseCases();
	this.isRunning = true;

	Actor runActorOrDefaultUser = runActor != null ? runActor : model.getUserActor();
	as(runActorOrDefaultUser).triggerAutonomousSystemReaction();
	return this;
    }

    private void triggerAutonomousSystemReaction() {
	eventPublisher.accept(this);
    }

    /**
     * After you called this method, the runner will only react to steps that have
     * explicitly set the specified actor as one of its actors, or that are declared
     * as "autonomous system reactions".
     *
     * @param runActor
     *                  the actor to run as
     * @return this runner, for method chaining with {@link #run(Model)}
     */
    public ModelRunner as(Actor runActor) {
	this.runActor = Objects.requireNonNull(runActor);
	if(model != null) {
	    this.steps = getActorSteps(runActor, model);
	}
	return this;
    }
    private Collection<Step> getActorSteps(Actor actor, Model model) {
	Set<Step> actorSteps = model.getModifiableSteps().stream()
		.filter(step -> anyStepActorIsRunActor(step, actor))
		.collect(Collectors.toSet());
	return actorSteps;
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
     * Stops the runner. It will not be reacting to events, until
     * {@link #run(Model)} is called again.
     */
    public void stop() {
	isRunning = false;
    }

    /**
     * Call this method to provide several event objects to the runner. For each
     * event object, {@link #reactTo(Object)} is called.
     *
     * @param events
     *                   the events to react to
     * @return the latest step that has been run
     */
    public Optional<Step> reactTo(Object... events) {
	Objects.requireNonNull(events);

	for (Object event : events) {
	    reactTo(event);
	}
	return getLatestStep();
    }

    /**
     * Call this method to provide a command or event object to the runner.
     *
     * <p>
     * If it is running, the runner will then check which steps can react to the
     * event. If a single step can react, the runner will call the event handler
     * with it. If no step can react, the runner will either call the handler
     * defined with {@link #handleUnhandledWith(Consumer)}, or if no such handler
     * exists, consume the event silently.
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
     * @param       <T>
     *                  the type of the command or event object
     * @param event
     *                  the command or event object
     * @return the step that was run latest by the model runner
     * @throws MoreThanOneStepCanReact
     *                                     when more than one step can react
     * @throws InfiniteRepetition
     *                                     when a step has an always true condition,
     *                                     or there is an infinite loop.
     */
    public <T> Optional<Step> reactTo(T event) {
	Objects.requireNonNull(event);

	if (event instanceof Collection) {
	    Object[] events = ((Collection<?>) event).toArray(new Object[0]);
	    return reactTo(events);
	}

	if (isRunning) {
	    Class<? extends Object> currentEventClass = event.getClass();

	    try {
		Set<Step> stepsThatCanReact = getStepsThatCanReactTo(currentEventClass);
		triggerSystemReactionForSteps(event, stepsThatCanReact);
	    } catch (StackOverflowError err) {
		throw new InfiniteRepetition(latestStep);
	    }
	}
	return getLatestStep();
    }

    private void triggerSystemReactionForSteps(Object event, Collection<Step> steps) {
	Step step = null;

	if (steps.size() == 1) {
	    step = steps.iterator().next();
	    triggerSystemReactionForStep(event, step);
	} else if (steps.size() > 1) {
	    throw new MoreThanOneStepCanReact(steps);
	} else if (unhandledEventHandler != null && !isSystemEvent(event)) {
	    unhandledEventHandler.accept(event);
	} else if (event instanceof RuntimeException) {
	    throw (RuntimeException) event;
	}
    }

    private <T> boolean isSystemEvent(T event) {
	return event instanceof ModelRunner;
    }

    private void triggerSystemReactionForStep(Object event, Step step) {
	if (step.getSystemReaction() == null) {
	    throw new MissingUseCaseStepPart(step, "system");
	}

	stepToBeRun.setupWith(step, event, eventPublisher);
	recordStepNameAndEvent(step, event);

	setLatestStep(step);

	try {
	    eventHandler.accept(stepToBeRun);
	} catch (Exception e) {
	    handleException(e);
	}

	includedUseCases.continueAfterIncludeStepWhenEndOfIncludedFlowIsReached(this);
	triggerAutonomousSystemReaction();
    }

    void recordStepNameAndEvent(Step step, Object event) {
	if (isRecording) {
	    recordedStepNames.add(step.getName());
	    if (event != null) {
		recordedEvents.add(event);
	    }
	}
    }

    /**
     * Returns whether at least one step can react to an event of the specified
     * class.
     * <p>
     * A step "can react" if all of the following conditions are met: a) the runner
     * is running b) one of the step's actors matches the actor the runner is run as
     * c) the step's event class is the same or a superclass of the specified event
     * class d) the step has a condition that is true
     *
     * @param eventClass
     *                       the specified class
     * @return true if the runner is running and at least one step can react, false
     *         otherwise
     */
    public boolean canReactTo(Class<? extends Object> eventClass) {
	Objects.requireNonNull(eventClass);
	boolean canReact = getReactToTypes().contains(eventClass);
	return canReact;
    }

    /**
     * Returns the classes of events the runner can react to.
     * <p>
     * See {@link #canReactTo(Class)} for a description of what "can react" means.
     * 
     * @return the collection of classes of events
     */ 
    public Set<Class<?>> getReactToTypes() {
	Set<Class<?>> eventsReactedTo = getRunningStepStream()
		.filter(step -> includedUseCases.isStepInIncludedUseCaseIfPresent(step))
		.filter(step -> hasTruePredicate(step)).map(step -> step.getEventClass())
		.collect(Collectors.toCollection(LinkedHashSet::new));
	return eventsReactedTo;
    }

    /**
     * Returns the steps in the model that can react to the specified event class.
     *
     * @param eventClass
     *                       the class of events
     * @return the steps that can react to the class of events
     */
    public Set<Step> getStepsThatCanReactTo(Class<? extends Object> eventClass) {
	Objects.requireNonNull(eventClass);

	Set<Step> stepsThatCanReact = getStepsInStreamThatCanReactTo(eventClass, getRunningStepStream());
	return stepsThatCanReact;
    }

    Stream<Step> getRunningStepStream() {
	Stream<Step> stepStream = isRunning ? steps.stream() : Stream.empty();
	return stepStream;
    }

    Set<Step> getStepsInStreamThatCanReactTo(Class<? extends Object> eventClass, Stream<Step> stepStream) {
	Set<Step> steps = stepStream
		.filter(step -> stepEventClassIsSameOrSuperclassAsEventClass(step, eventClass))
		.filter(step -> includedUseCases.isStepInIncludedUseCaseIfPresent(step))
		.filter(step -> hasTruePredicate(step))
		.collect(Collectors.toSet());
	return steps;
    }

    private boolean anyStepActorIsRunActor(Step step, Actor runActor) {
	Actor[] stepActors = step.getActors();
	if (stepActors == null) {
	    throw (new MissingUseCaseStepPart(step, "actor"));
	}

	Actor systemActor = step.getModel().getSystemActor();
	for (Actor stepActor : stepActors) {
	    if (stepActor.equals(systemActor) || stepActor.equals(runActor)) {
		return true;
	    }
	}
	return false;
    }

    private boolean stepEventClassIsSameOrSuperclassAsEventClass(Step useCaseStep, Class<?> currentEventClass) {
	Class<?> stepEventClass = useCaseStep.getEventClass();
	boolean result = stepEventClass.isAssignableFrom(currentEventClass);
	return result;
    }

    private boolean hasTruePredicate(Step step) {
	Predicate<ModelRunner> predicate = step.getPredicate();
	boolean result = predicate.test(this);
	return result;
    }

    /**
     * Overwrite this method to control what happens exactly when an exception is
     * thrown by a system reaction. The behavior implemented in runner: the
     * exception is published as an event.
     * You may replace this with a more sophisticated
     * behavior, that for example involves some kind of logging.
     *
     * @param e
     *              the exception that has been thrown by the system reaction
     */
    protected void handleException(Exception e) {
	eventPublisher.accept(e);
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
     * @param latestStep
     *                       the latest step run
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
     * After calling this method, until recording is stopped, events and step names
     * are recorded. If step names/events have been recorded before calling this
     * method, these are discarded.
     * 
     * @return this model runner for method chaining
     */
    public ModelRunner startRecording() {
	recordedStepNames.clear();
	recordedEvents.clear();
	isRecording = true;
	return this;
    }

    /**
     * When calling this method, recording is stopped. No events and step names are
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
	String[] stepNames = recordedStepNames.stream().toArray(String[]::new);
	return stepNames;
    }

    /**
     * Returns the recorded events that the runner reacted to so far.
     * <p>
     * If no events have caused a system reaction, an empty array is returned. For
     * example, this method can used with the assertArrayEquals method of JUnit to
     * compare the actual events that caused a reaction (returned by this method) to
     * the expected events.
     *
     * @return the ordered events that caused a system reaction
     */
    public Object[] getRecordedEvents() {
	Object[] events = recordedEvents.toArray();
	return events;
    }

    public void startIncludedUseCase(UseCase includedUseCase, FlowStep includeStep) {
	includedUseCases.startIncludedUseCase(includedUseCase, includeStep);
    }
}
