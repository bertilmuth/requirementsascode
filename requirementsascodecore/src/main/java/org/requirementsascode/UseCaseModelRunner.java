package org.requirementsascode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.requirementsascode.exception.MissingUseCaseStepPart;
import org.requirementsascode.exception.MoreThanOneStepCanReact;
import org.requirementsascode.exception.UnhandledException;

/**
 * A use case model runner is a highly configurable use case controller that
 * receives events from the frontend and conditionally calls methods on the
 * backend.
 *
 * <p>
 * In requirementsascode, a use case model runner is the only way the frontend
 * communicates with the backend. It is configured by the use case model it
 * owns. Each real user needs an instance of a runner, as the runner determines
 * the journey of the user through the use cases.
 */
public class UseCaseModelRunner implements Serializable {
    private static final long serialVersionUID = 1787451244764017381L;

    private Actor user;
    private List<Actor> userAndSystem;

    private UseCaseModel useCaseModel;
    private Step latestStep;
    private boolean isRunning;
    private SystemReactionTrigger systemReactionTrigger;
    private Consumer<SystemReactionTrigger> systemReaction;
    private Predicate<Step> stepWithoutAlternativeCondition;
    private LinkedList<UseCase> includedUseCases;
    private LinkedList<FlowStep> includeSteps;
    private UseCase includedUseCase;
    private FlowStep includeStep;

    /**
     * Constructor for creating a runner with standard system reaction, that is: the
     * system reaction, as defined in the use case step, simply accepts an event.
     */
    public UseCaseModelRunner() {
	this.systemReactionTrigger = new SystemReactionTrigger();

	adaptSystemReaction(new DirectSystemReactionTrigger());
	restart();
    }

    private static class DirectSystemReactionTrigger implements Consumer<SystemReactionTrigger>, Serializable {
	private static final long serialVersionUID = 9039056478378482872L;

	@Override
	public void accept(SystemReactionTrigger t) {
	    t.trigger();
	}
    }

    /**
     * Adapt the system reaction to perform tasks before and/or after triggering the
     * system reaction. This is useful for cross-cutting concerns, e.g. measuring
     * performance.
     *
     * @param adaptedSystemReaction
     *            the system reaction to replace the standard system reaction.
     */
    public void adaptSystemReaction(Consumer<SystemReactionTrigger> adaptedSystemReaction) {
	this.systemReaction = adaptedSystemReaction;
    }

    /**
     * Restarts the runner, resetting it to its original defaults ("no flow has been
     * run, no step has been run, no use case included").
     */
    public void restart() {
	setLatestStep(null);
	includedUseCases = new LinkedList<>();
	includeSteps = new LinkedList<>();
	includedUseCase = null;
	includeStep = null;
    }

    /**
     * Configures the runner to use the specified use case model. After you called
     * this method, the runner will accept events via {@link #reactTo(Object)}.
     *
     * <p>
     * As a side effect, this method immediately triggers "autonomous system
     * reactions".
     *
     * @param useCaseModel
     *            the model that defines the runner's behavior
     */
    public void run(UseCaseModel useCaseModel) {
	this.useCaseModel = useCaseModel;
	this.userAndSystem = userAndSystem(user != null ? user : useCaseModel.getUserActor());
	this.isRunning = true;
	triggerAutonomousSystemReaction();
    }

    private void triggerAutonomousSystemReaction() {
	reactTo(this);
    }

    private List<Actor> userAndSystem(Actor userActor) {
	return Arrays.asList(userActor, userActor.getUseCaseModel().getSystemActor());
    }

    /**
     * After you called this method, the runner will only react to steps that have
     * explicitly set the specified actor as one of its actors, or that are declared
     * as "autonomous system reactions".
     *
     * <p>
     * As a side effect, calling this method triggers immediately triggers
     * "autonomous system reactions".
     *
     * @param actor
     *            the actor to run as
     * @return this runner, for method chaining with {@link #run(UseCaseModel)}
     */
    public UseCaseModelRunner as(Actor actor) {
	Objects.requireNonNull(actor);

	this.user = actor;
	this.userAndSystem = userAndSystem(user);
	return this;
    }

    /**
     * Returns whether the runner is currently running.
     *
     * @see #run(UseCaseModel)
     * @return true if the runner is running, false otherwise.
     */
    public boolean isRunning() {
	return isRunning;
    }

    /**
     * Stops the runner. It will not be reacting to events, until
     * {@link #run(UseCaseModel)} is called again.
     */
    public void stop() {
	isRunning = false;
    }

