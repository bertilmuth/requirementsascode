package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A use case step is a part of a use case. The use case steps define the
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
    private Predicate<UseCaseModelRunner> reactWhile;

    private Actor[] actors;
    private Class<?> eventClass;
    private Consumer<?> systemReaction;
    private Predicate<UseCaseModelRunner> when;

    /**
     * Creates a use case step with the specified name that belongs to the specified
     * use case.
     *
     * @param useCase the use case this step belongs to
     * @param stepName
     *            the name of the step to be created
     */
    Step(String stepName, UseCase useCase) {
	super(stepName, useCase.getUseCaseModel());
	this.useCase = useCase;
    }

    public abstract Predicate<UseCaseModelRunner> getCondition();

    public UseCase getUseCase() {
	return useCase;
    }

    protected void setReactWhile(Predicate<UseCaseModelRunner> reactWhile) {
	this.reactWhile = reactWhile;
    }

    public Predicate<UseCaseModelRunner> getReactWhile() {
	return reactWhile;
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
