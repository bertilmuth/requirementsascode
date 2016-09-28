package org.requirementsascode;

import java.util.Objects;
import java.util.UUID;

public abstract class UseCaseModelElement {
	private String name;
	private UseCaseModel model;

	public UseCaseModelElement(String name, UseCaseModel model) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(model);
		
		this.name = name;
		this.model = model;
	}

	public String getName() {
		return name;
	}

	public UseCaseModel getModel() {
		return model;
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