package org.requirementsascode;

import static org.requirementsascode.SystemReaction.continueAfterStepAndCurrentFlowCanBeReentered;
import static org.requirementsascode.SystemReaction.continueAfterStepAndCurrentFlowCantBeReentered;
import static org.requirementsascode.UseCaseStepPredicate.afterStep;
import static org.requirementsascode.UseCaseStepPredicate.isRunnerAtStart;
import static org.requirementsascode.UseCaseStepPredicate.noOtherStepCouldReactThan;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.NoSuchElementInUseCase;

/**
 * A use case step, as part of a use case.
 * The use case steps define the behavior of the use case. 
 * 
 * A use case step is the core class of requirementsascode, providing all the necessary configuration 
 * information to the {@link UseCaseRunner} to cause the system to react to events.
 * A use case step has a predicate, which defines the complete condition that needs to be fulfilled
 * to enable the step, given a matching event occurs.  
 * 
 * @author b_muth
 *
 */
public class UseCaseStep extends UseCaseModelElement{
	static final String REPEAT_STEP_POSTFIX = " (#REPEAT)";
	static final String NEXT_LOOP_ITERATION_STEP_POSTFIX = " (#NEXT)";
	
	private UseCaseFlow useCaseFlow;
	private Optional<UseCaseStep> previousStepInFlow;
	private Predicate<UseCaseRunner> predicate;
	
	private ActorPart actorPart;
	private EventPart<?> eventPart;
	private SystemPart<?> systemPart;
	
	/**
	 * Creates a use case step with the specified name that 
	 * belongs to the specified use case flow.
	 * 
	 * @param stepName the name of the step to be created
	 * @param useCaseFlow the use case flow that will contain the new use case
	 * @param previousStepInFlow the step created before the step in its flow, or else an empty optional if it is the first step in its flow
	 * @param predicate the complete predicate of the step, or else an empty optional which implicitly means: {@link #inSequenceUnlessInterrupted()} 
	 */
	UseCaseStep(String stepName, UseCaseFlow useCaseFlow, Optional<UseCaseStep> previousStepInFlow, Optional<Predicate<UseCaseRunner>> predicate) {
		super(stepName, useCaseFlow.useCaseModel());
		Objects.requireNonNull(previousStepInFlow);
		Objects.requireNonNull(predicate);
		
		this.useCaseFlow = useCaseFlow;
		this.previousStepInFlow = previousStepInFlow;
		this.predicate = predicate.orElse(inSequenceUnlessInterrupted());		
	}
	
	/**
	 * Defines which actors (i.e. user groups) can cause the system to react to the event of this step.  
	 * 
	 * Note: in order for the system to react to one the specified actors,
	 * {@link UseCaseRunner#runAs(Actor)} needs to be called
	 * before {@link UseCaseRunner#reactTo(Object)}.
	 * 
	 * @param actors the actors that defines the user groups
	 * @return the created actor part of this step
	 */
	public UseCaseStep.ActorPart as(Actor... actors) {
		Objects.requireNonNull(actors);
		
		actorPart = new ActorPart(actors);
		return actorPart;
	}
	
	/**
	 * Defines an "autonomous system reaction",
	 * meaning the system will react when the step's predicate is true, without
	 * needing an event provided via {@link UseCaseRunner#reactTo(Object)}.
	 * 
	 * As an implicit side effect, the step is connected to the default system
	 * actor (see {@link UseCaseModel#systemActor()}).
	 * As another side effect, the step handles the default
	 * system event. Default system events are raised by the 
	 * use case runner itself, causing "autonomous system reactions".
	 * 
	 * @param systemReaction the autonomous system reaction
	 * @return the created system part of this step
	 */
	public UseCaseStep.SystemPart<?> system(Runnable systemReaction) {
		Objects.requireNonNull(systemReaction);
		
		Actor systemActor = useCaseModel().systemActor(); 

		UseCaseStep.SystemPart<?> systemPart = as(systemActor).system(systemReaction);
		
		return systemPart;
	}
	
