package org.requirementsascode;

import java.util.Objects;

public class FlowlessSystemPart<T> {
	private long flowlessStepCounter;
	private UseCasePart useCasePart;

	FlowlessSystemPart(StepSystemPart<T> stepSystemPart, long flowlessStepCounter) {
		this.flowlessStepCounter = flowlessStepCounter;
		this.useCasePart = stepSystemPart.getStepPart().getUseCasePart();
	}

	public FlowlessConditionPart condition(Condition condition) {
		FlowlessConditionPart conditionPart = new FlowlessConditionPart(condition, useCasePart, ++flowlessStepCounter);
		return conditionPart;
	}

	public <U> FlowlessUserPart<U> on(Class<U> messageClass) {
		FlowlessUserPart<U> flowlessUserPart = condition(null).on(messageClass);
		return flowlessUserPart;
	}

	public Model build() {
		return useCasePart.build();
	}

	public UseCasePart useCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		UseCasePart newUseCasePart = useCasePart.getModelBuilder().useCase(useCaseName);
		return newUseCasePart;
	}

}