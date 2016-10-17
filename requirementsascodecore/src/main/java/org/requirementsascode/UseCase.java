package org.requirementsascode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyInModelException;

import static org.requirementsascode.ModelElementContainer.*;

/**
 * A use case, as part of a use case model.
 * 
 * As an example, a use case for an ATM is "Get cash".
 * As another example, a use case for an online flight reservation system is "Book flight".
 * 
 * The use case itself defines no behavior. The use case steps that are part
 * of the use case define the behavior of the use case. As steps are often performed one
 * after the other, in sequence, they are grouped in use case flows.
 * 
 * @author b_muth
 *
 */
public class UseCase extends UseCaseModelElement{
	private Map<String, UseCaseFlow> nameToFlowMap;
	private Map<String, UseCaseStep> nameToStepMap;
	private UseCaseFlow basicFlow;

	UseCase(String name, UseCaseModel useCaseModel) {
		super(name, useCaseModel);
		this.nameToFlowMap = new LinkedHashMap<>();
		this.nameToStepMap = new LinkedHashMap<>();
		this.basicFlow = newFlow("Basic Flow");
	} 

	/**
	 * The basic flow defines the 'happy day scenario' of the use case:
	 * no exceptions are handled in it, all steps are assumed to go well.
	 * 
	 * The basic flow is a sequence of use case steps that lead the
	 * user to the user's goal. There is exactly one basic flow per use case.
	 * 
	 * @return the basic flow of the use case
	 */
	public UseCaseFlow basicFlow() {
		return basicFlow;
	}
	
	/**
	 * Checks whether this use case contains the specified flow.
	 * 
	 * @param flowName name of the flow whose existence to check
	 * 
	 * @return true if this use case contains the specified flow, false otherwise
	 */
	public boolean hasFlow(String flowName) {		
		boolean hasFlow = hasModelElement(flowName, nameToFlowMap);
		return hasFlow;
	}
	
	/**
	 * Checks whether this use case contains the specified step.
	 * 
	 * @param stepName name of the step whose existence to check
	 * 
	 * @return true if this use case contains the specified step, false otherwise 
	 */
	public boolean hasStep(String stepName) {
		boolean hasStep = hasModelElement(stepName, nameToStepMap);		
		return hasStep;
	}
	
	/**
	 * Creates a new flow in this use case.
	 * 
	 * @param flowName the name of the flow to be created.
	 * 
	 * @return the newly created flow
	 * @throws ElementAlreadyInModelException if a flow with the specified name already exists in the use case
	 */
	public UseCaseFlow newFlow(String flowName) {
		UseCaseFlow flow = new UseCaseFlow(flowName, this);
		saveModelElement(flow, nameToFlowMap);
		return flow;
	}
	
	UseCaseStep newStep(String stepName, UseCaseFlow flow, Optional<UseCaseStep> previousStep, Optional<Predicate<UseCaseRunner>> predicate) {
		UseCaseStep step = new UseCaseStep(stepName, flow, previousStep, predicate);
		saveModelElement(step, nameToStepMap);
		return step;
	}
	
	/**
	 * Finds the flow with the specified name, contained in this use case.
	 * 
	 * @param flowName the name of the flow to look for
	 * 
	 * @return the flow if found, or else an empty optional
	 */
	public Optional<UseCaseFlow> findFlow(String flowName) {
		Optional<UseCaseFlow> flow = findModelElement(flowName, nameToFlowMap);
		return flow;
	}
	
	/**
	 * Finds the step with the specified name, contained in this use case.
	 * 
	 * @param stepName the name of the step to look for
	 * 
	 * @return the step if found, or else an empty optional
	 */
	public Optional<UseCaseStep> findStep(String stepName) {
		Optional<UseCaseStep> step = findModelElement(stepName, nameToStepMap);
		return step;
	}
	
	/**
	 * Returns the flows contained in this use case.
	 * Do not modify that list directly, use {@link #newFlow(String)}.
	 * 
	 * @return a list of the flows
	 */
	public List<UseCaseFlow> getFlows() {
		return getModelElements(nameToFlowMap);
	}
	
	/**
	 * Returns the steps contained in this use case.
	 * Do not modify that list directly, use {@link UseCaseFlow#newStep(String)}
	 * 
	 * @return a list of the steps
	 */
	public List<UseCaseStep> getSteps() {
		return getModelElements(nameToStepMap);
	}
}