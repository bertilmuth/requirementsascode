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


public class UseCaseRunner {
	private List<Actor> actorsToRunAs;
	private UseCaseModel useCaseModel;
	private Optional<UseCaseStep> optionalLatestStep;
	private UseCaseFlow latestFlow;
	private boolean isRunning;

	public UseCaseRunner() {
		this.isRunning = false;
		this.useCaseModel = new UseCaseModel(this);
		this.actorsToRunAs = Arrays.asList(useCaseModel.getAutonomousSystemActor());
		this.optionalLatestStep = Optional.empty();
	}
	
	public UseCaseModel getUseCaseModel() {
		return useCaseModel;
	}
	
	public UseCaseRunner run() {
		isRunning = true;
		triggerAutonomousSystemReaction();
		return this;
	}
	
	private void triggerAutonomousSystemReaction() {
		reactTo(new AutonomousSystemReactionEvent());
	}

	public UseCaseRunner as(Actor actor) {
		Objects.requireNonNull(actor);
		
		actorsToRunAs = Arrays.asList(actor, useCaseModel.getAutonomousSystemActor());
		return this;
	}
	
	public void reactTo(Object... events) {
		Objects.requireNonNull(events);
		
		for (Object event : events) {
			reactTo(event);
		}		
	}

	public <T> UseCaseStep reactTo(T event) {
		Objects.requireNonNull(event);
		
		UseCaseStep latestStepRun = null;
		if(isRunning){
			Class<? extends Object> currentEventClass = event.getClass();
			Set<UseCaseStep> reactingUseCaseSteps = getStepsEnabledFor(currentEventClass);
			latestStepRun = triggerSystemReaction(event, reactingUseCaseSteps);
		}
		return latestStepRun;
	}

	public Set<UseCaseStep> getStepsEnabledFor(Class<? extends Object> eventClass) {
		Objects.requireNonNull(eventClass);
		
		Stream<UseCaseStep> stepStream = useCaseModel.getUseCaseSteps().stream();
		Set<UseCaseStep> enabledSteps = getEnabledStepSubset(eventClass, stepStream);
		return enabledSteps;
	}
	
	Set<UseCaseStep> getEnabledStepSubset(Class<? extends Object> eventClass, Stream<UseCaseStep> stepStream) {
		Set<UseCaseStep> enabledSteps = stepStream
			.filter(step -> stepActorIsRunActor(step))
			.filter(step -> stepEventClassIsSameOrSuperclassAsEventClass(step, eventClass))
			.filter(step -> isConditionFulfilled(step))
			.collect(Collectors.toSet());
		return enabledSteps;
	}

	protected <T> UseCaseStep triggerSystemReaction(T event, Collection<UseCaseStep> useCaseSteps) {
		UseCaseStep useCaseStep = null;

		if(useCaseSteps.size() == 1){
			useCaseStep = useCaseSteps.iterator().next();
			triggerSystemReactionAndHandleException(event, useCaseStep);
		} else if(useCaseSteps.size() > 1){
			String message = getMoreThanOneStepCouldReactExceptionMessage(useCaseSteps);
			throw new MoreThanOneStepCouldReactException(message);
		}
		
		return useCaseStep;
	}
	protected <T> String getMoreThanOneStepCouldReactExceptionMessage(Collection<UseCaseStep> useCaseSteps) {
		String message = "System could react to more than one step: ";
		String useCaseStepsClassNames = useCaseSteps.stream().map(useCaseStep -> useCaseStep.toString())
				.collect(Collectors.joining(",", message, ""));
		return useCaseStepsClassNames;
	}
	
	protected <T> UseCaseStep triggerSystemReactionAndHandleException(T event, UseCaseStep useCaseStep) {
		if(useCaseStep.getSystemPart() == null){
			String message = getMissingSystemPartExceptionMessage(useCaseStep);
			throw new MissingUseCaseStepPartException(message);
		}
		
		setLatestStep(useCaseStep);
		setLatestFlow(useCaseStep.getUseCaseFlow());
		
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
	protected String getMissingSystemPartExceptionMessage(UseCaseStep useCaseStep) {
		String message = "Use Case Step \"" + useCaseStep + "\" has no defined system part! Please have a look and update your Use Case Model for this step!";
		return message;
	}
	
	protected <T> void triggerSystemReaction(T event, Consumer<T> systemReaction) {
		systemReaction.accept(event);
	}

	protected void handleException(Exception e) {
		reactTo(e);
	}

	private boolean stepActorIsRunActor(UseCaseStep useCaseStep) {
		ActorPart<?> actorPart = useCaseStep.getActorPart();
		if(actorPart == null){
			String message = getMissingActorPartExceptionMessage(useCaseStep);
			throw(new MissingUseCaseStepPartException(message));
		}
		
		return actorsToRunAs.contains(actorPart.getActor());
	}
	
	protected String getMissingActorPartExceptionMessage(UseCaseStep useCaseStep) {
		String message = "Use Case Step \"" + useCaseStep + "\" has no defined actor part! Please have a look and update your Use Case Model for this step!";
		return message;
	}
	
	private boolean stepEventClassIsSameOrSuperclassAsEventClass(UseCaseStep useCaseStep, Class<?> currentEventClass) {
		Class<?> stepEventClass = useCaseStep.getActorPart().getEventClass();
		return stepEventClass.isAssignableFrom(currentEventClass);
	}
	
	private boolean isConditionFulfilled(UseCaseStep useCaseStep) {
		Predicate<UseCaseRunner> predicate = useCaseStep.getPredicate();
		boolean result = predicate.test(this);
		return result;
	}
	
	public Optional<UseCaseStep> getLatestStep() {
		return optionalLatestStep;
	}
	
	public void setLatestStep(UseCaseStep latestStep) {
		this.optionalLatestStep = Optional.of(latestStep);
	}
	
	public UseCaseFlow getLatestFlow() {
		return latestFlow;
	}
	
	public void setLatestFlow(UseCaseFlow latestFlow) {
		this.latestFlow = latestFlow;
	}
}
