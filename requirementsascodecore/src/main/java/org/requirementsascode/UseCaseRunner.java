package org.requirementsascode;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.requirementsascode.UseCaseStep.ActorPart;
import org.requirementsascode.exception.MissingUseCaseStepPartException;
import org.requirementsascode.exception.MoreThanOneStepCouldReactException;


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

	/**
	 * Constructor for creating a use case runner.
	 */
	public UseCaseRunner() {
		this.isRunning = false;
		this.useCaseModel = new UseCaseModel(this);
		as(useCaseModel.getUserActor()).restart();
	}
	
	/**
	 * Returns the use case model for the runner,
	 * which configures the behavior of the runner.
	 * 
	 * @return the use case model
	 */
	public UseCaseModel getUseCaseModel() {
		return useCaseModel;
	}
	
	/**
	 * Restarts the runner, setting latest flow and latest step 
	 * to its original defaults ("no flow has been run, no step has been run").
	 */
	public void restart() {
		this.latestFlow = Optional.empty();
		this.latestStep = Optional.empty();
	}
	
	/**
	 * Runs the use case model. Calling this method triggers 
	 * "autonomous system reactions", and activates reacting to events
	 * via {@link #reactTo(Object)}.
	 * 
	 * @see UseCaseStep#system(Runnable)
	 * @return the runner itself, for method chaining, e.g. with {@link #as(Actor)}
	 */
	public UseCaseRunner run() {
		isRunning = true;
		triggerAutonomousSystemReaction();
		return this;
	}
	
	private void triggerAutonomousSystemReaction() {
		reactTo(new SystemEvent());
	}

	/**
	 * Defines the actor the runner is run as.
	 * 
	 * After calling this method, the runner will only trigger system reactions
	 * of steps that have explicitly set this actor, or that are "autonomous system reactions".
	 * 
	 * @see UseCaseStep#actor(Actor...)
	 * @see UseCaseStep#system(Runnable) 
	 * @param actor the actor to run as
	 * @return the use case runner
	 */
	public UseCaseRunner as(Actor actor) {
		Objects.requireNonNull(actor);
		
		actorsToRunAs = Arrays.asList(actor, useCaseModel.getSystemActor());
		return this;
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
	 * The runner will then check which steps could react to the event. 
	 * If a single step could react, the runner will trigger the system reaction for that step.
	 * If no step could react, the runner will NOT trigger any system reaction.
	 * If more than one step could react, the runner will throw an exception.
	 * 
	 * After that, the runner will trigger "autonomous system reactions".
	 * 
	 * See {@link #getStepsThatCouldReactTo(Class)} for a description of what "could react" means.
	 * 
	 * @param <T> the type of the event object
	 * @param event the event object provided by the frontend
	 * @return the use case step whose system reaction was triggered, or null if none was triggered.
	 * @throws MoreThanOneStepCouldReactException the exception that occurs if more than one step could react
	 */
	public <T> UseCaseStep reactTo(T event) {
		Objects.requireNonNull(event);
		
		UseCaseStep latestStepRun = null;
		if(isRunning){
			Class<? extends Object> currentEventClass = event.getClass();
			Set<UseCaseStep> reactingUseCaseSteps = getStepsThatCouldReactTo(currentEventClass);
			latestStepRun = triggerSystemReaction(event, reactingUseCaseSteps);
		}
		return latestStepRun;
	}

	/**
	 * Returns the use case steps in the use case model that could react to the specified event class.
	 * 
	 * A step "could react" if all of the following conditions are met:
	 * a) the step's actor matches the actor the runner is run as
	 * b) the step's event class is the same or a superclass of the specified event class 
	 * c) the condition of the step is fulfilled, that is: its predicate is true
	 * 
	 * @param eventClass the class of events 
	 * @return the steps that could react to the class of events
	 */
	public Set<UseCaseStep> getStepsThatCouldReactTo(Class<? extends Object> eventClass) {
		Objects.requireNonNull(eventClass);
		
		Stream<UseCaseStep> stepStream = useCaseModel.getSteps().stream();
		Set<UseCaseStep> enabledSteps = getSubsetOfStepsThatCouldReact(eventClass, stepStream);
		return enabledSteps;
	}
	
	/**
	 * Gets the steps that could react from the specified stream, rather than from the whole
	 * use case model.
	 * 
	 * @see #getStepsThatCouldReactTo(Class)
	 * @param eventClass eventClass the class of events
	 * @param stepStream the stream of steps
	 * @return the subset of steps that could react to the class of events
	 */
	Set<UseCaseStep> getSubsetOfStepsThatCouldReact(Class<? extends Object> eventClass, Stream<UseCaseStep> stepStream) {
		Set<UseCaseStep> enabledSteps = stepStream
			.filter(step -> stepActorIsRunActor(step))
			.filter(step -> stepEventClassIsSameOrSuperclassAsEventClass(step, eventClass))
			.filter(step -> isConditionFulfilled(step))
			.collect(Collectors.toSet());
		return enabledSteps;
	}

	private <T> UseCaseStep triggerSystemReaction(T event, Collection<UseCaseStep> useCaseSteps) {
		UseCaseStep useCaseStep = null;

		if(useCaseSteps.size() == 1){
			useCaseStep = useCaseSteps.iterator().next();
			triggerSystemReactionAndHandleException(event, useCaseStep);
		} else if(useCaseSteps.size() > 1){
			throw new MoreThanOneStepCouldReactException(useCaseSteps);
		}
		
		return useCaseStep;
	}
	
	private <T> UseCaseStep triggerSystemReactionAndHandleException(T event, UseCaseStep useCaseStep) {
		if(useCaseStep.getSystemPart() == null){
			throw new MissingUseCaseStepPartException(useCaseStep, "system");
		}
		
		setLatestStep(Optional.of(useCaseStep));
		setLatestFlow(Optional.of(useCaseStep.getFlow()));
		
		try {
			@SuppressWarnings("unchecked")
			Consumer<T> systemReaction = 
				(Consumer<T>) useCaseStep.getSystemPart().getSystemReaction();
			triggerSystemReaction(event, systemReaction);
		} 
		catch (Exception e) { 
			handleException(e);
		} 
		
		triggerAutonomousSystemReaction();

		return useCaseStep;
	}
	
	/**
	 * Overwrite this method to control what happens exactly when a system reaction is triggered.
	 * The behavior implemented in UseCaseRunner: the consumer that is the system reaction simply accepts the event.
	 * You may replace this with a more sophisticated behavior, that for example involves some kind of logging.
	 * 
	 * @param <T> the type of event the system reacts to
	 * @param event the event that is passed to the system reation
	 * @param systemReaction the system reaction that accepts the event
	 */
	protected <T> void triggerSystemReaction(T event, Consumer<T> systemReaction) {
		systemReaction.accept(event);
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
		ActorPart actorPart = useCaseStep.getActorPart();
		if(actorPart == null){
			throw(new MissingUseCaseStepPartException(useCaseStep, "actor"));
		}
		
		Actor[] stepActors = actorPart.getActors();
		boolean stepActorIsRunActor = 
			Stream.of(stepActors).anyMatch(stepActor -> actorsToRunAs.contains(stepActor));
		return stepActorIsRunActor;
	}
	
	private boolean stepEventClassIsSameOrSuperclassAsEventClass(UseCaseStep useCaseStep, Class<?> currentEventClass) {
		Class<?> stepEventClass = useCaseStep.getEventPart().getEventClass();
		return stepEventClass.isAssignableFrom(currentEventClass);
	}
	
	private boolean isConditionFulfilled(UseCaseStep useCaseStep) {
		Predicate<UseCaseRunner> predicate = useCaseStep.getPredicate();
		boolean result = predicate.test(this);
		return result;
	}
	
	/**
	 * Returns the lastest step that has been run by this UseCaseRunner.
	 * 
	 * @return the latest step run
	 */
	public Optional<UseCaseStep> getLatestStep() {
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
	public Optional<UseCaseFlow> getLatestFlow() {
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