	/**
	 * Makes the use case runner continue after the specified step.
	 * Note that the current flow is NOT entered immediately, even if it's condition is true.
	 * 
	 * @param stepName name of the step to continue after, in this use case.
	 * @return the use case this step belongs to, to ease creation of further flows
	 * @throws NoSuchElementInUseCase if no step with the specified stepName is found in the current use case
	 */
	public UseCase continueAfter(String stepName) {
		Objects.requireNonNull(stepName);
		
		system(continueAfterStepAndCurrentFlowCantBeReentered(useCase(), stepName));
		return useCase();
	}
	
	/**
	 * Makes the use case runner start from the beginning, when no
	 * flow and step has been run.
	 * 
	 * @see UseCaseRunner#restart()
	 * @return the use case this step belongs to, to ease creation of further flows
	 */
	public UseCase restart() {
		system(() -> useCaseModel().useCaseRunner().restart());
		return useCase();
	}
	
	/**
	 * Defines the class of user event objects that this step accepts,
	 * so that they can cause a system reaction when the step's predicate is true.
	 * 
	 * Given that the step's predicate is true, the system reacts to objects that are
	 * instances of the specified class or instances of any direct or indirect subclass
	 * of the specified class.
	 * 
	 * As an implicit side effect, the step is connected to the default user
	 * actor (see {@link UseCaseModel#userActor()}).
	 * 
	 * Note: you must provide the event objects at runtime by calling {@link UseCaseRunner#reactTo(Object)}
	 * 
	 * @param eventClass the class of events the system reacts to in this step
	 * @param <T> the type of the class
	 * @return the created event part of this step
	 */
	public <T> EventPart<T> user(Class<T> eventClass) {
		Objects.requireNonNull(eventClass);

		Actor userActor = useCaseModel().userActor();
		EventPart<T> eventPart = as(userActor).user(eventClass);
		
		return eventPart;
	}
	
	/**
	 * Defines the class of system event objects or exceptions that this step accepts,
	 * so that they can cause a system reaction when the step's predicate is true.
	 * 
	 * Given that the step's predicate is true, the system reacts to objects that are
	 * instances of the specified class or instances of any direct or indirect subclass
	 * of the specified class.
	 * 
	 * As an implicit side effect, the step is connected to the default system
	 * actor (see {@link UseCaseModel#systemActor()})..
	 * 
	 * Note: you must provide the event objects at runtime by calling {@link UseCaseRunner#reactTo(Object)}
	 * 
	 * @param eventOrExceptionClass the class of events the system reacts to in this step
	 * @param <T> the type of the class
	 * @return the created event part of this step
	 */
	public <T> EventPart<T> handle(Class<T> eventOrExceptionClass) {
		Objects.requireNonNull(eventOrExceptionClass);

		Actor systemActor = useCaseModel().systemActor();
		EventPart<T> eventPart = as(systemActor).user(eventOrExceptionClass);
		
		return eventPart;
	}
	
	/**
	 * Returns the use case flow this step is part of.
	 * 
	 * @return the containing use case flow
	 */
	public UseCaseFlow flow() {
		return useCaseFlow;
	}
	
	/**
	 * Returns the use case this step is part of.
	 * 
	 * @return the containing use case
	 */
	public UseCase useCase() {
		return flow().useCase();
	}
	
	/**
	 * Returns the step created before this step in its flow, 
	 * or else an empty optional if this step is the first step in its flow.
	 * 
	 * @return the previous step in this step's flow
	 */
	public Optional<UseCaseStep> previousStepInFlow() {
		return previousStepInFlow;
	}
	
	/**
	 * Returns the actor part of this step.
	 * 
	 * @return the actor part
	 */
	public ActorPart actorPart() {
		return actorPart;
	}
	
	/**
	 * Returns the event part of this step.
	 * 
	 * @return the event part
	 */
	public EventPart<?> eventPart() {
		return eventPart;
	}
	