    /**
     * Call this method from the frontend to provide several event objects to the
     * runner. For each event object, {@link #reactTo(Object)} is called.
     *
     * @param events
     *            the events to react to
     */
    public void reactTo(Object... events) {
	Objects.requireNonNull(events);

	for (Object event : events) {
	    reactTo(event);
	}
    }

    /**
     * Call this method from the frontend to provide an event object to the runner.
     *
     * <p>
     * If it is running, the runner will then check which steps can react to the
     * event. If a single step can react, the runner will trigger the system
     * reaction for that step. If no step can react, the runner will NOT trigger any
     * system reaction. If more than one step can react, the runner will throw an
     * exception.
     *
     * <p>
     * After that, the runner will trigger "autonomous system reactions".
     *
     * <p>
     * See {@link #getStepsThatCanReactTo(Class)} for a description of what "can
     * react" means.
     *
     * @param <T>
     *            the type of the event object
     * @param event
     *            the event object provided by the frontend
     * @return the use case step whose system reaction was triggered, or else an
     *         empty optional if none was triggered.
     * @throws MoreThanOneStepCanReact
     *             if more than one step can react
     * @throws UnhandledException
     *             if no step can react, and the event is an (in)direct subclass of
     *             Throwable.
     */
    public <T> Optional<Step> reactTo(T event) {
	Objects.requireNonNull(event);

	Optional<Step> latestStepRun = Optional.empty();
	if (isRunning) {
	    Class<? extends Object> currentEventClass = event.getClass();
	    Set<Step> stepsThatCanReact = getStepsThatCanReactTo(currentEventClass);
	    latestStepRun = triggerSystemReactionForSteps(event, stepsThatCanReact);
	}
	return latestStepRun;
    }

    private <T> Optional<Step> triggerSystemReactionForSteps(T event, Collection<Step> steps) {
	Step useCaseStep = null;

	if (steps.size() == 1) {
	    useCaseStep = steps.iterator().next();
	    triggerSystemReactionForStep(event, useCaseStep);
	} else if (steps.size() > 1) {
	    throw new MoreThanOneStepCanReact(steps);
	} else if (event instanceof Throwable) {
	    throw new UnhandledException((Throwable) event);
	}

	return useCaseStep != null ? Optional.of(useCaseStep) : Optional.empty();
    }

    private <T> Step triggerSystemReactionForStep(T event, Step step) {
	if (step.getSystemReaction() == null) {
	    throw new MissingUseCaseStepPart(step, "system");
	}

	setLatestStep(step);
	setStepWithoutAlternativeCondition(null);
	systemReactionTrigger.setupWith(event, step);

	try {
	    systemReaction.accept(systemReactionTrigger);
	} catch (Exception e) {
	    handleException(e);
	}

	continueAfterIncludeStepWhenEndOfIncludedFlowIsReached();
	triggerAutonomousSystemReaction();
	return step;
    }

    private void continueAfterIncludeStepWhenEndOfIncludedFlowIsReached() {
	if (includedUseCase != null && includeStep != null && isAtEndOfIncludedFlow()) {
	    setLatestStep(includeStep);
	    includedUseCase = getUseCaseIncludedBefore();
	    includeStep = getIncludeStepBefore();
	}
    }

    private UseCase getUseCaseIncludedBefore() {
	includedUseCases.pop();
	UseCase includedUseCase = includedUseCases.peek();
	return includedUseCase;
    }

    private FlowStep getIncludeStepBefore() {
	includeSteps.pop();
	FlowStep includeStep = includeSteps.peek();
	return includeStep;
    }

    /**
     * Returns whether at least one step can react to an event of the specified
     * class.
     *
     * @see #getStepsThatCanReactTo(Class)
     * @param eventClass
     *            the specified class
     * @return true if the runner is running and at least one step can react, false
     *         otherwise
     */
    public boolean canReactTo(Class<? extends Object> eventClass) {
	boolean canReact = getStepsThatCanReactTo(eventClass).size() > 0;
	return canReact;
    }

