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

import org.requirementsascode.exception.MissingUseCaseStepPart;
import org.requirementsascode.exception.MoreThanOneStepCanReact;
import org.requirementsascode.exception.UnhandledException;


/**
 * A use case runner is a highly configurable use case controller that receives events
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
	private Optional<Actor> user;
	private List<Actor> userAndSystem;
	
	private UseCaseModel useCaseModel;
	private Optional<UseCaseStep> latestStep;
	private Optional<UseCaseFlow> latestFlow;
	private boolean isRunning;
	private SystemReactionTrigger systemReactionTrigger;
	private Consumer<SystemReactionTrigger> systemReaction;
	private Optional<Predicate<UseCaseStep>> stepWithoutAlternativePredicate;

	/**
	 * Constructor for creating a use case runner with standard system reaction,
	 * that is: the system reaction, as defined in the use case step, simply accepts
	 * an event.
	 */
	public UseCaseRunner() {
		this.user = Optional.empty();
		this.systemReactionTrigger = new SystemReactionTrigger();
		this.stepWithoutAlternativePredicate = Optional.empty();
		
		adaptSystemReaction(systemReactionTrigger -> systemReactionTrigger.trigger());
		restart();
	}

	/**
	 * Adapt the system reaction to perform tasks before and/or after triggering the
	 * system reaction. This is useful for cross-cutting concerns, e.g. measuring performance.
	 * 
	 * @param adaptedSystemReaction the system reaction to replace the standard system reaction.
	 */
	public void adaptSystemReaction(Consumer<SystemReactionTrigger> adaptedSystemReaction) {
		this.systemReaction = adaptedSystemReaction;
	}
	
	/**
	 * Restarts the runner, setting latest flow and latest step 
	 * to its original defaults ("no flow has been run, no step has been run").
	 */
	public void restart() {
		setLatestStep(Optional.empty());
	}
	
	/**
	 * Configures the runner to use the specified use case model.
	 * After you called this method, the runner will accept events via {@link #reactTo(Object)}.
	 * 
	 * As a side effect, this method immediately triggers "autonomous system reactions".
	 * 
	 * @param useCaseModel the model that defines the runner's behavior
	 */
	public void run(UseCaseModel useCaseModel) {
		this.useCaseModel = useCaseModel;
		
		Actor userActor = user.orElse(useCaseModel.userActor());
		userAndSystem = Arrays.asList(userActor, useCaseModel.systemActor());

		this.isRunning = true;
		triggerAutonomousSystemReaction();
	}
	
	/**
	 * After you called this method, the runner will only react to steps 
	 * that have explicitly set the specified actor as one of its actors, 
	 * or that are declared as "autonomous system reactions".
	 * 
	 * As a side effect, calling this method triggers immediately triggers "autonomous system reactions".
	 * 
	 * @param actor the actor to run as 
	 * @return this runner, for method chaining with {@link #run(UseCaseModel)}
	 */
	public UseCaseRunner as(Actor actor) {
		Objects.requireNonNull(actor);
		
		user = Optional.of(actor);
		return this;
	}
	
	private void triggerAutonomousSystemReaction() {
		reactTo(this);
	}
	
	/**
	 * Returns whether the runner is currently running.
	 * @see #run(UseCaseModel)
	 * 
	 * @return true if the runner is running, false otherwise.
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Stops the runner. It will not be reacting to events,
	 * until {@link #run(UseCaseModel)} is called again.
	 */
	public void stop() {	
		isRunning = false;
	}
	
	/**
	 * Call this method from the frontend to provide several event objects to the use case runner.
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
	 * Call this method from the frontend to provide an event object to the use case runner.
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
			Set<UseCaseStep> stepsThatCanReact = stepsThatCanReactTo(currentEventClass);
			latestStepRun = triggerSystemReactionForSteps(event, stepsThatCanReact);
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
				.filter(stepWithoutAlternativePredicate.orElse(s -> true))
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
		if(useCaseStep.system() == null){
			throw new MissingUseCaseStepPart(useCaseStep, "system");
		}
		
		setLatestStep(Optional.of(useCaseStep));
		
		try {
			stepWithoutAlternativePredicate = Optional.empty();
			systemReactionTrigger.setupWith(event, useCaseStep);
			systemReaction.accept(systemReactionTrigger);
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
		UseCaseStepAs actorPart = useCaseStep.as();
		if(actorPart == null){
			throw(new MissingUseCaseStepPart(useCaseStep, "actor"));
		}
		
		Actor[] stepActors = actorPart.actors();
		boolean stepActorIsRunActor = 
			Stream.of(stepActors).anyMatch(stepActor -> userAndSystem.contains(stepActor));
		return stepActorIsRunActor;
	}
	
	private boolean stepEventClassIsSameOrSuperclassAsEventClass(UseCaseStep useCaseStep, Class<?> currentEventClass) {
		Class<?> stepEventClass = useCaseStep.user().eventClass();
		return stepEventClass.isAssignableFrom(currentEventClass);
	}
	
	private boolean hasTruePredicate(UseCaseStep useCaseStep) {
		Predicate<UseCaseRunner> predicate = useCaseStep.predicate();
		boolean result = predicate.test(this);
		return result;
	}
	
	/**
	 * Returns the latest step that has been run by this UseCaseRunner.
	 * 
	 * @return the latest step run
	 */
	public Optional<UseCaseStep> latestStep() {
		return latestStep;
	}
	
	/**
	 * Sets the latest step run by the use case runner.
	 * Implicitly, this also sets the latest flow to the flow 
	 * that contains the step.
	 * 
	 * Use this method if you want to restore some previous state,
	 * normally you should influence the behavior of the runner by
	 * calling {@link #reactTo(Object)}.
	 * 
	 * @param latestStep the latest step run
	 */
	public void setLatestStep(Optional<UseCaseStep> latestStep) {		
		this.latestStep = latestStep;
		this.latestFlow = latestStep.map(s -> s.flow());
	}
	
	/**
	 * Returns the flow the latest step that has been run by this UseCaseRunner
	 * is contained in.
	 * 
	 * @return the latest flow run
	 */
	public Optional<UseCaseFlow> latestFlow() {
		return latestFlow;
	}
	
	void setStepWithoutAlternativePredicate(Predicate<UseCaseStep> stepWithoutAlternativePredicate){
		this.stepWithoutAlternativePredicate = Optional.of(stepWithoutAlternativePredicate);
	}
}
