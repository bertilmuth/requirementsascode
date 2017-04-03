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
 * system to react to events.
 * 
 * @author b_muth
 *
 */
public class Step extends UseCaseModelElement { 
	private Flow flow;
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
		super(stepName, useCaseFlow.getUseCaseModel());
		Objects.requireNonNull(previousStepInFlow);
		this.flow = useCaseFlow;
		this.previousStepInFlow = previousStepInFlow;
	}
	
	public Optional<Step> getPreviousStepInFlow() {
		return previousStepInFlow;
	}

	public Flow getFlow() {
		return flow;
	}

	public UseCase getUseCase() {
		return getFlow().getUseCase();
	}
	
	public Predicate<UseCaseModelRunner> getPredicate() {
		return predicate;
	}

	void setPredicate(Predicate<UseCaseModelRunner> predicate) {
		this.predicate = predicate;
	}

	public Actor[] getAs() {
		return actors;
	}
	
	void setAs(Actor[] actors) {
		this.actors = actors;
	}
	
	public Class<?> getUser() {
		return eventClass;
	}

	void setUser(Class<?> eventClass) {
		this.eventClass = eventClass;
	}
	public Consumer<?> getSystem() {
		return systemReaction;
	}

	void setSystem(Consumer<?> systemReaction) {
		this.systemReaction = systemReaction;
	}
}
