package org.requirementsascode;

import java.util.Objects;
import java.util.UUID;

public abstract class UseCaseModelElement {
	private String name;
	private UseCaseModel useCaseModel;

	public UseCaseModelElement(String name, UseCaseModel useCaseModel) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(useCaseModel);
		
		this.name = name;
		this.useCaseModel = useCaseModel;
	}

	public String getName() {
		return name;
	}

	public UseCaseModel getUseCaseModel() {
		return useCaseModel;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	protected String uniqueStepName(String stepName) {
		return uniqueStepName(stepName, UUID.randomUUID());
	}
	
	protected String uniqueStepName(String stepName, Object uniqueIdPart) {
		return stepName + " (#" + uniqueIdPart.toString() + ")";
	}
}