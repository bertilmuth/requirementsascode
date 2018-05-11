package org.requirementsascode;

import java.util.Objects;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.NoSuchElementInModel;
import org.requirementsascode.flowposition.After;
import org.requirementsascode.flowposition.Anytime;
import org.requirementsascode.flowposition.FlowPosition;
import org.requirementsascode.flowposition.InsteadOf;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see Flow
 * @author b_muth
 */
public class FlowPart {
    private Flow flow;
    private UseCase useCase;
    private UseCasePart useCasePart;
    private FlowPositionPart optionalFlowPositionPart;

    FlowPart(Flow flow, UseCasePart useCasePart) {
	this.flow = flow;
	this.useCasePart = useCasePart;
	this.useCase = useCasePart.useCase();
    }

    /**
     * Starts the flow after the specified step has been run, in this flow's use
     * case. 
     * 
     * Note: You should use after to handle exceptions that occurred in the specified
     * step.
     *
     * @param stepName
     *            the name of the step to start the flow after
     * @return this flow part, to ease creation of the condition and the
     *         first step of the flow
     * @throws NoSuchElementInModel
     * 		if the specified step is not found in a flow of this use case
     * 		            
     */
    public FlowPositionPart after(String stepName) {
	Step step = useCase.findStep(stepName);
	After after = new After(step);
	optionalFlowPositionPart = new FlowPositionPart(after);
	return optionalFlowPositionPart;
    }
    
    public class FlowPositionPart {
	private FlowPosition flowPosition;
	private Predicate<ModelRunner> optionalWhen;

	public FlowPositionPart(FlowPosition flowPosition) {
	    this.flowPosition = flowPosition;
	}

	public FlowPositionPart when(Predicate<ModelRunner> whenCondition) {
	    optionalWhen = whenCondition;
	    return this;
	}

	/**
	 * Creates the first step of this flow. It can be run when the
	 * flow's condition is fulfilled.
	 *
	 * @param stepName
	 *            the name of the step to be created
	 * @return the newly created step part, to ease creation of further steps
	 * @throws ElementAlreadyInModel
	 *             if a step with the specified name already exists in the use case
	 */
	public StepPart step(String stepName) {
	    FlowStep step = useCase.newInterruptingFlowStep(stepName, flow, flowPosition, optionalWhen);
	    StepPart stepPart = new StepPart(step, useCasePart, FlowPart.this);
	    return stepPart;
	}
    }

    /**
     * Creates the first step of this flow. It can be interrupted by any
     * other flow that has an explicit condition. It can be run when no
     * other step has been run before.
     *
     * @param stepName
     *            the name of the step to be created
     * @return the newly created step part, to ease creation of further steps
     * @throws ElementAlreadyInModel
     *             if a step with the specified name already exists in the use case
     */
    public StepPart step(String stepName) {
	FlowStep step = useCase.newInterruptableFlowStep(stepName, flow);
	StepPart stepPart = new StepPart(step, useCasePart, FlowPart.this);
	return stepPart;
    }

    /**
     * Starts the flow as an alternative to the specified step, in this flow's use
     * case.
     *
     * @param stepName
     *            the name of the specified step
     * @return this flow part, to ease creation of the condition and the
     *         first step of the flow
     * @throws NoSuchElementInModel
     *             if the specified step is not found in this flow's use case
     */
    public FlowPositionPart insteadOf(String stepName) {
	FlowStep step = (FlowStep)useCase.findStep(stepName);
	InsteadOf insteadOf = new InsteadOf(step);
	optionalFlowPositionPart = new FlowPositionPart(insteadOf);
	return optionalFlowPositionPart;
    }

    /**
     * Starts the flow after any step that has been run, or at the beginning.
     * 
     * @return this flow part, to ease creation of the condition and the
     *         first step of the flow
     */
    public FlowPositionPart anytime() {
	Anytime anytime = new Anytime();
	optionalFlowPositionPart = new FlowPositionPart(anytime);
	return optionalFlowPositionPart;
    }

    /**
     * Constrains the flow's condition: only if the specified condition is true as
     * well (beside the flow position), the flow is started.
     *
     * @param whenCondition
     *            the condition that constrains when the flow is started
     * @return this flow part, to ease creation of the condition and the
     *         first step of the flow
     */
    public FlowPositionPart when(Predicate<ModelRunner> whenCondition) {
	Objects.requireNonNull(whenCondition);

	optionalFlowPositionPart = new FlowPositionPart(new Anytime());
	FlowPositionPart when = optionalFlowPositionPart.when(whenCondition);
	return when;
    }

    Flow getFlow() {
	return flow;
    }

    UseCasePart getUseCasePart() {
	return useCasePart;
    }

    ModelBuilder getModelBuilder() {
	return useCasePart.getModelBuilder();
    }
}
