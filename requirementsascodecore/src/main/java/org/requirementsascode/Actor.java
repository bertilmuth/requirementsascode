package org.requirementsascode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * An actor is a role that a user plays. Actors represent different user groups.
 * They enable to distinguish user rights: only an actor that is connected to a
 * particular step is allowed to cause a system reaction for that step.
 *
 * @author b_muth
 */
public class Actor implements Serializable {
	private static final long serialVersionUID = 2441478758595877661L;

	private String name;
	private ModelRunner modelRunner;
	private Map<UseCase, List<Step>> useCaseToStepMap;

	private Model behavior;

	/**
	 * Creates an actor with the specified name that is part of the specified use
	 * case model.
	 *
	 * @param name  the name of the actor
	 */
	public Actor(String name) {
		this.name = name;
		this.useCaseToStepMap = new HashMap<>();
	}
	
	/**
	 * Returns the name of the actor.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the use cases this actor is associated with.
	 *
	 * <p>
	 * The actor is associated to a use case if it is connected to at least one of
	 * its steps.
	 *
	 * @return the use cases the actor is associated with
	 */
	public Set<UseCase> getUseCases() {
		Set<UseCase> useCases = useCaseToStepMap.keySet();
		return Collections.unmodifiableSet(useCases);
	}

	/**
	 * Returns the steps this actor is connected with, for the specified use case.
	 *
	 * @param useCase the use case to query for steps the actor is connected with
	 * @return the steps the actor is connected with
	 */
	public List<Step> getStepsOf(UseCase useCase) {
		Objects.requireNonNull(useCase);

		List<Step> steps = getModifiableStepsOf(useCase);
		return Collections.unmodifiableList(steps);
	}

	public void connectToStep(Step step) {
		Objects.requireNonNull(step.getUseCase());
		Objects.requireNonNull(step);

		List<Step> steps = getModifiableStepsOf(step.getUseCase());
		steps.add(step);
	}

	private List<Step> getModifiableStepsOf(UseCase useCase) {
		useCaseToStepMap.putIfAbsent(useCase, new ArrayList<>());
		return useCaseToStepMap.get(useCase);
	}
	
	public Actor withBehavior(Model model) {
		this.behavior = Objects.requireNonNull(model);
		this.modelRunner = new ModelRunner().as(this).run(behavior);
		return this;
	}
	
	public Optional<Model> getBehavior() {
		return Optional.ofNullable(behavior);
	}
	
	public Optional<Object> reactTo(Object message) {
		Objects.requireNonNull(message);

		Optional<Object> latestPublishedEvent = getModelRunner().flatMap(runner -> runner.reactTo(message));

		return latestPublishedEvent;
	}
	
	public Optional<ModelRunner> getModelRunner() {
		return Optional.ofNullable(modelRunner);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actor other = (Actor) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}
}
