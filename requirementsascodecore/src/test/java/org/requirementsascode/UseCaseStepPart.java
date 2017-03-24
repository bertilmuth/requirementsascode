package org.requirementsascode;

public class UseCaseStepPart {
	private UseCaseStep useCaseStep;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCaseStepPart(UseCaseStep useCaseStep, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCaseStep = useCaseStep;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public <T> UseCaseStepUserPart<T> user(Class<T> eventOrExceptionClass) {
		UseCaseStepUser<T> useCaseStepUser = useCaseStep.user(eventOrExceptionClass);
		return new UseCaseStepUserPart<T>(useCaseStepUser, useCaseModelBuilder);
	}

	public <T> UseCaseStepUserPart<T> handle(Class<T> eventOrExceptionClass) {
		UseCaseStepUser<T> useCaseStepUser = useCaseStep.handle(eventOrExceptionClass);
		return new UseCaseStepUserPart<>(useCaseStepUser, useCaseModelBuilder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UseCaseStepSystemPart<?> system(Runnable systemReaction) {
		UseCaseStepSystem<?> useCaseStepSystem = useCaseStep.system(systemReaction);
		return new UseCaseStepSystemPart(useCaseStepSystem, useCaseModelBuilder);
	}

	public UseCaseStepAsPart as(Actor... actors) {
		UseCaseStepAs useCaseStepAs = useCaseStep.as(actors);
		return new UseCaseStepAsPart(useCaseStepAs, useCaseModelBuilder);
	} 
}
