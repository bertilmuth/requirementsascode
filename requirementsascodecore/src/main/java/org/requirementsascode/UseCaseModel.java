package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.requirementsascode.exception.ElementAlreadyInModelException;

public class UseCaseModel {
	private Map<String, Actor> nameToActorMap;
	private Map<String, UseCase> nameToUseCaseMap;
	private UseCaseRunner useCaseRunner;
	private Actor autonomousSystemReactionActor;
	
	UseCaseModel(UseCaseRunner useCaseModelRun) {
		this.nameToActorMap = new HashMap<>();
		this.nameToUseCaseMap = new HashMap<>();
		this.autonomousSystemReactionActor = newActor("Autonomous System Reaction Actor");
		this.useCaseRunner = useCaseModelRun;
	}

	public boolean hasActor(String actorName) {		
		return hasModelElement(actorName, nameToActorMap);
	}
	
	public Actor newActor(String actorName) {
		Objects.requireNonNull(actorName);
		Actor actor = new Actor(actorName, this);
		saveModelElement(actor, nameToActorMap);
		return actor;
	}

	public boolean hasUseCase(String useCaseName) {		
		return hasModelElement(useCaseName, nameToUseCaseMap);
	}

	public UseCase newUseCase(String useCaseName) {		
		UseCase useCase = new UseCase(useCaseName, this);
		saveModelElement(useCase, nameToUseCaseMap);
		return useCase;
	}
	
	public Optional<Actor> findActor(String actorName) {
		Optional<Actor> actor = findModelElement(actorName, nameToActorMap);
		return actor;
	}

	public Optional<UseCase> findUseCase(String useCaseName) {
		Optional<UseCase> useCase = findModelElement(useCaseName, nameToUseCaseMap);
		return useCase;
	}

	public Collection<Actor> getActors() {
		return Collections.unmodifiableCollection(nameToActorMap.values());
	}

	public Collection<UseCase> getUseCases() {
		return Collections.unmodifiableCollection(nameToUseCaseMap.values());
	}
	
	public Set<UseCaseStep> getUseCaseSteps() {
		return getUseCases().stream()
			.map(useCase -> useCase.getSteps())
			.flatMap(steps -> steps.stream())
			.collect(Collectors.toSet());
	}
	
	public UseCaseRunner getUseCaseRunner() {
		return useCaseRunner;
	}

	public Actor getAutonomousSystemActor() {
		return autonomousSystemReactionActor;
	}
	
	static <T extends UseCaseModelElement> Optional<T> findModelElement(String modelElementName, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);
		Optional<T> optionalUseCaseModelElement = modelElementNameToElementMap.containsKey(modelElementName)?
			Optional.of(modelElementNameToElementMap.get(modelElementName)) : Optional.empty();
		return optionalUseCaseModelElement;
	}
	
	static <T extends UseCaseModelElement> boolean hasModelElement(String modelElementName, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);
		return findModelElement(modelElementName, modelElementNameToElementMap).isPresent();
	}
	
	static <T extends UseCaseModelElement> void saveModelElement(T modelElement, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElement);
		Objects.requireNonNull(modelElementNameToElementMap);
		String modelElementName = modelElement.getName();
		if(hasModelElement(modelElementName, modelElementNameToElementMap)){
			throw new ElementAlreadyInModelException(modelElementName);
		}
		modelElementNameToElementMap.put(modelElementName, modelElement);
	}
	
	static <T extends UseCaseModelElement> List<T> getModelElements(Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementNameToElementMap);
		ArrayList<T> modelElementList = new ArrayList<>();
		modelElementList.addAll(modelElementNameToElementMap.values());
		return Collections.unmodifiableList(modelElementList);
	}
}
