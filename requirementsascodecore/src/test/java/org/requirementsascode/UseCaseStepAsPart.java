package org.requirementsascode;

public class UseCaseStepAsPart{
	private UseCaseStepAs useCaseStepAs;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCaseStepAsPart(UseCaseStepAs useCaseStepAs, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCaseStepAs = useCaseStepAs;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public UseCaseModelBuilder system(Runnable systemReaction) {
		useCaseStepAs.system(systemReaction);
		return useCaseModelBuilder;
	} 

	public <T> UseCaseStepUserPart<T> user(Class<T> eventClass) {
		UseCaseStepUser<T> useCaseStepUser = useCaseStepAs.user(eventClass);
		return new UseCaseStepUserPart<>(useCaseStepUser, useCaseModelBuilder);
	}
}
