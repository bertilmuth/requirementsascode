package org.requirementsascode;
import java.util.function.Predicate; 

class ContinueWithoutAlternativeAt extends ContinueAt{
	ContinueWithoutAlternativeAt(UseCase useCase, String stepName) {
		super(useCase, stepName);
	}
	
	@Override
	public void run() {
		UseCaseRunner useCaseRunner = useCase().useCaseModel().useCaseRunner();
		useCaseRunner.setStepWithoutAlternativePredicate(includeOnly(stepName()));
		super.run();		
	}
	
	private Predicate<UseCaseStep> includeOnly(String stepName) {
		return step -> stepName.equals(step.name());
	}
}