    /**
     * Returns the use case steps in the use case model that can react to the
     * specified event class.
     *
     * <p>
     * A step "can react" if all of the following conditions are met: a) the runner
     * is running b) one of the step's actors matches the actor the runner is run as
     * c) the step's event class is the same or a superclass of the specified event
     * class d) the step has a condition that is true
     *
     * @param eventClass
     *            the class of events
     * @return the steps that can react to the class of events
     */
    public Set<Step> getStepsThatCanReactTo(Class<? extends Object> eventClass) {
	Objects.requireNonNull(eventClass);

	Set<Step> stepsThatCanReact;
	if (isRunning) {
	    Stream<Step> stepStream = useCaseModel.getModifiableSteps().stream();
	    stepsThatCanReact = stepsInStreamThatCanReactTo(eventClass, stepStream);
	} else {
	    stepsThatCanReact = new HashSet<>();
	}

	return stepsThatCanReact;
    }

    /**
     * Gets the steps that can react from the specified stream, rather than from the
     * whole use case model.
     *
     * @see #getStepsThatCanReactTo(Class)
     * @param eventClass
     *            eventClass the class of events
     * @param stepStream
     *            the stream of steps
     * @return the subset of steps that can react to the class of events
     */
    Set<Step> stepsInStreamThatCanReactTo(Class<? extends Object> eventClass, Stream<Step> stepStream) {
	Set<Step> steps;
	steps = stepStream.filter(step -> stepActorIsRunActor(step))
		.filter(step -> stepEventClassIsSameOrSuperclassAsEventClass(step, eventClass))
		.filter(step -> hasTrueCondition(step)).filter(step -> isStepInIncludedUseCaseIfPresent(step))
		.filter(getStepWithoutAlternativeCondition().orElse(s -> true)).collect(Collectors.toSet());
	return steps;
    }

    private boolean stepActorIsRunActor(Step step) {
	Actor[] stepActors = step.getActors();
	if (stepActors == null) {
	    throw (new MissingUseCaseStepPart(step, "actor"));
	}
	boolean stepActorIsRunActor = Stream.of(stepActors).anyMatch(stepActor -> userAndSystem.contains(stepActor));
	return stepActorIsRunActor;
    }

    private boolean stepEventClassIsSameOrSuperclassAsEventClass(Step useCaseStep, Class<?> currentEventClass) {
	Class<?> stepEventClass = useCaseStep.getUserEventClass();
	return stepEventClass.isAssignableFrom(currentEventClass);
    }

    private boolean hasTrueCondition(Step step) {
	Predicate<UseCaseModelRunner> condition = step.getCondition();
	boolean result = condition.test(this);
	return result;
    }

    private boolean isStepInIncludedUseCaseIfPresent(Step step) {
	boolean result = true;
	if (includedUseCase != null) {
	    result = includedUseCase.equals(step.getUseCase());
	}
	return result;
    }

    /**
     * Overwrite this method to control what happens exactly when an exception is
     * thrown by a system reaction. The behavior implemented in runner: the
     * exception is provided as an event object to the runner, by calling
     * {@link #reactTo(Object)}. You may replace this with a more sophisticated
     * behavior, that for example involves some kind of logging.
     *
     * @param e
     *            the exception that has been thrown by the system reaction
     */
    protected void handleException(Exception e) {
	reactTo(e);
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
     *            the latest step run
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
	return getLatestStep().filter(step -> step instanceof FlowStep).map(step -> ((FlowStep)step).getFlow());
    }

    public void setStepWithoutAlternativeCondition(Predicate<Step> condition) {
	stepWithoutAlternativeCondition = condition;
    }

    private Optional<Predicate<Step>> getStepWithoutAlternativeCondition() {
	return Optional.ofNullable(stepWithoutAlternativeCondition);
    }

    public void includeUseCase(UseCase includedUseCase, FlowStep includeStep) {
	this.includedUseCase = includedUseCase;
	this.includeStep = includeStep;

	includedUseCases.push(includedUseCase);
	includeSteps.push(includeStep);
    }

    private boolean isAtEndOfIncludedFlow() {
	Optional<FlowStep> lastStepOfRunningFlow = getLatestStep().map(ls -> getLastStepOf(((FlowStep)ls).getFlow()));
	boolean result = getLatestStep()
		.map(ls -> ls.getUseCase().equals(includedUseCase) && ls.equals(lastStepOfRunningFlow.get()))
		.orElse(false);
	return result;
    }

    private FlowStep getLastStepOf(Flow flow) {
	List<FlowStep> stepsOfFlow = flow.getSteps();
	int lastStepIndex = stepsOfFlow.size() - 1;
	FlowStep lastStepOfFlow = stepsOfFlow.get(lastStepIndex);
	return lastStepOfFlow;
    }
}
