package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Use an instance of this class if you want to find out the details about the step
 * to be run in a custom event handler, and to trigger the system reaction.
 *
 * @see ModelRunner#handleWith(Consumer)
 * @author b_muth
 */
public class StepToBeRun implements Serializable {
    private static final long serialVersionUID = -8615677956101523359L;

    private Object event;
    private Step step;

    StepToBeRun() {
    }

    /**
     * Triggers the system reaction of this step.
     */
    @SuppressWarnings("unchecked")
    public void run() {
	((Function<Object, Object[]>) step.getSystemReaction()).apply(event);
    }

    void setupWith(Object event, Step useCaseStep) {
	this.event = event;
	this.step = useCaseStep;
    }

    /**
     * Returns the name of the step whose system reaction is performed when
     * {@link #run()} is called.
     *
     * @return the step name.
     */
    public String getStepName() {
	return step.getName();
    }

    /**
     * Returns the precondition that needs to be true to trigger the system reaction
     * when {@link #run()} is called.
     *
     * @return the condition, or an empty optional when no condition was specified.
     */
    public Optional<? extends Object> getCondition() {
	Optional<? extends Object> optionalCondition = step.getCondition();
	return optionalCondition;
    }

    /**
     * Returns the event object that will be passed to the system reaction when
     * {@link #run()} is called.
     *
     * @return the event object, or an empty optional when no event was specified.
     */
    public Optional<? extends Object> getEvent() {
	Optional<? extends Object> optionalEvent = null;
	if (event instanceof ModelRunner) {
	    optionalEvent = Optional.empty();
	} else {
	    optionalEvent = Optional.of(event);
	}
	return optionalEvent;
    }

    /**
     * Returns the system reaction to be executed when {@link #run()} is
     * called.
     *
     * @return the system reaction.
     */
    public Object getSystemReaction() {
	Object systemReactionObject = ((SystemReaction)step.getSystemReaction()).getSystemReactionObject();
	if (systemReactionObject instanceof AutonomousSystemReaction) {
	    AutonomousSystemReaction autonomousSystemReaction = (AutonomousSystemReaction) systemReactionObject;
	    systemReactionObject = autonomousSystemReaction.getSystemReaction();
	} 
	return systemReactionObject;
    }
}
