package org.requirementsascode;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A use case step, as part of a use case. The use case steps define the
 * behavior of the use case.
 * 
 * A use case step is the core class of requirementsascode, providing all the
 * necessary configuration information to the {@link UseCaseModelRunner} to cause the
 * system to react to events. A use case step has a predicate, which defines the
 * complete condition that needs to be fulfilled to cause the system reaction, given a
 * matching event occurs.
 * 
 * @author b_muth
 *
 */
public class Step extends UseCaseModelElement { 
	private Flow useCaseFlow;
	private Optional<Step> previousStepInFlow;
	private Predicate<UseCaseModelRunner> predicate;

	private Actor[] actors;
	private Class<?> eventClass;
	private Consumer<?> systemReaction;

	/**
	 * Creates a use case step with the specified name that belongs to the
	 * specified use case flow.
	 * 
	 * @param stepName
	 *            the name of the step to be created
	 * @param useCaseFlow
	 *            the use case flow that will contain the new use case
	 * @param previousStepInFlow
	 *            the step created before the step in its flow, or else an empty
	 *            optional if it is the first step in its flow
	 */
	Step(String stepName, Flow useCaseFlow, Optional<Step> previousStepInFlow) {
		super(stepName, useCaseFlow.useCaseModel());
		Objects.requireNonNull(previousStepInFlow);
		this.useCaseFlow = useCaseFlow;
		this.previousStepInFlow = previousStepInFlow;
	}
	
	public Optional<Step> previousStepInFlow() {
		return previousStepInFlow;
	}

	public Flow flow() {
		return useCaseFlow;
	}

	public UseCase useCase() {
		return flow().getUseCase();
	}
	
	public Predicate<UseCaseModelRunner> predicate() {
		return predicate;
	}

	void setPredicate(Predicate<UseCaseModelRunner> predicate) {
		this.predicate = predicate;
	}

	public Actor[] getActors() {
		return actors;
	}
	
	void setActors(Actor[] actors) {
		this.actors = actors;
	}
	
	public Class<?> getEventClass() {
		return eventClass;
	}

	void setEventClass(Class<?> eventClass) {
		this.eventClass = eventClass;
	}
	public Consumer<?> getSystemReaction() {
		return systemReaction;
	}

	void setSystemReaction(Consumer<?> systemReaction) {
		this.systemReaction = systemReaction;
	}
}