	/**
	 * Returns the system part of this step.
	 * 
	 * @return the system part
	 */
	public SystemPart<?> systemPart() {
		return systemPart;
	}
	
	/**
	 * Returns the predicate of this step
	 * 
	 * @return the predicate of this step
	 */
	public Predicate<UseCaseRunner> predicate() {
		return predicate;
	} 
	
	/**
	 * This predicate makes sure that use case steps following the first step
	 * in a flow are executed in sequence, unless the first step of an 
	 * alternative flow interrupts the flow.
	 * 
	 * @return the predicate for running steps in sequence
	 */
	private Predicate<UseCaseRunner> inSequenceUnlessInterrupted() {
		Predicate<UseCaseRunner> afterPreviousStep = 
			previousStepInFlow.map(s -> afterStep(s)).orElse(isRunnerAtStart());
		return afterPreviousStep.and(noOtherStepCouldReactThan(this));
	}
	
	/*
	 * The part of the step that contains a reference to the actor
	 * that is allowed to trigger a system reaction for this step.
	 * 
	 * @author b_muth
	 *
	 */
	public class ActorPart{
		private Actor[] actors;
		
		private ActorPart(Actor... actor) {
			this.actors = actor;
			connectActorsToThisStep(actors);		
		}

		private void connectActorsToThisStep(Actor[] actors) {
			for (Actor actor : actors) {
				actor.newStep(useCase(), UseCaseStep.this);
			}
		}
		
		/**
		 * Defines the class of user event objects that this step accepts,
		 * so that they can cause a system reaction when the step's predicate is true.
		 * 
		 * Given that the step's predicate is true, the system reacts to objects that are
		 * instances of the specified class or instances of any direct or indirect subclass
		 * of the specified class.
		 * 
		 * Note: you must provide the event objects at runtime by calling {@link UseCaseRunner#reactTo(Object)}
		 * 
		 * @param eventClass the class of events the system reacts to in this step
		 * @param <T> the type of the class
		 * @return the created event part of this step
		 */
		public <T> EventPart<T> user(Class<T> eventClass) {
			EventPart<T> newEventPart = new EventPart<>(eventClass);
			UseCaseStep.this.eventPart = newEventPart;
			return newEventPart;
		}

		/**
		 * Without triggering a system reaction, raise the specified event.
		 * You may call this method several times during model creation, to raise
		 * several events.
		 * 
		 * Internally calls {@link UseCaseRunner#reactTo(Object)} for the specified event.
		 * 
		 * @param <U> the type of event to be raised
		 * @param eventSupplier the supplier proving the event
		 * @return the system part
		 */
		public <U> SystemPart<SystemEvent> raise(Supplier<U> eventSupplier) {
			Objects.requireNonNull(eventSupplier);
			
			SystemPart<SystemEvent> systemPartWithRaisedEvent 
				= system(() -> {}).raise(eventSupplier);
			return systemPartWithRaisedEvent;
		}
		
		/**
		 * Makes the use case runner continue after the specified step.
		 * 
		 * @param stepName name of the step to continue after, in this use case.
		 * @return the use case this step belongs to, to ease creation of further flows
		 * @throws NoSuchElementInUseCase if no step with the specified stepName is found in the current use case
		 */
		public UseCase continueAfter(String stepName) {
			Objects.requireNonNull(stepName);
			
			system(SystemReaction.continueAfterStepAndCurrentFlowCantBeReentered(useCase(), stepName));
			return useCase();
		}
		
		/**
		 * Defines an "autonomous system reaction",
		 * meaning the system will react when the step's predicate is true, without
		 * needing an event provided to the use case runner.
		 * 
		 * Instead of the model creator defining the event (via
		 * {@link #user(Class)}), the step implicitly handles the default
		 * system event. Default system events are raised by the 
		 * use case runner itself, causing "autonomous system reactions".
		 * 
		 * @param systemReaction the autonomous system reaction
		 * @return the created system part of this step
		 */
		public SystemPart<SystemEvent> system(Runnable systemReaction) {
			SystemPart<SystemEvent> systemPart = 
				user(SystemEvent.class).system(systemEvent -> systemReaction.run());
			return systemPart;
		}
		
