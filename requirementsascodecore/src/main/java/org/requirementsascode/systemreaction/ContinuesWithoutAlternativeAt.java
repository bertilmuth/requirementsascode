package org.requirementsascode.systemreaction;
import java.io.Serializable;
import java.util.function.Predicate;

import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.Step; 

public class ContinuesWithoutAlternativeAt extends ContinuesAt implements Serializable{
  private static final long serialVersionUID = -2063519627961799238L;

  public ContinuesWithoutAlternativeAt(UseCase useCase, String stepName) {
		super(useCase, stepName);
	}
	
	@Override
	public void accept(UseCaseModelRunner runner) {
		runner.setStepWithoutAlternativePredicate(includeOnly(getStepName()));
		super.accept(runner);		
	}
	
	private Predicate<Step> includeOnly(String stepName) {
		return step -> stepName.equals(step.getName());
	}
}