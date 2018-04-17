package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.predicate.After;

/**
 * A use case step, as part of a use case. The use case steps define the
 * behavior of the use case.
 *
 * <p>
 * A use case step is the core class of requirementsascode, providing all the
 * necessary configuration information to the {@link UseCaseModelRunner} to
 * cause the system to react to events.
 *
 * @author b_muth
 */
public abstract class Step extends UseCaseModelElement implements Serializable {
    private static final long serialVersionUID = -2926490717985964131L;

    private UseCase useCase;
    private Flow flow;
    private Predicate<UseCaseModelRunner> reactWhile;

    private Actor[] actors;
    private Class<?> userEventClass;
    private Consumer<?> systemReaction;
    private Predicate<UseCaseModelRunner> flowPosition;
    private Predicate<UseCaseModelRunner> when;

    private Step previousStepInFlow;

    /**
     * Creates a use case step with the specified name that belongs to the specified
     * use case flow.
     *
     * @param stepName
     *            the name of the step to be created
     * @param useCaseFlow
     *            the use case flow that will contain the new use case
     */
    Step(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase.getUseCaseModel());
	this.useCase = useCase;
	this.flow = useCaseFlow;
    }

    public abstract Predicate<UseCaseModelRunner> getPredicate();

    public Flow getFlow() {
	return flow;
    }

    public UseCase getUseCase() {
	return useCase;
    }

    public Optional<Step> getPreviousStepInFlow() {
	return Optional.ofNullable(previousStepInFlow);
    }

    protected void setPreviousStepInFlow(Step previousStepInFlow) {
	this.previousStepInFlow = previousStepInFlow;
	setFlowPosition(new After(previousStepInFlow));
    }

    protected void setReactWhile(Predicate<UseCaseModelRunner> reactWhile) {
	this.reactWhile = reactWhile;
    }

    public Predicate<UseCaseModelRunner> getReactWhile() {
	return reactWhile;
    }

    void setFlowPosition(Predicate<UseCaseModelRunner> flowPosition) {
	this.flowPosition = flowPosition;
    }

    public Optional<Predicate<UseCaseModelRunner>> getFlowPosition() {
	return Optional.ofNullable(flowPosition);
    }

    void setWhen(Predicate<UseCaseModelRunner> when) {
	this.when = when;
    }

    public Optional<Predicate<UseCaseModelRunner>> getWhen() {
	return Optional.ofNullable(when);
    }

    public Actor[] getActors() {
	return actors;
    }

    void setActors(Actor[] actors) {
	this.actors = actors;
    }

    public Class<?> getUserEventClass() {
	return userEventClass;
    }

    void setUserEventClass(Class<?> userEventClass) {
	this.userEventClass = userEventClass;
    }

    public Consumer<?> getSystemReaction() {
	return systemReaction;
    }

    void setSystemReaction(Consumer<?> systemReaction) {
	this.systemReaction = systemReaction;
    }

    public void includeUseCase(UseCase includedUseCase) {
	for (Flow includedFlow : includedUseCase.getFlows()) {
	    includeFlow(includedFlow);
	}
    }

    private void includeFlow(Flow includedFlow) {
	Optional<Step> optionalFirstStepOfFlow = includedFlow.getFirstStep();
	optionalFirstStepOfFlow.ifPresent(firstStepOfFlow -> {
	    Predicate<UseCaseModelRunner> oldFlowPosition = firstStepOfFlow.getFlowPosition().orElse(r -> false);
	    Predicate<UseCaseModelRunner> includeFlowPosition = new After(this).or(oldFlowPosition);
	    firstStepOfFlow.setFlowPosition(includeFlowPosition);
	});
    }
}
