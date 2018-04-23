package org.requirementsascode;

import static org.requirementsascode.ModelElementContainer.findModelElement;
import static org.requirementsascode.ModelElementContainer.getModelElements;
import static org.requirementsascode.ModelElementContainer.hasModelElement;
import static org.requirementsascode.ModelElementContainer.saveModelElement;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.NoSuchElementInModel;
import org.requirementsascode.flowposition.Anytime;
import org.requirementsascode.flowposition.FlowPosition;

/**
 * A use case, as part of a model.
 *
 * <p>
 * As an example, a use case for an ATM is "Get cash". As another example, a use
 * case for an online flight reservation system is "Book flight".
 *
 * <p>
 * The use case itself defines no behavior. The steps that are part of
 * the use case define the behavior of the use case. As steps are often
 * performed one after the other, in sequence, they are grouped in use case
 * flows.
 *
 * @author b_muth
 */
public class UseCase extends ModelElement implements Serializable {
    private static final long serialVersionUID = 4939249650285018834L;

    private static final String BASIC_FLOW = "Basic flow";

    private Map<String, Flow> nameToFlowMap;
    private Map<String, Step> nameToStepMap;
    private Flow basicFlow;

    /**
     * Creates a use case with the specified name that belongs to the specified model.
     *
     * @param useCaseName
     *            the name of the use case to be created
     * @param model
     *            the model that will contain the new use case
     */
    UseCase(String useCaseName, Model model) {
	super(useCaseName, model);
	this.nameToFlowMap = new LinkedHashMap<>();
	this.nameToStepMap = new LinkedHashMap<>();
	this.basicFlow = newFlow(BASIC_FLOW);
    }

    /**
     * The basic flow defines the 'happy day scenario' of the use case: no
     * exceptions are handled in it, all steps are assumed to go well.
     *
     * <p>
     * The basic flow is a sequence of steps that lead the user to the
     * user's goal. There is exactly one basic flow per use case.
     *
     * @return the basic flow of the use case
     */
    public Flow getBasicFlow() {
	return basicFlow;
    }

    /**
     * Checks whether this use case contains the specified flow.
     *
     * @param flowName
     *            the name of the flow whose existence to check
     * @return true if this use case contains the specified flow, false otherwise
     */
    public boolean hasFlow(String flowName) {
	boolean hasFlow = hasModelElement(flowName, nameToFlowMap);
	return hasFlow;
    }

    /**
     * Checks whether this use case contains the specified step.
     *
     * @param stepName
     *            the name of the step whose existence to check
     * @return true if this use case contains the specified step, false otherwise
     */
    public boolean hasStep(String stepName) {
	boolean hasStep = hasModelElement(stepName, nameToStepMap);
	return hasStep;
    }

    /**
     * Creates a new flow in this use case.
     *
     * @param flowName
     *            the name of the flow to be created.
     * @return the newly created flow
     * @throws ElementAlreadyInModel
     *             if a flow with the specified name already exists in the use case
     */
    Flow newFlow(String flowName) {
	Flow flow = new Flow(flowName, this);
	saveModelElement(flow, nameToFlowMap);
	return flow;
    }

    /**
     * Creates a new step that can interrupt other flows. This is the first step of
     * a flow with a defined flow position and/or when condition. The flow position
     * and when condition are the same as defined for the flow.
     *
     * @param stepName
     *            the name of the step
     * @param flow
     *            the flow the step shall belong to
     * @param flowPosition
     *            the flow position, may be null, meaning: anytime.
     * @param when
     *            the when condition, may be null if there is none.
     * @return the newly created step
     */
    InterruptingFlowStep newInterruptingFlowStep(String stepName, Flow flow, FlowPosition flowPosition,
	    Predicate<ModelRunner> when) {
	InterruptingFlowStep step = new InterruptingFlowStep(stepName, this, flow);
	if(flowPosition == null) {
	    flowPosition = new Anytime();
	}
	step.setFlowPosition(flowPosition);
	step.setWhen(when);

	saveModelElement(step, nameToStepMap);

	return step;
    }

    /**
     * Creates a step that can be "interrupted", that is: interrupting steps are
     * performed instead of this step when their flow position is correct and their
     * condition is fulfilled.
     * 
     * All steps of a flow are interruptable steps, with the potential exception of
     * the first step. The first step is interruptable as well iff no flow position
     * and no when condition are specified.
     * 
     * So, for example, if you build
     * <code>builder.useCase("UC1").basicFlow().step("S1")</code>, then S1 is an
     * interruptable step as well.
     *
     * @param stepName
     *            the name of the step
     * @param flow
     *            the flow the step shall belong to
     * @return the newly created step
     */
    InterruptableFlowStep newInterruptableFlowStep(String stepName, Flow flow) {
	InterruptableFlowStep step = new InterruptableFlowStep(stepName, this, flow);
	saveModelElement(step, nameToStepMap);

	return step;
    }

    /**
     * Creates a step that is independent of a flow.
     *
     * @param stepName
     *            the name of the step
     * @return the newly created step
     */
    FlowlessStep newFlowlessStep(String stepName) {
	FlowlessStep step = new FlowlessStep(stepName, this);
	saveModelElement(step, nameToStepMap);

	return step;
    }

    /**
     * Finds the flow with the specified name, contained in this use case.
     *
     * @param flowName
     *            the name of the flow to look for
     * @return the flow if found, or else an empty optional
     * @throws NoSuchElementInModel
     *             if no flow with the specified flowName is found in the current
     *             use case
     */
    public Flow findFlow(String flowName) {
	Flow flow = findModelElement(flowName, nameToFlowMap);
	return flow;
    }

    /**
     * Finds the step with the specified name, contained in this use case.
     *
     * @param stepName
     *            the name of the step to look for
     * @return the step if found, or else an empty optional
     * @throws NoSuchElementInModel
     *             if no step with the specified stepName is found in the current
     *             use case
     */
    public Step findStep(String stepName) {
	Step step = findModelElement(stepName, nameToStepMap);
	return step;
    }

    /**
     * Returns the flows contained in this use case.
     *
     * @return a collection of the flows
     */
    public Collection<Flow> getFlows() {
	Collection<Flow> modifiableFlows = getModelElements(nameToFlowMap);
	return Collections.unmodifiableCollection(modifiableFlows);
    }

    /**
     * Returns the steps contained in this use case.
     *
     * @return a collection of the steps
     */
    public Collection<Step> getSteps() {
	Collection<Step> modifiableSteps = getModifiableSteps();
	return Collections.unmodifiableCollection(modifiableSteps);
    }

    Collection<Step> getModifiableSteps() {
	return getModelElements(nameToStepMap);
    }
}
