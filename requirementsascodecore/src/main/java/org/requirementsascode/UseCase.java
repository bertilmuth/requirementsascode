package org.requirementsascode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

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

	public UseCaseFlow basicFlow() {
		return basicFlow;
	}
	
	public boolean hasFlow(String flowName) {		
		boolean hasFlow = UseCaseModel.hasModelElement(flowName, nameToFlowMap);
		return hasFlow;
	}
	
	public UseCaseFlow newFlow(String flowName) {
		Objects.requireNonNull(flowName);
 
		UseCaseFlow flow = new UseCaseFlow(flowName, this);
		UseCaseModel.saveModelElement(flow, nameToFlowMap);
		return flow;
	}
	
	public Optional<UseCaseFlow> findFlow(String flowName) {
		Optional<UseCaseFlow> flow = UseCaseModel.findModelElement(flowName, nameToFlowMap);
		return flow;
	}
	
	public List<UseCaseFlow> getFlows() {
		return UseCaseModel.getModelElements(nameToFlowMap);
	}
	
	public boolean hasStep(String stepName) {
		boolean hasStep = UseCaseModel.hasModelElement(stepName, nameToStepMap);		
		return hasStep;
	}
	
	public Optional<UseCaseStep> findStep(String stepName) {
		Optional<UseCaseStep> step = UseCaseModel.findModelElement(stepName, nameToStepMap);
		return step;
	}
	
	UseCaseStep newStep(String stepName, UseCaseFlow flow, Optional<UseCaseStep> previousStep, Predicate<UseCaseRunner> predicate) {
		UseCaseStep newStep = new UseCaseStep(stepName, flow, previousStep, predicate);
		UseCaseModel.saveModelElement(newStep, nameToStepMap);
		return newStep;
	}

	public List<UseCaseStep> getSteps() {
		return UseCaseModel.getModelElements(nameToStepMap);
	}
}