package org.requirementsascode;

import static org.requirementsascode.UseCaseStepPredicate.afterStep;
import static org.requirementsascode.UseCaseStepPredicate.atFirstStep;
import static org.requirementsascode.UseCaseStepPredicate.noOtherStepIsEnabledThan;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class UseCaseStep extends UseCaseModelElement{
	private UseCaseFlow useCaseFlow;
	private Optional<UseCaseStep> previousStep;
	private Optional<Predicate<UseCaseRunner>> predicate;
	
	private ActorPart actorPart;
	private EventPart<?> eventPart;
	private SystemPart<?> systemPart;
		
	UseCaseStep(String stepName, UseCaseFlow useCaseFlow, Optional<UseCaseStep> previousStep, Optional<Predicate<UseCaseRunner>> predicate) {
		super(stepName, useCaseFlow.getUseCaseModel());
		Objects.requireNonNull(previousStep);
		Objects.requireNonNull(predicate);
		
		this.useCaseFlow = useCaseFlow;
		this.previousStep = previousStep;
		this.predicate = predicate;		
	}
	
	public UseCaseStep.ActorPart actor(Actor actor) {
		Objects.requireNonNull(actor);
		
		actorPart = new ActorPart(actor);
		return actorPart;
	}
	
	public UseCaseStep.SystemPart<?> system(Runnable systemReaction) {
		Objects.requireNonNull(systemReaction);
		
		Actor systemActor = getUseCaseModel().getSystemActor();
		
		UseCaseStep.SystemPart<?> systemPart =
			actor(systemActor).handle(SystemEvent.class).
				system(systemEvent -> systemReaction.run());
		
		return systemPart;
	}
	
	public <T> EventPart<T> handle(Class<T> eventOrExceptionClass) {
		Objects.requireNonNull(eventOrExceptionClass);

		Actor userActor = getUseCaseModel().getUserActor();
		EventPart<T> newEventPart = actor(userActor).handle(eventOrExceptionClass);
		
		return newEventPart;
	}
	
	public UseCaseFlow getUseCaseFlow() {
		return useCaseFlow;
	}
	
	public UseCase getUseCase() {
		return getUseCaseFlow().getUseCase();
	}
	
	public Optional<UseCaseStep> getPreviousStep() {
		return previousStep;
	}
	
	public ActorPart getActorPart() {
		return actorPart;
	}
	
	public EventPart<?> getEventPart() {
		return eventPart;
	}
	
	public SystemPart<?> getSystemPart() {
		return systemPart;
	}
	
	public Predicate<UseCaseRunner> getPredicate() {
		return predicate.orElse(afterPreviousStepWhenNoOtherStepIsEnabled());
	} 
	
	private Predicate<UseCaseRunner> afterPreviousStepWhenNoOtherStepIsEnabled() {
		Predicate<UseCaseRunner> afterPreviousStepPredicate = 
			previousStep.map(s -> afterStep(s)).orElse(atFirstStep());
		return afterPreviousStepPredicate.and(noOtherStepIsEnabledThan(this));
	}
	
	public class ActorPart{
		private Actor namedActor;
		
		private ActorPart(Actor actor) {
			this.namedActor = actor;
			connectActorToThisStep(namedActor);		
		}

		private void connectActorToThisStep(Actor actor) {
			actor.newStep(getUseCase(), UseCaseStep.this);
		}
		
		public <T> EventPart<T> handle(Class<T> eventOrExceptionClass) {
			EventPart<T> newEventPart = new EventPart<>(eventOrExceptionClass);
			UseCaseStep.this.eventPart = newEventPart;
			return newEventPart;
		}
		
		public Actor getActor() {
			return namedActor;
		}
	}
	
	public class EventPart<T>{
		private Class<T> eventClass;
		
		private EventPart(Class<T> eventClass) {
			this.eventClass = eventClass;
		}
		
		public SystemPart<T> system(Consumer<T> systemReaction) {
			Objects.requireNonNull(systemReaction);
			
			SystemPart<T> newSystemPart = new SystemPart<>(systemReaction);
			UseCaseStep.this.systemPart = newSystemPart;
			return newSystemPart;		
		}
		
		public Class<T> getEventClass() {
			return eventClass;
		}
	}
	
	public class SystemPart<T>{
		private Consumer<T> systemReaction;

		private SystemPart(Consumer<T> systemReaction) {
			this.systemReaction = systemReaction;
		}
		
		public Consumer<T> getSystemReaction() {
			return systemReaction;
		}
		
		public UseCase newUseCase(String useCaseName) {
			Objects.requireNonNull(useCaseName);

			UseCase newUseCase = getUseCaseModel().newUseCase(useCaseName);
			return newUseCase;
		}
		
		public UseCaseFlow newFlow(String flowName) {
			Objects.requireNonNull(flowName);

			UseCaseFlow newFlow = getUseCase().newFlow(flowName);
			return newFlow;
		}

		public UseCaseStep newStep(String stepName) {			
			Objects.requireNonNull(stepName);

			UseCaseStep newStep = 
				getUseCase().newStep(stepName, getUseCaseFlow(), Optional.of(UseCaseStep.this), Optional.empty());
			
			return newStep; 
		}
		
		public SystemPart<T> repeatWhile(Predicate<UseCaseRunner> condition) {
			Objects.requireNonNull(condition);
			
			String thisStepName = getName();
			String newRepeatStepName = uniqueRepeatStepName();
			
			UseCaseStep newRepeatStep = newFlow(newRepeatStepName)
				.after(thisStepName).when(condition).newStep(newRepeatStepName);
			
			makeRepeatStepBehaveLikeThisStep(newRepeatStep);
			
			getUseCaseFlow().continueAfter(thisStepName, Optional.of(newRepeatStep), Optional.empty());
			
			return this;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void makeRepeatStepBehaveLikeThisStep(UseCaseStep newRepeatStep) {
			newRepeatStep
				.actor(getActorPart().getActor()).handle(getEventPart().getEventClass())
				.system((Consumer)getSystemPart().getSystemReaction());
		}
		
		public UseCase continueAfter(String stepName) {
			Objects.requireNonNull(stepName);
			
			getUseCaseFlow().continueAfter(stepName, Optional.of(UseCaseStep.this), Optional.empty());
			return getUseCase();
		}

		public void restart() {
			newStep(uniqueRestartStepName()).system(
				() -> getUseCaseModel().getUseCaseRunner().restart());
		}
	}
	
	private String uniqueRepeatStepName() {
		return uniqueStepName(getName(), "REPEAT");
	}
	
	private String uniqueRestartStepName() {
		return uniqueStepName("RESTART");
	}
}
