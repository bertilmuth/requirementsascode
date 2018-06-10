package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.condition.Condition;
import org.requirementsascode.condition.ReactWhile;

/**
 * A step is a part of a use case. The steps define the
 * behavior of the use case.
 *
 * <p>
 * A step is the core class of requirementsascode, providing all the
 * necessary configuration information to the {@link ModelRunner} to
 * cause the system to react to events.
 *
 * @author b_muth
 */
public abstract class Step extends ModelElement implements Serializable {
    private static final long serialVersionUID = -2926490717985964131L;

    private UseCase useCase;
    private ReactWhile reactWhile;

    private Actor[] actors;
    private Class<?> eventClass;
    private Consumer<?> systemReaction;
    private Condition when;

    /**
     * Creates a step with the specified name that belongs to the specified
     * use case.
     *
     * @param useCase the use case this step belongs to
     * @param stepName
     *            the name of the step to be created
     */
    Step(String stepName, UseCase useCase) {
	super(stepName, useCase.getModel());
	this.useCase = useCase;
    }

    public abstract Predicate<ModelRunner> getPredicate();

    public UseCase getUseCase() {
	return useCase;
    }

    void setReactWhile(ReactWhile reactWhile) {
	this.reactWhile = reactWhile;
    }

    public ReactWhile getReactWhile() {
	return reactWhile;
    }

    void setWhen(Condition whenCondition) {
	this.when = whenCondition;
    }

    public Optional<Condition> getWhen() {
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
    
    protected static Predicate<ModelRunner> toPredicate(Condition condition){
	return modelRunner -> condition.evaluate();
    }
}
