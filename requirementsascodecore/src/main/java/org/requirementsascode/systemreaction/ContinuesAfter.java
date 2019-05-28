package org.requirementsascode.systemreaction;

import java.io.Serializable;

import org.requirementsascode.FlowStep;
import org.requirementsascode.UseCase;

public class ContinuesAfter extends AbstractContinuesAfter implements Serializable {
	private static final long serialVersionUID = -8065764394122743979L;

	public ContinuesAfter(String stepName, UseCase useCase) {
		super(stepName, (FlowStep) useCase.findStep(stepName));
	}
}