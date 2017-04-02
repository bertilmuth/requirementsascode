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
 * necessary configuration information to the {@link UseCaseRunner} to cause the
 * system to react to events. A use case step has a predicate, which defines the
 * complete condition that needs to be fulfilled to cause the system reaction, given a
 * matching event occurs.
 * 
 * @author b_muth
 *
 */
public class UseCaseStep extends UseCaseModelElement { 
	private UseCaseFlow useCaseFlow;
	private Optional<UseCaseStep> previousStepInFlow;
	private Predicate<UseCaseRunner> predicate;

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
	UseCaseStep(String stepName, UseCaseFlow useCaseFlow, Optional<UseCaseStep> previousStepInFlow) {
		super(stepName, useCaseFlow.useCaseModel());
		Objects.requireNonNull(previousStepInFlow);
		this.useCaseFlow = useCaseFlow;
		this.previousStepInFlow = previousStepInFlow;
	}
	
	public Optional<UseCaseStep> previousStepInFlow() {
		return previousStepInFlow;
	}

	public UseCaseFlow flow() {
		return useCaseFlow;
	}

	public UseCase useCase() {
		return flow().useCase();
	}
	
	public Predicate<UseCaseRunner> predicate() {
		return predicate;
	}

	public void setPredicate(Predicate<UseCaseRunner> predicate) {
		this.predicate = predicate;
	}

	public Actor[] getActors() {
		return actors;
	}
	
	public void setActors(Actor[] actors) {
		this.actors = actors;
	}
	
	public Class<?> getEventClass() {
		return eventClass;
	}

	public void setEventClass(Class<?> eventClass) {
		this.eventClass = eventClass;
	}
	public Consumer<?> getSystemReaction() {
		return systemReaction;
	}

	public void setSystemReaction(Consumer<?> systemReaction) {
		this.systemReaction = systemReaction;
	}
}
