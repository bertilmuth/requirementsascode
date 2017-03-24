package org.requirementsascode;
import java.util.function.Predicate; 

class ContinueExclusivelyAt extends ContinueAt{
	ContinueExclusivelyAt(UseCase useCase, String stepName) {
		super(useCase, stepName);
	}
	
	@Override
	public void run() {
		UseCaseRunner useCaseRunner = useCase().useCaseModel().useCaseRunner();
		useCaseRunner.setExclusiveStepPredicate(includeOnly(stepName()));
		super.run();		
	}
	
	private Predicate<UseCaseStep> includeOnly(String stepName) {
		return step -> stepName.equals(step.name());
	}
}