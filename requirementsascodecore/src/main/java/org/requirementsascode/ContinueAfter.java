package org.requirementsascode;

import java.util.Optional;

public class ContinueAfter extends AbstractContinue{
	public ContinueAfter(UseCase useCase, String stepName) {
		super(stepName, Optional.of(useCase.findStepOrThrow(stepName)));
	}
}