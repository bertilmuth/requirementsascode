package org.requirementsascode;

import static org.requirementsascode.ModelElementContainer.findModelElement;
import static org.requirementsascode.ModelElementContainer.hasModelElement;
import static org.requirementsascode.ModelElementContainer.saveModelElement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UseCaseModel {
	private Map<String, Actor> nameToActorMap;
	private Map<String, UseCase> nameToUseCaseMap;
	private UseCaseRunner useCaseRunner;
	private Actor systemActor;
	
	UseCaseModel(UseCaseRunner useCaseModelRun) {
		this.nameToActorMap = new HashMap<>();
		this.nameToUseCaseMap = new HashMap<>();
		this.systemActor = newActor("Autonomous System Reaction Actor");
		this.useCaseRunner = useCaseModelRun;
	}

	public boolean hasActor(String actorName) {		
		boolean hasActor = hasModelElement(actorName, nameToActorMap);
		return hasActor;
	}

	public boolean hasUseCase(String useCaseName) {		
		boolean hasUseCase = hasModelElement(useCaseName, nameToUseCaseMap);
		return hasUseCase;
	}
	
	public Actor newActor(String actorName) {
		Actor actor = new Actor(actorName, this);
		saveModelElement(actor, nameToActorMap);
		return actor;
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

	public Actor getSystemActor() {
		return systemActor;
	}
}
