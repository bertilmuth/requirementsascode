package org.requirementsascode.systemreaction;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractContinues<T> implements Consumer<T> {
	private String stepName;

	public AbstractContinues(String stepName) {
		Objects.requireNonNull(stepName);
		this.stepName = stepName;
	}

	public String getStepName() {
		return stepName;
	}
}
