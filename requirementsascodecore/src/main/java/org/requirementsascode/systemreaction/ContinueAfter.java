package org.requirementsascode.systemreaction;

import java.util.Optional;

import org.requirementsascode.UseCase;

public class ContinueAfter extends AbstractContinue{
	public ContinueAfter(UseCase useCase, String stepName) {
		super(stepName, Optional.of(useCase.findStep(stepName)));
	}
}