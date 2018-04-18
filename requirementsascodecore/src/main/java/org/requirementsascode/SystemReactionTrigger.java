package org.requirementsascode;

import java.io.Serializable;
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
public class SystemReactionTrigger implements Serializable {
    private static final long serialVersionUID = -8615677956101523359L;

    private Object event;
    private Step useCaseStep;

    SystemReactionTrigger() {
    }

    /**
     * The system reaction of the step accepts the event (both event and step passed
     * in earlier).
     *
     * @see #setupWith(Object, FlowStep)
     */
    @SuppressWarnings("unchecked")
    public void trigger() {
	((Consumer<Object>) useCaseStep.getSystemReaction()).accept(event);
    }

    void setupWith(Object event, Step useCaseStep) {
	this.event = event;
	this.useCaseStep = useCaseStep;
    }

    /**
     * Returns the event object that will be passed to the system reaction when
     * {@link #trigger()} is called.
     *
     * @return the event object.
     */
    public Object getEvent() {
	return event;
    }

    /**
     * Returns the use case step whose system reaction is performed when
     * {@link #trigger()} is called.
     *
     * @return the use case step.
     */
    public Step getUseCaseStep() {
	return useCaseStep;
    }
}
