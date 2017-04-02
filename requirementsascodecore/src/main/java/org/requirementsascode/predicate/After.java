package org.requirementsascode.predicate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.UseCaseStep;

public class After implements Predicate<UseCaseModelRunner>{
	private Optional<UseCaseStep> previousStep;

	public After(Optional<UseCaseStep> previousStep) {
		this.previousStep = previousStep;
	}
	
	@Override
	public boolean test(UseCaseModelRunner useCaseRunner) {
		Optional<UseCaseStep> latestStep = useCaseRunner.latestStep();
		boolean isSystemAtRightStep = 
			Objects.equals(previousStep, latestStep);
		return isSystemAtRightStep;	
	}
}
