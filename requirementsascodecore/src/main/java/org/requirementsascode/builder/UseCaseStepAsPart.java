package org.requirementsascode.builder;

import java.util.function.Consumer;

import org.requirementsascode.ContinueAfter;
import org.requirementsascode.ContinueAt;
import org.requirementsascode.ContinueWithoutAlternativeAt;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.UseCaseStepAs;
import org.requirementsascode.UseCaseStepUser;

public class UseCaseStepAsPart{
	private UseCaseStep useCaseStep;
	private UseCaseStepPart useCaseStepPart;

	public UseCaseStepAsPart(UseCaseStepAs useCaseStepAs, UseCaseStepPart useCaseStepPart) {
		this.useCaseStepPart = useCaseStepPart;
		this.useCaseStep = useCaseStepPart.useCaseStep();
	}

	public UseCaseStepSystemPart<UseCaseRunner> system(Consumer<UseCaseRunner> systemReaction) {
		UseCaseStepSystemPart<UseCaseRunner> systemPart = 
			user(UseCaseRunner.class).system(systemReaction);
		return systemPart;
	} 

	public <T> UseCaseStepUserPart<T> user(Class<T> eventClass) {
		UseCaseStepUser<T> user = new UseCaseStepUser<>(useCaseStep, eventClass);
		return new UseCaseStepUserPart<>(user, useCaseStepPart);
	}

	public UseCasePart continueAt(String stepName) {
		system(new ContinueAt(useCaseStep.useCase(), stepName)); 
		return useCaseStepPart.useCasePart();
	}

	public UseCasePart continueAfter(String stepName) {		
		system(new ContinueAfter(useCaseStep.useCase(), stepName));
		return useCaseStepPart.useCasePart();
	}

	public UseCasePart continueWithoutAlternativeAt(String stepName) {
		system(new ContinueWithoutAlternativeAt(useCaseStep.useCase(), stepName));
		return useCaseStepPart.useCasePart();
	}
}
