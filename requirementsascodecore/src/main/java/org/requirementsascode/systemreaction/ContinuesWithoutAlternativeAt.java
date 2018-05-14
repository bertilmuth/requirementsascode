package org.requirementsascode.systemreaction;

import java.io.Serializable;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;

public class ContinuesWithoutAlternativeAt extends AbstractContinues implements Serializable {
    private static final long serialVersionUID = -2063519627961799238L;

    public ContinuesWithoutAlternativeAt(String continueAtStepName, FlowStep currentStep) {
	super(continueAtStepName);
	FlowStep continueAtStep = ((FlowStep) currentStep.getUseCase().findStep(continueAtStepName));
	continueAtStep.orAfter(currentStep);
    }

    @Override
    public void accept(ModelRunner runner) {
    }
}