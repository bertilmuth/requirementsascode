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

	public UseCaseFlowPart insteadOf(String stepName) {
		useCaseFlow.insteadOf(stepName);
		return this;
	}

	public UseCaseFlowPart after(String stepName) {
		useCaseFlow.after(stepName);	
		return this;
	}

	public UseCaseFlowPart when(Predicate<UseCaseRunner> whenPredicate) {
		useCaseFlow.when(whenPredicate);
		return this;
	}

}
