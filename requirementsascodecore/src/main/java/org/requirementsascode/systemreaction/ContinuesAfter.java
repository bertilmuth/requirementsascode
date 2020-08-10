package org.requirementsascode.systemreaction;

import org.requirementsascode.FlowStep;
import org.requirementsascode.UseCase;

public class ContinuesAfter extends AbstractContinuesAfter{
	public ContinuesAfter(String stepName, UseCase useCase) {
		super(stepName, (FlowStep) useCase.findStep(stepName));
	}
}