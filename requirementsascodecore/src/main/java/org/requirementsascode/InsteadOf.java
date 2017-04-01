package org.requirementsascode;

import java.util.Optional;
import java.util.function.Predicate;

public class InsteadOf implements Predicate<UseCaseRunner>{
	private After after;

	public InsteadOf(UseCaseStep step) {
		Optional<UseCaseStep> previousStep = step.previousStepInFlow();
		this.after = new After(previousStep);
	}
	
	@Override
	public boolean test(UseCaseRunner useCaseRunner) {
		return after.test(useCaseRunner);
	}
}
