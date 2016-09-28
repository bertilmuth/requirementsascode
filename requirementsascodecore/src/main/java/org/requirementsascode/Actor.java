package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set; 

public class Actor extends UseCaseModelElement{
	private Map<UseCase, List<UseCaseStep>> useCaseToStepMap;

	Actor(String name, UseCaseModel useCaseModel) {
		super(name, useCaseModel);
		this.useCaseToStepMap = new HashMap<>();
	}

	public Set<UseCase> getUseCases() {
		Set<UseCase> useCases = useCaseToStepMap.keySet();
		return Collections.unmodifiableSet(useCases);
	}

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
		Objects.requireNonNull(useCase);
		
		useCaseToStepMap.putIfAbsent(useCase, new ArrayList<>());
		return useCaseToStepMap.get(useCase);
	}
}
