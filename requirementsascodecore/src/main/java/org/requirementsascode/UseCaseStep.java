package org.requirementsascode;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.requirementsascode.event.AutonomousSystemReactionEvent;
import static org.requirementsascode.UseCaseStepCondition.*;

public class UseCaseStep extends UseCaseModelElement{
	private UseCaseFlow useCaseFlow;
	private UseCaseStep previousStep;
	private Predicate<UseCaseRunner> predicate;
	
	private ActorPart<?> actorPart;
	private SystemPart<?> systemPart;
		
	UseCaseStep(String stepName, UseCaseFlow useCaseFlow, UseCaseStep previousStep, Predicate<UseCaseRunner> predicate) {
		super(stepName, useCaseFlow.getModel());
		Objects.requireNonNull(useCaseFlow);
		
		this.useCaseFlow = useCaseFlow;
		this.previousStep = previousStep;
		this.predicate = predicate;		
	}
	
	public <U> UseCaseStep.ActorPart<U> actor(Actor actor, Class<U> eventClass) {
		Objects.requireNonNull(actor);
		Objects.requireNonNull(eventClass);
		
		ActorPart<U> newActorPart = new ActorPart<>(actor, eventClass);
		this.actorPart = newActorPart;
		return newActorPart;
	}
	
	public UseCaseStep.SystemPart<?> system(Runnable autonomousSystemReaction) {
		Objects.requireNonNull(autonomousSystemReaction);
		
		Actor autoReactionActor = getModel().getAutonomousSystemReactionActor();
		
		UseCaseStep.SystemPart<?> systemPart =
			actor(autoReactionActor, AutonomousSystemReactionEvent.class).
				system((o) -> autonomousSystemReaction.run());
		
		return systemPart;
	}
	
	public <T> ActorPart<T> handle(Class<T> exceptionOrEventClass) {
		Objects.requireNonNull(exceptionOrEventClass);

		Actor autoReactionActor = getModel().getAutonomousSystemReactionActor();
		ActorPart<T> newActorPart = actor(autoReactionActor, exceptionOrEventClass);
		this.actorPart = newActorPart;
		
		return newActorPart;
	}
	
	public UseCaseFlow getUseCaseFlow() {
		return useCaseFlow;
	}
	
	public UseCase getUseCase() {
		return getUseCaseFlow().getUseCase();
	}
	
	public UseCaseStep getPreviousStep() {
		return previousStep;
	}
	
	public ActorPart<?> getActorPart() {
		return actorPart;
	}
	
	public SystemPart<?> getSystemPart() {
		return systemPart;
	}
	
	public Predicate<UseCaseRunner> getPredicate() {
		if(predicate == null){
			predicate = afterPreviousStepWhenNoOtherStepIsEnabled();
		}
		return predicate;
	} 
	private Predicate<UseCaseRunner> afterPreviousStepWhenNoOtherStepIsEnabled() {
		return afterStep(previousStep).and(noOtherStepIsEnabledThan(this));
	}
	
	private Predicate<UseCaseRunner> noOtherStepIsEnabledThan(UseCaseStep theStep) {
		return run -> {
			Class<?> currentEventClass = theStep.getActorPart().getEventClass();
			
			UseCaseModel useCaseModel = theStep.getModel();
			
			Stream<UseCaseStep> otherStepsStream = 
				useCaseModel.getUseCaseSteps().stream()
					.filter(step -> !step.equals(theStep));
			
			Set<UseCaseStep> enabledOtherSteps = run.getEnabledStepSubset(currentEventClass, otherStepsStream);
			return enabledOtherSteps.size() == 0;
		};
	}
	
	public class ActorPart<T>{
		private Actor namedActor;
		private Class<T> eventClass;
		
		private ActorPart(Actor actor, Class<T> eventClass) {
			this.namedActor = actor;
			this.eventClass = eventClass;
			connectActorToThisStep(namedActor);		
		}

		private void connectActorToThisStep(Actor actor) {
			actor.newStep(getUseCase(), UseCaseStep.this);
		}
		
		public SystemPart<T> system(Consumer<T> systemReaction) {
			Objects.requireNonNull(systemReaction);
			
			SystemPart<T> newSystemPart = new SystemPart<>(systemReaction);
			UseCaseStep.this.systemPart = newSystemPart;
			return newSystemPart;		
		}
		
		public Actor getActor() {
			return namedActor;
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

			UseCase newUseCase = getModel().newUseCase(useCaseName);
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
				getUseCase().newStep(stepName, getUseCaseFlow(), UseCaseStep.this, null);
			
			return newStep;
		}
		
		public SystemPart<T> repeatWhile(Predicate<UseCaseRunner> condition) {
			Objects.requireNonNull(condition);
			
			String thisStepName = getName();
			String newRepeatStepName = uniqueRepeatStepName();
			
			UseCaseStep newRepeatStep = newFlow(newRepeatStepName)
				.after(thisStepName).when(condition).newStep(newRepeatStepName);
			
			makeRepeatStepBehaveLikeThisStep(newRepeatStep);
			
			getUseCaseFlow().continueAfter(thisStepName, newRepeatStep);
			
			return this;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void makeRepeatStepBehaveLikeThisStep(UseCaseStep newRepeatStep) {
			newRepeatStep
				.actor(getActorPart().getActor(), getActorPart().getEventClass())
				.system((Consumer)getSystemPart().getSystemReaction());
		}
		
		public UseCase continueAfter(String stepName) {
			Objects.requireNonNull(stepName);
			
			getUseCaseFlow().continueAfter(stepName, UseCaseStep.this);
			return getUseCase();
		}

		public void reset() {
			newStep(uniqueResetStepName()).system(
				() -> {
					getModel().getUseCaseRunner().setLatestFlow(null);
					getUseCaseFlow().jumpTo(null);
				});
		}
	}
	
	private String uniqueRepeatStepName() {
		return uniqueStepName(getName(), "REPEAT");
	}
	
	private String uniqueResetStepName() {
		return uniqueStepName("Use Case Run resets");
	}
}
