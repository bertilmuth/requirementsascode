package org.requirementsascode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
	
	public boolean hasStep(String stepName) {
		boolean hasStep = UseCaseModel.hasModelElement(stepName, nameToStepMap);		
		return hasStep;
	}
	
	public UseCaseFlow newFlow(String flowName) {
		UseCaseFlow flow = new UseCaseFlow(flowName, this);
		UseCaseModel.saveModelElement(flow, nameToFlowMap);
		return flow;
	}
	
	UseCaseStep newStep(String stepName, UseCaseFlow flow, Optional<UseCaseStep> optionalPreviousStep, Predicate<UseCaseRunner> predicate) {
		UseCaseStep step = new UseCaseStep(stepName, flow, optionalPreviousStep, predicate);
		UseCaseModel.saveModelElement(step, nameToStepMap);
		return step;
	}
	
	public Optional<UseCaseFlow> findFlow(String flowName) {
		Optional<UseCaseFlow> flow = UseCaseModel.findModelElement(flowName, nameToFlowMap);
		return flow;
	}
	
	public Optional<UseCaseStep> findStep(String stepName) {
		Optional<UseCaseStep> step = UseCaseModel.findModelElement(stepName, nameToStepMap);
		return step;
	}
	
	public List<UseCaseFlow> getFlows() {
		return UseCaseModel.getModelElements(nameToFlowMap);
	}
	
	public List<UseCaseStep> getSteps() {
		return UseCaseModel.getModelElements(nameToStepMap);
	}
}