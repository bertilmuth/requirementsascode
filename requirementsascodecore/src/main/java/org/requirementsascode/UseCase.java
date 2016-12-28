package org.requirementsascode;

import static org.requirementsascode.ModelElementContainer.findModelElement;
import static org.requirementsascode.ModelElementContainer.getModelElements;
import static org.requirementsascode.ModelElementContainer.hasModelElement;
import static org.requirementsascode.ModelElementContainer.saveModelElement;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyInModelException;

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

	/**
	 * Creates a use case with the specified name that 
	 * belongs to the specified use case model.
	 * 
	 * @param useCaseName the name of the use case to be created
	 * @param useCaseModel the use case model that will contain the new use case
	 */
	UseCase(String useCaseName, UseCaseModel useCaseModel) {
		super(useCaseName, useCaseModel);
		this.nameToFlowMap = new LinkedHashMap<>();
		this.nameToStepMap = new LinkedHashMap<>();
		this.basicFlow = flow("Basic Flow");
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
	 * @param flowName the name of the flow whose existence to check
	 * @return true if this use case contains the specified flow, false otherwise
	 */
	public boolean hasFlow(String flowName) {		
		boolean hasFlow = hasModelElement(flowName, nameToFlowMap);
		return hasFlow;
	}
	
	/**
	 * Checks whether this use case contains the specified step.
	 * 
	 * @param stepName the name of the step whose existence to check
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
	 * @return the newly created flow
	 * @throws ElementAlreadyInModelException if a flow with the specified name already exists in the use case
	 */
	public UseCaseFlow flow(String flowName) {
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
	 * @return the step if found, or else an empty optional
	 */
	public Optional<UseCaseStep> findStep(String stepName) {
		Optional<UseCaseStep> step = findModelElement(stepName, nameToStepMap);
		return step;
	}
	
	/**
	 * Returns the flows contained in this use case.
	 * Do not modify the returned collection directly, use {@link #flow(String)}.
	 * 
	 * @return a collection of the flows
	 */
	public Collection<UseCaseFlow> flows() {
		return getModelElements(nameToFlowMap);
	}
	
	/**
	 * Returns the steps contained in this use case.
	 * Do not modify the returned collection directly, use {@link UseCaseFlow#step(String)}
	 * 
	 * @return a collection of the steps
	 */
	public Collection<UseCaseStep> steps() {
		return getModelElements(nameToStepMap);
	}
}