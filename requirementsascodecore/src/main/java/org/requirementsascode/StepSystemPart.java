package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.flowposition.FlowPosition;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see FlowStep#setSystemReaction(Consumer)
 * @author b_muth
 */
public class StepSystemPart<T> {
    private StepPart stepPart;
    private Step step;

    StepSystemPart(StepPart stepPart, Consumer<T> systemReaction) {
	this.stepPart = stepPart;
	this.step = stepPart.getStep();
	step.setSystemReaction(systemReaction);
    }

    public Model build() {
	return stepPart.getModelBuilder().build();
    }

    /**
     * Creates a new step in this flow, with the specified name, that follows the
     * current step in sequence.
     *
     * @param stepName
     *            the name of the step to be created
     * @return the newly created step
     * @throws ElementAlreadyInModel
     *             if a step with the specified name already exists in the use case
     */
    public StepPart step(String stepName) {
	Objects.requireNonNull(stepName);

	FlowPart flowPart = stepPart.getFlowPart();
	FlowStep trailingStepInFlow = createTrailingStepInFlow(stepName, flowPart);
	StepPart trailingStepInFlowPart = new StepPart(trailingStepInFlow, stepPart.getUseCasePart(), flowPart);

	return trailingStepInFlowPart;
    }
    
    FlowStep createTrailingStepInFlow(String stepName, FlowPart flowPart) {
	Flow flow = flowPart.getFlow();
	FlowStep newTrailingStep = step.getUseCase().newInterruptableFlowStep(stepName, flow);
	return newTrailingStep;
    }

    /**
     * Creates a new flow in the current use case.
     *
     * @param flowName
     *            the name of the flow to be created.
     * @return the newly created flow part
     * @throws ElementAlreadyInModel
     *             if a flow with the specified name already exists in the use case
     */
    public FlowPart flow(String flowName) {
	Objects.requireNonNull(flowName);

	FlowPart useCaseFlowPart = stepPart.getUseCasePart().flow(flowName);
	return useCaseFlowPart;
    }

    /**
     * Creates a new use case in the current model.
     *
     * @param useCaseName
     *            the name of the use case to be created.
     * @return the newly created use case part
     * @throws ElementAlreadyInModel
     *             if a use case with the specified name already exists in the model
     */
    public UseCasePart useCase(String useCaseName) {
	Objects.requireNonNull(useCaseName);

	UseCasePart useCasePart = stepPart.getModelBuilder().useCase(useCaseName);
	return useCasePart;
    }

    /**
     * React to this step's event as long as the condition is fulfilled.
     *
     * <p>
     * Even when the condition is fulfilled, the flow can advance given that the
     * event of the next step is received.
     *
     * <p>
     * Note that if the condition is not fulfilled after the previous step has been
     * performed, the step will not react at all.
     *
     * @param reactWhileCondition
     *            the condition to check
     * @return the system part
     */
    public StepSystemPart<T> reactWhile(Condition reactWhileCondition) {
	Objects.requireNonNull(reactWhileCondition);
	((FlowStep)step).setReactWhile(reactWhileCondition); 
	createLoop((FlowStep)step);

	return this;
    }
    
    private void createLoop(FlowStep step) {
	FlowPosition flowPosition = step.getFlowPosition();
	flowPosition.orAfter(step);
    }
}
