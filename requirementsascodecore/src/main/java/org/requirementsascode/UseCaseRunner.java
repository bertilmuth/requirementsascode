package org.requirementsascode;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.requirementsascode.UseCaseStep.ActorPart;
import org.requirementsascode.exception.MissingUseCaseStepPart;
import org.requirementsascode.exception.MoreThanOneStepCanReact;
import org.requirementsascode.exception.UnhandledException;


/**
 * A use case runner is a highly configurable use case controller that received events
 * from the frontend and conditionally calls methods on the backend.
 * 
 * In requirementsascode, a use case runner is the only way the frontend communicates with the
 * backend. The use case runner is configured by the use case model it owns.
 * Each real user needs an instance of a use case runner, as the runner determines the journey
 * of the user through the use cases.
 * 
 *  
 */
public class UseCaseRunner {
	private List<Actor> actorsToRunAs;
	private UseCaseModel useCaseModel;
	private Optional<UseCaseStep> latestStep;
	private Optional<UseCaseFlow> latestFlow;
	private boolean isRunning;
	private SystemReactionTrigger systemReactionTrigger;
	private Consumer<SystemReactionTrigger> adaptedSystemReaction;

	/**
	 * Constructor for creating a use case runner with standard system reaction,
	 * that is: the system reaction, as defined in the use case step, simply accepts
	 * an event.
	 */
	public UseCaseRunner() {
		this(systemReactionTrigger -> systemReactionTrigger.trigger());
	}
	
	/**
	 * Constructor for creating a use case runner with an adapted system reaction.
	 * 
	 * @param adaptedSystemReaction the adapted system reaction.
	 */
	public UseCaseRunner(Consumer<SystemReactionTrigger> adaptedSystemReaction) {
		this.isRunning = false;
		this.useCaseModel = new UseCaseModel(this);
		this.systemReactionTrigger = new SystemReactionTrigger();
		this.adaptedSystemReaction = adaptedSystemReaction;
		restart();
	}
	
	/**
	 * Returns the use case model for the runner,
	 * which configures the behavior of the runner.
	 * 
	 * @return the use case model
	 */
	public UseCaseModel useCaseModel() {
		return useCaseModel;
	}
	
	/**
	 * Restarts the runner, setting latest flow and latest step 
	 * to its original defaults ("no flow has been run, no step has been run").
	 */
	public void restart() {
		setLatestFlow(Optional.empty());
		setLatestStep(Optional.empty());
	}
	
	/**
	 * Runs the use case model with the default user, see {@link UseCaseModel#userActor()}.
	 * 
	 * From now on (until called the next time), the runner will only trigger system reactions
	 * of steps that have no explicit actor or that are "autonomous system reactions".
	 * 
	 * Calling this method activates reacting to events via {@link #reactTo(Object)}.
	 * 
	 * As a side effect, calling this method triggers immediately triggers "autonomous system reactions".
	 * 
	 */
	public void run() {
		runAs(useCaseModel.userActor());
	}
	
	private void triggerAutonomousSystemReaction() {
		reactTo(new SystemEvent());
	}

	/**
	 * Runs the use case model with the specified actor.
	 * 
	 * From now on (until called the next time), the runner will only trigger system reactions
	 * of steps that have explicitly set this actor, or that are "autonomous system reactions".
	 * Calling this method activates reacting to events via {@link #reactTo(Object)}.
	 * 
	 * As a side effect, calling this method triggers immediately triggers "autonomous system reactions".
	 * 
	 * @param actor the actor to run as
	 */
	public void runAs(Actor actor) {
		Objects.requireNonNull(actor);
		
		isRunning = true;
		actorsToRunAs = Arrays.asList(actor, useCaseModel.systemActor());
		triggerAutonomousSystemReaction();
	}
	
