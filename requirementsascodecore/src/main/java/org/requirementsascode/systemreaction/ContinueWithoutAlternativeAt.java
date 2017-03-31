package org.requirementsascode.systemreaction;
import java.util.function.Predicate;

import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep; 

public class ContinueWithoutAlternativeAt extends ContinueAt{
	public ContinueWithoutAlternativeAt(UseCase useCase, String stepName) {
		super(useCase, stepName);
	}
	
	@Override
	public void accept(UseCaseRunner runner) {
		runner.setStepWithoutAlternativePredicate(includeOnly(stepName()));
		super.accept(runner);		
	}
	
	private Predicate<UseCaseStep> includeOnly(String stepName) {
		return step -> stepName.equals(step.name());
	}
}