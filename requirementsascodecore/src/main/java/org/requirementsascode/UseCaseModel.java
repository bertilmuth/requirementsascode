package org.requirementsascode;

import static org.requirementsascode.ModelElementContainer.findModelElement;
import static org.requirementsascode.ModelElementContainer.getModelElements;
import static org.requirementsascode.ModelElementContainer.hasModelElement;
import static org.requirementsascode.ModelElementContainer.saveModelElement;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.requirementsascode.exception.ElementAlreadyInModelException;

/**
 * A use case model is a container for use cases and their associated actors.
 * It is used to configure a {@link UseCaseRunner}.
 * 
 * @author b_muth
 *
 */
public class UseCaseModel {
	private Map<String, Actor> nameToActorMap;
	private Map<String, UseCase> nameToUseCaseMap;
	private UseCaseRunner useCaseRunner;
	private Actor userActor;
	private Actor systemActor;
	
	UseCaseModel(UseCaseRunner useCaseModelRun) {
		this.nameToActorMap = new HashMap<>();
		this.nameToUseCaseMap = new HashMap<>();
		this.userActor = newActor("User");
		this.systemActor = newActor("Autonomous System Reaction Actor");
		this.useCaseRunner = useCaseModelRun;
	}

	/**
	 * Checks whether this model contains the specified actor.
	 * 
	 * @param actorName name of the actor whose existence to check
	 * 
	 * @return true if this model contains the specified actor, false otherwise 
	 */
	public boolean hasActor(String actorName) {		
		boolean hasActor = hasModelElement(actorName, nameToActorMap);
		return hasActor;
	}

	/**
	 * Checks whether this model contains the specified use case.
	 * 
	 * @param useCaseName name of the use case whose existence to check
	 * 
	 * @return true if this model contains the specified use case, false otherwise 
	 */
	public boolean hasUseCase(String useCaseName) {		
		boolean hasUseCase = hasModelElement(useCaseName, nameToUseCaseMap);
		return hasUseCase;
	}
	
	/**
	 * Creates a new actor in this model.
	 * 
	 * @param actorName the name of the actor to be created.
	 * @return the newly created actor
	 * @throws ElementAlreadyInModelException if an actor with the specified name already exists in the model
	 */
	public Actor newActor(String actorName) {
		Actor actor = new Actor(actorName, this);
		saveModelElement(actor, nameToActorMap);
		return actor;
	}

	/**
	 * Creates a new use case in this model.
	 * 
	 * @param useCaseName the name of the use case to be created.
	 * @return the newly created use case
	 * @throws ElementAlreadyInModelException if a use case with the specified name already exists in the model
	 */
	public UseCase newUseCase(String useCaseName) {		
		UseCase useCase = new UseCase(useCaseName, this);
		saveModelElement(useCase, nameToUseCaseMap);
		return useCase;
	}
	
	/**
	 * Finds the actor with the specified name, contained in this model.
	 * 
	 * @param actorName the name of the actor to look for
	 * @return the actor if found, or else an empty optional
	 */
	public Optional<Actor> findActor(String actorName) {
		Optional<Actor> actor = findModelElement(actorName, nameToActorMap);
		return actor;
	}

	/**
	 * Finds the use case with the specified name, contained in this model.
	 * 
	 * @param useCaseName the name of the use case to look for
	 * @return the use case if found, or else an empty optional
	 */
	public Optional<UseCase> findUseCase(String useCaseName) {
		Optional<UseCase> useCase = findModelElement(useCaseName, nameToUseCaseMap);
		return useCase;
	}

	/**
	 * Returns the actors contained in this use case model.
	 * Do not modify that list directly, use {@link #newActor(String)}.
	 * 
	 * @return a collection of the actors
	 */
	public Collection<Actor> getActors() {
		return getModelElements(nameToActorMap);
	}

	/**
	 * Returns the use cases contained in this use case model.
	 * Do not modify that list directly, use {@link #newUseCase(String)}.
	 * 
	 * @return a collection of the use cases
	 */
	public Collection<UseCase> getUseCases() {
		return getModelElements(nameToUseCaseMap);
	}
	
	/**
	 * Returns the use case steps of use cases contained in this use case model.
	 * Do not modify that list directly, use {@link UseCaseFlow#newStep(String)}
	 * and {@link UseCaseStep.SystemPart#newStep(String)}.
	 * 
	 * @return a collection of the use steps
	 */
	public Set<UseCaseStep> getUseCaseSteps() {
		return getUseCases().stream()
			.map(useCase -> useCase.getSteps())
			.flatMap(steps -> steps.stream())
			.collect(Collectors.toSet());
	}
	
	/**
	 * Returns the actor representing the default user.
	 * This actor is implicitly used if you define a use case step
	 * without {@link UseCaseStep#actor(Actor)}, but with a 
	 * {@link UseCaseStep#handle(Class)}.
	 * 
	 * @return the user actor.
	 */
	public Actor getUserActor() {
		return userActor;
	}

	/**
	 * Returns the actor representing the system.
	 * This actor is implicitly used if you define a use case step
	 * without {@link UseCaseStep#actor(Actor)} and without a 
	 * {@link UseCaseStep#handle(Class)}, but just a {@link UseCaseStep#system(Runnable)}.
	 * 
	 * @return the user actor.
	 */
	public Actor getSystemActor() {
		return systemActor;
	}
	
	UseCaseRunner getUseCaseRunner() {
		return useCaseRunner;
	}
}