		/**
		 * Returns the actors that determine which user groups can 
		 * cause the system to react to the event of this step.  
		 * 
		 * @return the actors
		 */
		public Actor[] actors() {
			return actors;
		}
	}
	
	/**
	 * The part of the step that contains a reference to the event
	 * that is allowed to trigger a system reaction for this step.
	 * 
	 * @author b_muth
	 *
	 */
	public class EventPart<T>{
		private Class<T> eventClass;
		
		private EventPart(Class<T> eventClass) {
			this.eventClass = eventClass;
		}
		
		/**
		 * Defines the system reaction,
		 * meaning the system will react as specified when the step's predicate
		 * is true and an appropriate event object is received
		 * via {@link UseCaseRunner#reactTo(Object)}.
		 * 
		 * @param systemReaction the specified system reaction
		 * @return the created system part of this step
		 */
		public SystemPart<T> system(Consumer<T> systemReaction) {
			Objects.requireNonNull(systemReaction);
			
			SystemPart<T> newSystemPart = new SystemPart<>(systemReaction);
			UseCaseStep.this.systemPart = newSystemPart;
			return newSystemPart;		
		}
		
		/**
		 * Makes the use case runner continue after the specified step.
		 * Note that the current flow is NOT reentered immediately, even if it's condition is true.
		 * 
		 * @param stepName name of the step to continue after, in this use case.
		 * @return the use case this step belongs to, to ease creation of further flows
		 * @throws NoSuchElementInUseCase if no step with the specified stepName is found in the current use case
		 */
		public UseCase continueAfter(String stepName) {
			Objects.requireNonNull(stepName);
			
			system(sr -> continueAfterStepAndCurrentFlowCantBeReentered(useCase(), stepName).run());
			return useCase();
		}
		
		/**
		 * Makes the use case runner start from the beginning, when no
		 * flow and step has been run.
		 * 
		 * @see UseCaseRunner#restart()
		 * @return the use case this step belongs to, to ease creation of further flows
		 */
		public UseCase restart() {
			system(sr -> useCaseModel().useCaseRunner().restart());
			return useCase();
		}
		
		/**
		 * Returns the class of event or exception objects that the system reacts to in this step.
		 * The system reacts to objects that are instances of the returned class or 
		 * instances of any direct or indirect subclass of the returned class.
		 * 
		 * @return the class of events the system reacts to in this step
		 */
		public Class<T> eventClass() {
			return eventClass;
		}
	}
	
	/**
	 * The part of the step that contains a reference to the system reaction
	 * that can be triggered, given an approriate actor and event, and the 
	 * step's predicate being true.
	 * 
	 * @author b_muth
	 *
	 */
	public class SystemPart<T>{
		private Consumer<T> systemReaction;

		private SystemPart(Consumer<T> systemReaction) {
			this.systemReaction = systemReaction;
		}
		
		/**
		 * Returns the system reaction,
		 * meaning how the system will react when the step's predicate
		 * is true and an appropriate event object is received
		 * via {@link UseCaseRunner#reactTo(Object)}.
		 * 
		 * @return the system reaction
		 */
		public Consumer<T> systemReaction() {
			return systemReaction;
		}
		
		/**
		 * Creates a new use case in this model.
		 * 
		 * @param useCaseName the name of the use case to be created.
		 * @return the newly created use case
		 * @throws ElementAlreadyInModel if a use case with the specified name already exists in the model
		 */
		public UseCase useCase(String useCaseName) {
			Objects.requireNonNull(useCaseName);

			UseCase newUseCase = useCaseModel().useCase(useCaseName);
			return newUseCase;
		}
		
