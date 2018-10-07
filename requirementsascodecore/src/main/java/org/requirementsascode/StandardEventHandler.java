package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Class used to trigger a system reaction.
 *
 * <p>
 * Use an instance of this class only if you want to adapt the system reaction,
 * in order to call the standard system reaction from your adapted system
 * reaction.
 *
 * @author b_muth
 */
public class StandardEventHandler implements Serializable {
    private static final long serialVersionUID = -8615677956101523359L;

    private Object event;
    private Step step;

    StandardEventHandler() {
    }

    /**
     * The system reaction of the step accepts the event (both event and step passed
     * in earlier).
     *
     * @see #setupWith(Object, Step)
     */
    @SuppressWarnings("unchecked")
    public void handleEvent() {
	((Consumer<Object>) step.getSystemReaction()).accept(event);
    }

    void setupWith(Object event, Step useCaseStep) {
	this.event = event;
	this.step = useCaseStep;
    }

    /**
     * Returns the name of the step whose system reaction is performed when
     * {@link #handleEvent()} is called.
     *
     * @return the step name.
     */
    public String getStepName() {
	return step.getName();
    }

    /**
     * Returns the precondition that needs to be true to cause the 
     * system reaction when {@link #handleEvent()} is called.
     *
     * @return the condition, or an empty optional when no condition was specified.
     */
    public Optional<? extends Object> getCondition() {
	Optional<? extends Object> optionalCondition = step.getWhen();
	return optionalCondition;
    }

    /**
     * Returns the event object that will be passed to the system reaction when
     * {@link #handleEvent()} is called.
     *
     * @return the event object, or an empty optional when no event was specified.
     */
    public Optional<? extends Object> getEvent() {
	Optional<? extends Object> optionalEvent = null;
	if (event instanceof ModelRunner) {
	    optionalEvent = Optional.empty();
	} else {
	    optionalEvent = Optional.ofNullable(event);
	}
	return optionalEvent;
    }

    /**
    * Returns the system reaction to be executed when
    * {@link #handleEvent()} is called.
    *
    * @return the event object.
    */
    public Object getSystemReaction() {
	Object systemReaction = step.getSystemReaction();
	if(systemReaction instanceof AutonomousSystemReaction) {
	    AutonomousSystemReaction autonomousSystemReaction = (AutonomousSystemReaction)systemReaction;
	    systemReaction = autonomousSystemReaction.getSystemReaction();
	}
	return systemReaction;
    }
}
