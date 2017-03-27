package org.requirementsascode.builder;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseFlow;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;

public class UseCaseFlowPart {
	private UseCaseFlow useCaseFlow;
	private UseCasePart useCasePart;

	public UseCaseFlowPart(UseCaseFlow useCaseFlow, UseCasePart useCasePart) {
		this.useCaseFlow = useCaseFlow;
		this.useCasePart = useCasePart;
	}

	public UseCaseStepPart step(String stepName) {
		UseCaseStep useCaseStep = useCaseFlow.step(stepName);
		return new UseCaseStepPart(useCaseStep, useCasePart);
	}

	public UseCaseFlow insteadOf(String stepName) {
		return useCaseFlow.insteadOf(stepName);
	}

	public UseCaseFlow after(String stepName) {
		return useCaseFlow.after(stepName);		
	}

	public UseCaseFlowPart when(Predicate<UseCaseRunner> whenPredicate) {
		useCaseFlow.when(whenPredicate);
		return this;
	}

}
