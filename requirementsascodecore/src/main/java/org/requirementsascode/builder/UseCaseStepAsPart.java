package org.requirementsascode.builder;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.UseCaseStepAs;
import org.requirementsascode.UseCaseStepUser;

public class UseCaseStepAsPart{
	private UseCaseStepAs useCaseStepAs;
	private UseCaseStepPart useCaseStepPart;

	public UseCaseStepAsPart(UseCaseStepAs useCaseStepAs, UseCaseStepPart useCaseStepPart) {
		this.useCaseStepAs = useCaseStepAs;
		this.useCaseStepPart = useCaseStepPart;
	}

	public UseCaseStepSystemPart<UseCaseRunner> system(Consumer<UseCaseRunner> systemReaction) {
		UseCaseStepSystemPart<UseCaseRunner> systemPart = 
				user(UseCaseRunner.class).system(systemReaction);
		return systemPart;
	} 

	public <T> UseCaseStepUserPart<T> user(Class<T> eventClass) {
		UseCaseStep useCaseStep = useCaseStepPart.useCaseStep();
		UseCaseStepUser<T> user = new UseCaseStepUser<>(useCaseStep, eventClass);
		useCaseStep.setUser(user);
		return new UseCaseStepUserPart<>(user, useCaseStepPart);
	}

	public UseCasePart continueAt(String stepName) {
		useCaseStepAs.continueAt(stepName);
		return useCaseStepPart.useCasePart();
	}

	public UseCasePart continueAfter(String stepName) {
		useCaseStepAs.continueAfter(stepName);
		return useCaseStepPart.useCasePart();
	}

	public UseCasePart continueWithoutAlternativeAt(String stepName) {
		useCaseStepAs.continueWithoutAlternativeAt(stepName);
		return useCaseStepPart.useCasePart();
	}
}
