package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set; 

/**
 * An actor, as part of a use case model.
 * 
 * An actor is a role that a user plays. Actors in use case models represent different user groups.
 * They enable to distinguish user rights: only an actor that is connected to a particular use case step
 * is allowed to cause a system reaction for that step.
 * 
 * @author b_muth
 *
 */
public class Actor extends UseCaseModelElement{
	private Map<UseCase, List<UseCaseStep>> useCaseToStepMap;

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
	 * @return the set of use cases the actor is associated with
	 */
	public Set<UseCase> getUseCases() {
		Set<UseCase> useCases = useCaseToStepMap.keySet();
		return Collections.unmodifiableSet(useCases);
	}

	/**
	 * Returns the use case steps this actor is connected with,
	 * for the specified use case.
	 * 
	 * @param useCase the use case to query for steps the actor is connected with
	 * @return the set of use case steps the actor is connected with
	 */
	public List<UseCaseStep> getUseCaseSteps(UseCase useCase) {
		Objects.requireNonNull(useCase);

		List<UseCaseStep> steps = getModifiableUseCaseStepsList(useCase);
		return Collections.unmodifiableList(steps);
	}
	
	void newStep(UseCase namedUseCase, UseCaseStep namedUseCaseStep) {
		Objects.requireNonNull(namedUseCase);
		Objects.requireNonNull(namedUseCaseStep);
		
		List<UseCaseStep> steps = getModifiableUseCaseStepsList(namedUseCase);
		steps.add(namedUseCaseStep);
	}

	private List<UseCaseStep> getModifiableUseCaseStepsList(UseCase useCase) {		
		useCaseToStepMap.putIfAbsent(useCase, new ArrayList<>());
		return useCaseToStepMap.get(useCase);
	}
}
