package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.predicate.ReactWhile;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 *
 * @see FlowStep#setSystemReaction(Consumer)
 * @author b_muth
 */
public class StepSystemPart<T> {
    private FlowStepPart flowStepPart;
    private FlowStep step;

    StepSystemPart(FlowStepPart flowStepPart, Consumer<T> systemReaction) {
	this.flowStepPart = flowStepPart;
	this.step = flowStepPart.getStep();
	step.setSystemReaction(systemReaction);
    }

    public UseCaseModel build() {
	return flowStepPart.getUseCaseModelBuilder().build();
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

	FlowPart flowPart = flowStepPart.getFlowPart();
	FlowStep trailingStepInFlow = createTrailingStepInFlow(stepName, flowPart);
	StepPart trailingStepInFlowPart = new FlowStepPart(trailingStepInFlow, flowStepPart.getUseCasePart(), flowPart);

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

	FlowPart useCaseFlowPart = flowStepPart.getUseCasePart().flow(flowName);
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

	UseCasePart useCasePart = flowStepPart.getUseCaseModelBuilder().useCase(useCaseName);
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
    public StepSystemPart<T> reactWhile(Predicate<UseCaseModelRunner> reactWhileCondition) {
	Objects.requireNonNull(reactWhileCondition);

	ReactWhile reactWhile = new ReactWhile(step, reactWhileCondition);
	step.setReactWhile(reactWhile);

	return this;
    }
}