	/**
	 * Needs to be called by the frontend to provide several event objects to the use case runner.
	 * For each event object, {@link #reactTo(Object)} is called.
	 * 
	 * @param events the events to react to
	 */
	public void reactTo(Object... events) {
		Objects.requireNonNull(events);
		
		for (Object event : events) {
			reactTo(event);
		}		
	}

	/**
	 * Needs to be called by the frontend to provide an event object to the use case runner.
	 * 
	 * If it is running, the runner will then check which steps can react to the event. 
	 * If a single step can react, the runner will trigger the system reaction for that step.
	 * If no step can react, the runner will NOT trigger any system reaction.
	 * If more than one step can react, the runner will throw an exception.
	 * 
	 * After that, the runner will trigger "autonomous system reactions".
	 * 
	 * See {@link #stepsThatCanReactTo(Class)} for a description of what "can react" means.
	 * 
	 * @param <T> the type of the event object
	 * @param event the event object provided by the frontend
	 * @return the use case step whose system reaction was triggered, or else an empty optional if none was triggered.
	 * @throws MoreThanOneStepCanReact if more than one step can react
	 * @throws UnhandledException if no step can react, and the event is an (in)direct subclass of Throwable.
	 */
	public <T> Optional<UseCaseStep> reactTo(T event) {
		Objects.requireNonNull(event);
		
		Optional<UseCaseStep> latestStepRun = Optional.empty();
		if(isRunning){
			Class<? extends Object> currentEventClass = event.getClass();
			Set<UseCaseStep> reactingUseCaseSteps = stepsThatCanReactTo(currentEventClass);
			latestStepRun = triggerSystemReactionForSteps(event, reactingUseCaseSteps);
		}
		return latestStepRun;
	}
	
	/**
	 * Returns whether at least one step can react to an event of the specified class.
	 * @see #stepsThatCanReactTo(Class)
	 * 
	 * @param eventClass the specified class
	 * @return true if the runner is running and at least one step can react, false otherwise
	 */
	public boolean canReactTo(Class<? extends Object> eventClass) {
		boolean canReact = stepsThatCanReactTo(eventClass).size() > 0;
		return canReact;
	}

	/**
	 * Returns the use case steps in the use case model that can react to the specified event class.
	 * 
	 * A step "can react" if all of the following conditions are met:
	 * a) the UseCaseRunner is running
	 * b) one of the step's actors matches the actor the runner is run as
	 * c) the step's event class is the same or a superclass of the specified event class 
	 * d) the step has a predicate that is true
	 * 
	 * @param eventClass the class of events 
	 * @return the steps that can react to the class of events
	 */
	public Set<UseCaseStep> stepsThatCanReactTo(Class<? extends Object> eventClass) {
		Objects.requireNonNull(eventClass);
		
		Stream<UseCaseStep> stepStream = useCaseModel.steps().stream();
		Set<UseCaseStep> steps = stepsInStreamThatCanReactTo(eventClass, stepStream);
		return steps;
	}
	
	/**
	 * Gets the steps that can react from the specified stream, rather than from the whole
	 * use case model.
	 * 
	 * @see #stepsThatCanReactTo(Class)
	 * @param eventClass eventClass the class of events
	 * @param stepStream the stream of steps
	 * @return the subset of steps that can react to the class of events
	 */
	Set<UseCaseStep> stepsInStreamThatCanReactTo(Class<? extends Object> eventClass, Stream<UseCaseStep> stepStream) {
		Set<UseCaseStep> steps;
		if(isRunning){
			steps = stepStream
				.filter(step -> stepActorIsRunActor(step))
				.filter(step -> stepEventClassIsSameOrSuperclassAsEventClass(step, eventClass))
				.filter(step -> hasTruePredicate(step))
				.collect(Collectors.toSet());
		} else {
			steps = new HashSet<>();
		}

		return steps;
	}

