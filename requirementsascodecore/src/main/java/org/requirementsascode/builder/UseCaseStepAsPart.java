package org.requirementsascode.builder;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStepAs;
import org.requirementsascode.UseCaseStepSystem;
import org.requirementsascode.UseCaseStepUser;

public class UseCaseStepAsPart{
	private UseCaseStepAs useCaseStepAs;
	private UseCaseStepPart useCaseStepPart;

	public UseCaseStepAsPart(UseCaseStepAs useCaseStepAs, UseCaseStepPart useCaseStepPart) {
		this.useCaseStepAs = useCaseStepAs;
		this.useCaseStepPart = useCaseStepPart;
	}

	public UseCaseStepSystemPart<UseCaseRunner> system(Consumer<UseCaseRunner> systemReaction) {
		UseCaseStepSystem<UseCaseRunner> useCaseStepSystem = useCaseStepAs.system(systemReaction);
		return new UseCaseStepSystemPart<UseCaseRunner>(useCaseStepSystem, useCaseStepPart);
	} 

	public <T> UseCaseStepUserPart<T> user(Class<T> eventClass) {
		UseCaseStepUser<T> useCaseStepUser = useCaseStepAs.user(eventClass);
		return new UseCaseStepUserPart<>(useCaseStepUser, useCaseStepPart);
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
