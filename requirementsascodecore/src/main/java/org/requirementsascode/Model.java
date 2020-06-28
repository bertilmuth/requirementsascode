package org.requirementsascode;

import static org.requirementsascode.ModelElementContainer.findModelElement;
import static org.requirementsascode.ModelElementContainer.getModelElements;
import static org.requirementsascode.ModelElementContainer.hasModelElement;
import static org.requirementsascode.ModelElementContainer.saveModelElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.requirementsascode.builder.ModelBuilder;
import org.requirementsascode.exception.NoSuchElementInModel;

/**
 * A model is a container for events/commands and the system reactions that
 * handle them.
 * 
 * Either implicitly or explicitly, the model contains use cases and their
 * contained steps. Each step contains the event/command and the system
 * reaction, the condition under which it is executed and the actors it is
 * associated with.
 * 
 * A model is used to configure a {@link ModelRunner}.
 *
 * @author b_muth
 */
public class Model implements Serializable {
	private static final long serialVersionUID = -410733530299609758L;

	private Map<String, UseCase> nameToUseCaseMap;
	private Actor userActor;
	private Actor systemActor;

	private Model() {
		this.nameToUseCaseMap = new LinkedHashMap<>();
		this.userActor = new Actor("User", this);
		this.systemActor = new Actor("System", this);
	}

	/**
	 * The only way to build a model and its parts is to use the builder returned by
	 * this method.
	 * 
	 * @return the builder.
	 */
	public static ModelBuilder builder() {
		return new ModelBuilder(new Model());
	}

	/**
	 * Checks whether this model contains the specified actor.
	 *
	 * @param actorName the name of the actor whose existence to check
	 * @return true if this model contains the specified actor, false otherwise
	 */
	public boolean hasActor(String actorName) {
		Actor specifiedActor = new Actor(actorName,this);
		boolean hasActor = getActors().contains(specifiedActor);
		return hasActor;
	}

	/**
	 * Checks whether this model contains the specified use case.
	 *
	 * @param useCaseName the name of the use case whose existence to check
	 * @return true if this model contains the specified use case, false otherwise
	 */
	public boolean hasUseCase(String useCaseName) {
		boolean hasUseCase = hasModelElement(useCaseName, nameToUseCaseMap);
		return hasUseCase;
	}

	public UseCase newUseCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		UseCase useCase = new UseCase(useCaseName, this);
		saveModelElement(useCase, nameToUseCaseMap);
		return useCase;
	}

	/**
	 * Finds the actor with the specified name, contained in this model.
	 *
	 * @param actorName the name of the actor to look for
	 * @return the actor if found, or else an empty optional
	 * @throws NoSuchElementInModel if no actor with the specified actorName is
	 *                              found in the model
	 */
	public Actor findActor(String actorName) {
		Actor foundActor = getModifiableSteps().stream()
			.flatMap(s -> Arrays.stream(s.getActors()))
			.findFirst()
			.orElseThrow(() -> new NoSuchElementInModel(actorName));
		return foundActor;
	}

	/**
	 * Finds the use case with the specified name, contained in this model.
	 *
	 * @param useCaseName the name of the use case to look for
	 * @return the use case if found, or else an empty optional
	 * @throws NoSuchElementInModel if no use case with the specified useCaseName is
	 *                              found in the model
	 */
	public UseCase findUseCase(String useCaseName) {
		UseCase useCase = findModelElement(useCaseName, nameToUseCaseMap);
		return useCase;
	}

	/**
	 * Returns the actors contained in this model.
	 *
	 * @return the actors
	 */
	public Collection<Actor> getActors() {
		Set<Actor> actors = getModifiableSteps().stream()
			.flatMap(s -> Arrays.stream(s.getActors()))
			.collect(Collectors.toSet());
		return Collections.unmodifiableCollection(actors);
	}

	/**
	 * Returns the use cases contained in this model.
	 *
	 * @return the use cases
	 */
	public Collection<UseCase> getUseCases() {
		Collection<UseCase> modifiableUseCases = getModifiableUseCases();
		return Collections.unmodifiableCollection(modifiableUseCases);
	}

	Collection<UseCase> getModifiableUseCases() {
		return getModelElements(nameToUseCaseMap);
	}

	/**
	 * Returns the steps of use cases contained in this model.
	 *
	 * @return the use steps
	 */
	public Collection<Step> getSteps() {
		Collection<Step> modifiableSteps = getModifiableSteps();
		return Collections.unmodifiableCollection(modifiableSteps);
	}

	Collection<Step> getModifiableSteps() {
		Collection<UseCase> modifiableUseCases = getModifiableUseCases();
		Collection<Step> modifiableSteps = new ArrayList<>();
		
		for (UseCase useCase : modifiableUseCases) {
			Collection<Step> useCaseSteps = useCase.getModifiableSteps();
			modifiableSteps.addAll(useCaseSteps);
		}
		return modifiableSteps;
	}

	/**
	 * Returns the actor representing the default user.
	 *
	 * @return the user actor
	 */
	public Actor getUserActor() {
		return userActor;
	}

	/**
	 * Returns the actor representing the system.
	 *
	 * @return the user actor
	 */
	public Actor getSystemActor() {
		return systemActor;
	}
}
