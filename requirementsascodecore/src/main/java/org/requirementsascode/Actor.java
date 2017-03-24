package org.requirementsascode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set; 

/**
 * An actor, as part of a use case model.
 * 
 * An actor is a role that a user plays. Actors represent different user groups.
 * They enable to distinguish user rights: only an actor that is connected to a particular use case step
 * is allowed to cause a system reaction for that step.
 * 
 * @author b_muth
 *
 */
public class Actor extends UseCaseModelElement{
	private Map<UseCase, List<UseCaseStep>> useCaseToStepMap;

	/**
	 * Creates an actor with the specified name that is part
	 * of the specified use case model.
	 * 
	 * @param name the name of the actor
	 * @param useCaseModel the use case model
	 */
	Actor(String name, UseCaseModel useCaseModel) {
		super(name, useCaseModel);
		this.useCaseToStepMap = new HashMap<>();
	}

	/**
	 * Returns the use cases this actor is associated with.
	 * 
	 * The actor is associated to a use case if it is connected
	 * to at least one of its use case steps.
	 * 
	 * Do not modify the returned collection directly.
	 * 
	 * @return the use cases the actor is associated with
	 */
	public Set<UseCase> useCases() {
		return useCaseToStepMap.keySet();
	}

	/**
	 * Returns the use case steps this actor is connected with,
	 * for the specified use case.
	 * 
	 * Do not modify the returned collection directly.
	 * 
	 * @param useCase the use case to query for steps the actor is connected with
	 * @return the use case steps the actor is connected with
	 */
	public List<UseCaseStep> stepsOf(UseCase useCase) {
		Objects.requireNonNull(useCase);

		return useCaseSteps(useCase);
	}
	
	void newStep(UseCaseStep useCaseStep) {
		Objects.requireNonNull(useCaseStep.useCase());
		Objects.requireNonNull(useCaseStep);
		
		List<UseCaseStep> steps = useCaseSteps(useCaseStep.useCase());
		steps.add(useCaseStep);
	}

	private List<UseCaseStep> useCaseSteps(UseCase useCase) {		
		useCaseToStepMap.putIfAbsent(useCase, new ArrayList<>());
		return useCaseToStepMap.get(useCase);
	}
}
