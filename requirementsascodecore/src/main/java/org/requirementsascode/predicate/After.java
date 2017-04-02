package org.requirementsascode.predicate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.Step;

public class After implements Predicate<UseCaseModelRunner>{
	private Optional<Step> previousStep;

	public After(Optional<Step> previousStep) {
		this.previousStep = previousStep;
	}
	
	@Override
	public boolean test(UseCaseModelRunner useCaseRunner) {
		Optional<Step> latestStep = useCaseRunner.latestStep();
		boolean isSystemAtRightStep = 
			Objects.equals(previousStep, latestStep);
		return isSystemAtRightStep;	
	}
}
