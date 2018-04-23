package org.requirementsascode.systemreaction;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;

public abstract class AbstractContinue implements Consumer<ModelRunner>{
	private String stepName;
	private FlowStep previousStep;
	
	public AbstractContinue(String stepName, FlowStep previousStep) {
		Objects.requireNonNull(stepName);
		this.stepName = stepName;
		this.previousStep = previousStep;
	}
	
	@Override
	public void accept(ModelRunner runner) {
		runner.setLatestStep(previousStep);
	}
	
	public String getStepName() {
		return stepName;
	}
}
