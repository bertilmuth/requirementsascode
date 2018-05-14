package org.requirementsascode.systemreaction;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.ModelRunner;

public abstract class AbstractContinues implements Consumer<ModelRunner> {
    private String stepName;

    public AbstractContinues(String stepName) {
	Objects.requireNonNull(stepName);
	this.stepName = stepName;
    }

    public String getStepName() {
	return stepName;
    }
}
