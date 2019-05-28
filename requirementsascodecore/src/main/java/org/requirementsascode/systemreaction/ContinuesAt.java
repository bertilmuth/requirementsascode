package org.requirementsascode.systemreaction;

import java.io.Serializable;

import org.requirementsascode.FlowStep;
import org.requirementsascode.UseCase;

public class ContinuesAt extends AbstractContinuesAfter implements Serializable {
	private static final long serialVersionUID = -1087791192033164673L;

	public ContinuesAt(String stepName, UseCase useCase) {
		super(stepName, ((FlowStep) useCase.findStep(stepName)).getPreviousStepInFlow().orElse(null));
	}
}
