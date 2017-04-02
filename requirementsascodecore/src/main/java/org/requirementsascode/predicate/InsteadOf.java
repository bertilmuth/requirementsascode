package org.requirementsascode.predicate;

import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.UseCaseStep;

public class InsteadOf implements Predicate<UseCaseModelRunner>{
	private After after;

	public InsteadOf(UseCaseStep step) {
		Optional<UseCaseStep> previousStep = step.previousStepInFlow();
		this.after = new After(previousStep);
	}
	
	@Override
	public boolean test(UseCaseModelRunner useCaseRunner) {
		return after.test(useCaseRunner);
	}
}
