package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseFlow.ConditionalPart;
import org.requirementsascode.exception.ElementAlreadyExistsException;
import org.requirementsascode.exception.NoSuchElementExistsException;

public class UseCase extends UseCaseModelElement{
	private List<UseCaseFlow> flows;
	private LinkedList<UseCaseStep> steps;
	private ConditionalPart basicFlow;

	UseCase(String name, UseCaseModel useCaseModel) {
		super(name, useCaseModel);
		this.flows = new ArrayList<>();
		this.steps = new LinkedList<>();
		this.basicFlow = newFlow("Basic Flow");
	} 

	public ConditionalPart basicFlow() {
		return basicFlow;
	}
	
	public ConditionalPart newFlow(String flowName) {
		Objects.requireNonNull(flowName);

		if(hasFlow(flowName)){
			throw new ElementAlreadyExistsException(flowName);
		}
		UseCaseFlow flow = new UseCaseFlow(flowName, this);
		flows.add(flow);
		return flow.newConditionalPart();
	}
	
	public UseCaseFlow getFlow(String flowName) {
		Objects.requireNonNull(flowName);

		Optional<UseCaseFlow> optionalFlow = flows.stream().filter(flow -> flowName.equals(flow.getName())).findAny();
		if(!optionalFlow.isPresent()){
			throw new NoSuchElementExistsException(flowName);
		}
		return optionalFlow.get();
	}
	
	public List<UseCaseFlow> getFlows() {
		return Collections.unmodifiableList(flows);
	}
	
	public UseCaseStep getStep(String stepName) {
		Objects.requireNonNull(stepName);

		Optional<UseCaseStep> optionalStep = steps.stream().filter(step -> stepName.equals(step.getName())).findAny();
		if(!optionalStep.isPresent()){
			throw new NoSuchElementExistsException(stepName);
		}
		return optionalStep.get();
	}
	
	UseCaseStep newStep(String stepName, UseCaseFlow flow, UseCaseStep previousStep, Predicate<UseCaseModelRun> predicate) {
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
		
		boolean hasStep = flows.stream().anyMatch(flow -> flowName.equals(flow.getName()));
		return hasStep;
	}

	public List<UseCaseStep> getSteps() {
		return Collections.unmodifiableList(steps);
	}
}
