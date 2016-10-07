package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyExistsInModelException;

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

		if(hasFlow(flowName)){
			throw new ElementAlreadyExistsInModelException(flowName);
		}
		UseCaseFlow flow = new UseCaseFlow(flowName, this);
		flows.put(flowName, flow);
		return flow;
	}
	
	public Optional<UseCaseFlow> findFlow(String flowName) {
		Optional<UseCaseFlow> flow = UseCaseModel.findModelElement(flowName, flows);
		return flow;
	}
	
	public List<UseCaseFlow> getFlows() {
		ArrayList<UseCaseFlow> flowsList = new ArrayList<>();
		flowsList.addAll(flows.values());
		return Collections.unmodifiableList(flowsList);
	}
	
	public boolean hasStep(String stepName) {
		boolean hasStep = UseCaseModel.hasModelElement(stepName, steps);		
		return hasStep;
	}
	
	public Optional<UseCaseStep> findStep(String stepName) {
		Optional<UseCaseStep> step = UseCaseModel.findModelElement(stepName, steps);
		return step;
	}
	
	UseCaseStep newStep(String stepName, UseCaseFlow flow, Optional<UseCaseStep> optionalPreviousStep, Predicate<UseCaseRunner> predicate) {
		Objects.requireNonNull(stepName);
		Objects.requireNonNull(flow);
		if(hasStep(stepName)){
			throw new ElementAlreadyExistsInModelException(stepName);
		}
		UseCaseStep newStep = new UseCaseStep(stepName, flow, optionalPreviousStep, predicate);
		steps.put(stepName, newStep);
		return newStep;
	}

	public List<UseCaseStep> getSteps() {
		ArrayList<UseCaseStep> stepsList = new ArrayList<>();
		stepsList.addAll(steps.values());
		return Collections.unmodifiableList(stepsList);
	}
}