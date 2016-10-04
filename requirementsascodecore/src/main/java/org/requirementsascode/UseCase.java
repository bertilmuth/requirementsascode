package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyExistsException;
import org.requirementsascode.exception.NoSuchElementExistsException;

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
	
	public UseCaseFlow newFlow(String flowName) {
		Objects.requireNonNull(flowName);

		if(hasFlow(flowName)){
			throw new ElementAlreadyExistsException(flowName);
		}
		UseCaseFlow flow = new UseCaseFlow(flowName, this);
		flows.put(flowName, flow);
		return flow;
	}
	
	public UseCaseFlow getFlow(String flowName) {
		Objects.requireNonNull(flowName);

		UseCaseFlow flow = getByNameOrThrowException(flowName, flows);
		return flow;
	}
	
	public List<UseCaseFlow> getFlows() {
		ArrayList<UseCaseFlow> flowsList = new ArrayList<>();
		flowsList.addAll(flows.values());
		return Collections.unmodifiableList(flowsList);
	}
	
	public UseCaseStep getStep(String stepName) {
		Objects.requireNonNull(stepName);

		UseCaseStep step = getByNameOrThrowException(stepName, steps);
		return step;
	}
	
	UseCaseStep newStep(String stepName, UseCaseFlow flow, UseCaseStep previousStep, Predicate<UseCaseRunner> predicate) {
		Objects.requireNonNull(stepName);
		Objects.requireNonNull(flow);
		
		if(hasStep(stepName)){
			throw new ElementAlreadyExistsException(stepName);
		}
		UseCaseStep newStep = new UseCaseStep(stepName, flow, previousStep, predicate);
		steps.put(stepName, newStep);
		return newStep;
	}
	
	public boolean hasStep(String stepName) {
		Objects.requireNonNull(stepName);
		
		boolean hasStep = steps.containsKey(stepName);
		return hasStep;
	}
	
	public boolean hasFlow(String flowName) {
		Objects.requireNonNull(flowName);
		
		boolean hasStep = flows.containsKey(flowName); 
		return hasStep;
	}

	public List<UseCaseStep> getSteps() {
		ArrayList<UseCaseStep> stepsList = new ArrayList<>();
		stepsList.addAll(steps.values());
		return Collections.unmodifiableList(stepsList);
	}
	
	private <T extends UseCaseModelElement> T getByNameOrThrowException(String useCaseModelElementName, Map<String, T> useCaseModelElements) {
		T useCaseModelElement = useCaseModelElements.get(useCaseModelElementName);
		if(useCaseModelElement == null){
			throw new NoSuchElementExistsException(useCaseModelElementName);
		}
		return useCaseModelElement;
	}
}