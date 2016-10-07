package org.requirementsascode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class UseCase extends UseCaseModelElement{
	private Map<String, UseCaseFlow> flows;
	private Map<String, UseCaseStep> steps;
	private UseCaseFlow basicFlow;

	UseCase(String name, UseCaseModel useCaseModel) {
		super(name, useCaseModel);
		this.flows = new LinkedHashMap<>();
		this.steps = new LinkedHashMap<>();
		this.basicFlow = newFlow("Basic Flow");
	} 

	public UseCaseFlow basicFlow() {
		return basicFlow;
	}
	
	public boolean hasFlow(String flowName) {		
		boolean hasFlow = UseCaseModel.hasModelElement(flowName, flows);
		return hasFlow;
	}
	
	public UseCaseFlow newFlow(String flowName) {
		Objects.requireNonNull(flowName);
 
		UseCaseFlow flow = new UseCaseFlow(flowName, this);
		UseCaseModel.saveModelElement(flow, flows);
		return flow;
	}
	
	public Optional<UseCaseFlow> findFlow(String flowName) {
		Optional<UseCaseFlow> flow = UseCaseModel.findModelElement(flowName, flows);
		return flow;
	}
	
	public List<UseCaseFlow> getFlows() {
		return UseCaseModel.getModelElements(flows);
	}
	
	public boolean hasStep(String stepName) {
		boolean hasStep = UseCaseModel.hasModelElement(stepName, steps);		
		return hasStep;
	}
	
	public Optional<UseCaseStep> findStep(String stepName) {
		Optional<UseCaseStep> step = UseCaseModel.findModelElement(stepName, steps);
		return step;
	}
	
	UseCaseStep newStep(String stepName, UseCaseFlow flow, Optional<UseCaseStep> previousStep, Predicate<UseCaseRunner> predicate) {
		UseCaseStep newStep = new UseCaseStep(stepName, flow, previousStep, predicate);
		UseCaseModel.saveModelElement(newStep, steps);
		return newStep;
	}

	public List<UseCaseStep> getSteps() {
		return UseCaseModel.getModelElements(steps);
	}
}