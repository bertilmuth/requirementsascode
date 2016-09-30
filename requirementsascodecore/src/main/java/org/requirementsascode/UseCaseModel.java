package org.requirementsascode;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.requirementsascode.exception.ElementAlreadyExistsException;
import org.requirementsascode.exception.NoSuchElementExistsException;

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
		Objects.requireNonNull(actorName);
		
		return getActorByName(actorName)!=null;
	}
	
	private Actor getActorByName(String actorName) {
		return nameToActorMap.get(actorName);
	}
	
	public Actor newActor(String actorName) {
		Objects.requireNonNull(actorName);
		
		if(hasActor(actorName)){
			throw new ElementAlreadyExistsException(actorName);
		}
		Actor actor = new Actor(actorName, this);
		nameToActorMap.put(actorName, actor);
		return actor;
	}

	public boolean hasUseCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		
		return getUseCaseByName(useCaseName)!=null;
	}
	
	private UseCase getUseCaseByName(String useCaseName) {		
		return nameToUseCaseMap.get(useCaseName);
	}

	public UseCase newUseCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		
		if(hasUseCase(useCaseName)){
			throw new ElementAlreadyExistsException(useCaseName);
		}
		UseCase useCase = new UseCase(useCaseName, this);
		nameToUseCaseMap.put(useCaseName, useCase);
		return useCase;
	}
	
	public Actor getActor(String actorName) {
		Objects.requireNonNull(actorName);
		
		if(!hasActor(actorName)){
			throw new NoSuchElementExistsException(actorName);
		}
		Actor existingActor = getActorByName(actorName);
		return existingActor;
	}

	public UseCase getUseCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		
		if(!hasUseCase(useCaseName)){
			throw new NoSuchElementExistsException(useCaseName);
		}
		UseCase existingUseCase = getUseCaseByName(useCaseName);
		return existingUseCase;
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

	public Actor getAutonomousSystemReactionActor() {
		return autonomousSystemReactionActor;
	}
}
