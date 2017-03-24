package org.requirementsascode;

import java.util.Optional;

public class ContinueAfter extends AbstractContinue{
	ContinueAfter(UseCase useCase, String stepName) {
		super(useCase, stepName, Optional.of(useCase.findStep(stepName)));
	}
}