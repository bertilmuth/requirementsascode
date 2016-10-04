package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyExistsException;
import org.requirementsascode.exception.NoSuchElementExistsException;

public class UseCase extends UseCaseModelElement{
	private List<UseCaseFlow> flows;
	private LinkedList<UseCaseStep> steps;
	private UseCaseFlow basicFlow;

	UseCase(String name, UseCaseModel useCaseModel) {
		super(name, useCaseModel);
		this.flows = new ArrayList<>();
		this.steps = new LinkedList<>();
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
		flows.add(flow);
		return flow;
	}
	
	public UseCaseFlow getFlow(String flowName) {
		Objects.requireNonNull(flowName);

		UseCaseFlow flow = getUseCaseModelElementByName(flowName, flows);
		return flow;
	}
	
	public List<UseCaseFlow> getFlows() {
		return Collections.unmodifiableList(flows);
	}
	
	public UseCaseStep getStep(String stepName) {
		Objects.requireNonNull(stepName);

		UseCaseStep step = getUseCaseModelElementByName(stepName, steps);
		return step;
	}
	
	UseCaseStep newStep(String stepName, UseCaseFlow flow, UseCaseStep previousStep, Predicate<UseCaseRunner> predicate) {
		Objects.requireNonNull(stepName);
		Objects.requireNonNull(flow);
		
		if(hasStep(stepName)){
			throw new ElementAlreadyExistsException(stepName);
		}
		UseCaseStep newStep = new UseCaseStep(stepName, flow, previousStep, predicate);
		steps.add(newStep);
		return newStep;
	}
	
	public boolean hasStep(String stepName) {
		Objects.requireNonNull(stepName);
		
		boolean hasStep = steps.stream().anyMatch(step -> stepName.equals(step.getName()));
		return hasStep;
	}
	
	public boolean hasFlow(String flowName) {
		Objects.requireNonNull(flowName);
		
		boolean hasStep = flows.stream().anyMatch(hasName(flowName));
		return hasStep;
	}

	public List<UseCaseStep> getSteps() {
		return Collections.unmodifiableList(steps);
	}
	
	private <T extends UseCaseModelElement> T getUseCaseModelElementByName(String useCaseModelElementName, Collection<T> useCaseModelElements) {
		Optional<T> optionalUseCaseModelElement = useCaseModelElements.stream().filter(hasName(useCaseModelElementName)).findAny();
		T useCaseModelElement = optionalUseCaseModelElement.orElseThrow(() -> new NoSuchElementExistsException(useCaseModelElementName));
		return useCaseModelElement;
	}
	private Predicate<? super UseCaseModelElement> hasName(String name) {
		return useCaseModelElement -> name.equals(useCaseModelElement.getName());
	}
}
