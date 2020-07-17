package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractActor {
	private String name;
	private ModelRunner modelRunner;
	private Map<UseCase, List<Step>> useCaseToStepMap;

	
	/**
	 * Creates an actor with a name equal to the current class' simple name.
	 *
	 */
	public AbstractActor() {
		this.useCaseToStepMap = new HashMap<>();
		this.modelRunner = new ModelRunner();
		setName(getClass().getSimpleName());
	}

	/**
	 * Creates an actor with the specified name that is part of the specified use
	 * case model.
	 *
	 * @param name  the name of the actor
	 */
	public AbstractActor(String name) {
		this.useCaseToStepMap = new HashMap<>();
		this.modelRunner = new ModelRunner();
		setName(name);
	}

	/**
	 * Returns the name of the actor.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
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

	public Optional<Object> reactTo(Object message) {
		Objects.requireNonNull(message);
		
		Model actorBehavior = behavior();
		if(!modelRunner.isRunning() && actorBehavior != null) {
			modelRunner.run(actorBehavior);
		}
	
		Optional<Object> latestPublishedEvent = modelRunner.reactTo(message);
		
		return latestPublishedEvent;
	}

	public Optional<Object> reactTo(Object message, AbstractActor callingActor) {
		Objects.requireNonNull(message);
		Objects.requireNonNull(callingActor);
		
		Model actorBehavior = behavior();
		if(!modelRunner.isRunning() && actorBehavior != null) {
			modelRunner.as(callingActor).run(actorBehavior);
		}
	
		Optional<Object> latestPublishedEvent = modelRunner.reactTo(message);
		
		return latestPublishedEvent;
	}

	public abstract Model behavior();

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
		AbstractActor other = (AbstractActor) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

}