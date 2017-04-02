package org.requirementsascode.predicate;

import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;

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