	private <T> Optional<UseCaseStep> triggerSystemReactionForSteps(T event, Collection<UseCaseStep> useCaseSteps) {
		UseCaseStep useCaseStep = null;

		if(useCaseSteps.size() == 1){
			useCaseStep = useCaseSteps.iterator().next();
			triggerSystemReactionForStep(event, useCaseStep);
		} else if(useCaseSteps.size() > 1){
			throw new MoreThanOneStepCanReact(useCaseSteps);
		} else if(event instanceof Throwable){
			throw new UnhandledException((Throwable)event);
		}
		
		return useCaseStep != null? Optional.of(useCaseStep) : Optional.empty();
	}
	
	private <T> UseCaseStep triggerSystemReactionForStep(T event, UseCaseStep useCaseStep) {
		if(useCaseStep.systemPart() == null){
			throw new MissingUseCaseStepPart(useCaseStep, "system");
		}
		
		setLatestStep(Optional.of(useCaseStep));
		setLatestFlow(Optional.of(useCaseStep.flow()));
		
		try {
			systemReactionTrigger.setupWithEventAndUseCaseStep(event, useCaseStep);
			adaptedSystemReaction.accept(systemReactionTrigger);
		} 
		catch (Exception e) { 
			handleException(e);
		} 
		
		triggerAutonomousSystemReaction();

		return useCaseStep;
	}

	/**
	 * Overwrite this method to control what happens exactly when aa exception is thrown by a system reaction.
	 * The behavior implemented in UseCaseRunner: the exception is provided as an event object
	 * to the UseCaseRunner, by calling {@link #reactTo(Object)}.
	 * You may replace this with a more sophisticated behavior, that for example involves some kind of logging.
	 * 
	 * @param e the exception that has been thrown by the system reaction
	 */
	protected void handleException(Exception e) {
		reactTo(e);
	}

	private boolean stepActorIsRunActor(UseCaseStep useCaseStep) {
		ActorPart actorPart = useCaseStep.actorPart();
		if(actorPart == null){
			throw(new MissingUseCaseStepPart(useCaseStep, "actor"));
		}
		
		Actor[] stepActors = actorPart.actors();
		boolean stepActorIsRunActor = 
			Stream.of(stepActors).anyMatch(stepActor -> actorsToRunAs.contains(stepActor));
		return stepActorIsRunActor;
	}
	
	private boolean stepEventClassIsSameOrSuperclassAsEventClass(UseCaseStep useCaseStep, Class<?> currentEventClass) {
		Class<?> stepEventClass = useCaseStep.eventPart().eventClass();
		return stepEventClass.isAssignableFrom(currentEventClass);
	}
	
	private boolean hasTruePredicate(UseCaseStep useCaseStep) {
		Predicate<UseCaseRunner> predicate = useCaseStep.predicate();
		boolean result = predicate.test(this);
		return result;
	}
	
	/**
	 * Returns the lastest step that has been run by this UseCaseRunner.
	 * 
	 * @return the latest step run
	 */
	public Optional<UseCaseStep> latestStep() {
		return latestStep;
	}
	
	/**
	 * Sets the latest step run by the use case runner.
	 * Use this method if you want to restore some previous state,
	 * normally you should influence the behavior of the runner by
	 * calling {@link #reactTo(Object)}.
	 * 
	 * @param latestStep the latest step run
	 */
	public void setLatestStep(Optional<UseCaseStep> latestStep) {		
		this.latestStep = latestStep;
	}
	
	/**
	 * Returns the flow the lastest step that has been run by this UseCaseRunner
	 * is contained in.
	 * 
	 * @return the latest flow run
	 */
	public Optional<UseCaseFlow> latestFlow() {
		return latestFlow;
	}
	
	/**
	 * Sets the latest flow run by the use case runner.
	 * Use this method if you want to restore some previous state,
	 * normally you should influence the behavior of the runner by
	 * calling {@link #reactTo(Object)}.
	 * 
	 * @param latestFlow the latest flow run
	 */
	public void setLatestFlow(Optional<UseCaseFlow> latestFlow) {
		this.latestFlow = latestFlow;
	}
}