		/**
		 * Creates a new flow in the use case that contains this step.
		 * 
		 * @param flowName the name of the flow to be created.
		 * @return the newly created flow
		 * @throws ElementAlreadyInModel if a flow with the specified name already exists in the use case
		 */
		public UseCaseFlow flow(String flowName) {
			Objects.requireNonNull(flowName);

			UseCaseFlow newFlow = 
				UseCaseStep.this.useCase().flow(flowName);
			return newFlow;
		}

		/**
		 * Creates a new step in this flow, with the specified name, that follows this step in sequence.
		 * 
		 * @param stepName the name of the step to be created
		 * @return the newly created step, to ease creation of further steps
		 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
		 */
		public UseCaseStep step(String stepName) {			
			Objects.requireNonNull(stepName);

			UseCaseStep newStep = 
				UseCaseStep.this.useCase().newStep(stepName, UseCaseStep.this.flow(), 
					Optional.of(UseCaseStep.this), Optional.empty());
			
			return newStep; 
		}
		
		/**
		 * After triggering the system reaction, raise the specified event.
		 * You may call this method several times during model creation, to raise
		 * several events.
		 * 
		 * Internally calls {@link UseCaseRunner#reactTo(Object)} for the specified event.
		 * 
		 * @param <U> the type of event to be raised
		 * @param eventSupplier the supplier proving the event
		 * @return the system part
		 */
		public <U> SystemPart<T> raise(Supplier<U> eventSupplier) {
			systemReaction = systemReaction.andThen(x -> useCaseModel().useCaseRunner().reactTo(eventSupplier.get()));
			return this;
		}
		
		/**
		 * Repeat this step while the condition is fulfilled.
		 * 
		 * Note that the specified condition is evaluated the first time after the
		 * step, so the step is "run" at least one time before checking the repeat condition.
		 * 
		 * @param condition the condition to check
		 * @return the system part
		 */
		public SystemPart<T> repeatWhile(Predicate<UseCaseRunner> condition) {
			Objects.requireNonNull(condition);
			
			String thisStepName = name();
			
			UseCaseStep conditionalClonedStep = 
				createConditionalStepThatBehavesLike(thisStepName, condition);

			createStepThatStartsNextLoopIteration(thisStepName, conditionalClonedStep);
			
			return this;
		}

		private void createStepThatStartsNextLoopIteration(String thisStepName, UseCaseStep newRepeatStep) {
			String nextLoopIterationStepName = uniqueNextLoopIterationStepName();
			newRepeatStep.systemPart()
				.step(nextLoopIterationStepName)
					.system(continueAfterStepAndCurrentFlowCanBeReentered(UseCaseStep.this.useCase(), thisStepName));
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private UseCaseStep createConditionalStepThatBehavesLike(String thisStepName, Predicate<UseCaseRunner> condition) {
			String repeatStepName = uniqueRepeatStepName();

			UseCaseStep newRepeatStep = flow(repeatStepName).after(thisStepName).when(condition).step(repeatStepName);
			newRepeatStep.as(actorPart().actors()).user(eventPart().eventClass()).system((Consumer)systemPart().systemReaction());
			
			return newRepeatStep;
		}
	}
	
	/**
	 * Returns a unique name for a "repeat" step, to avoid name
	 * collisions if multiple "repeat" steps exist in the model.
	 * 
	 * Overwrite this only if you are not happy with the "automatically created"
	 * step names in the model.
	 * 
	 * @return a unique step name
	 */
	protected String uniqueRepeatStepName() {
		return name() + REPEAT_STEP_POSTFIX;
	}
	
	/**
	 * Returns a unique name for a "next loop iteration" step, to avoid name
	 * collisions if multiple "next loop iteration" steps exist in the model.
	 * 
	 * Overwrite this only if you are not happy with the "automatically created"
	 * step names in the model.
	 * 
	 * @return a unique step name
	 */
	protected String uniqueNextLoopIterationStepName() {
		return name() + NEXT_LOOP_ITERATION_STEP_POSTFIX;
	}
}
