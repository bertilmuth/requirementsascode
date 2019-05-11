package org.requirementsascode;

import java.util.function.Consumer;

public class FlowlessConditionPart {
	private long flowlessStepCounter;
	private StepPart stepPart;
	
	FlowlessConditionPart(Condition optionalCondition, UseCasePart useCasePart, long flowlessStepCounter) {
		UseCase useCase = useCasePart.getUseCase();
		FlowlessStep newStep = useCase.newFlowlessStep(optionalCondition, "S" + flowlessStepCounter);
		this.stepPart = new StepPart(newStep, useCasePart, null);
		this.flowlessStepCounter = flowlessStepCounter;
	}

	public <T> FlowlessUserPart<T> on(Class<T> eventOrExceptionClass) {
		StepUserPart<T> stepUserPart = stepPart.on(eventOrExceptionClass);
		FlowlessUserPart<T> flowlessUserPart = new FlowlessUserPart<>(stepUserPart, flowlessStepCounter);
		return flowlessUserPart;
	}

	public <T> FlowlessSystemPart<ModelRunner> system(Runnable systemReaction) {
		StepSystemPart<ModelRunner> stepSystemPart = stepPart.system(systemReaction);
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = new FlowlessSystemPart<>(stepSystemPart,
				flowlessStepCounter);
		return flowlessSystemPart;
	}

	public <T> FlowlessSystemPart<ModelRunner> system(Consumer<ModelRunner> systemReaction) {
		StepSystemPart<ModelRunner> stepSystemPart = stepPart.system(systemReaction);
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = new FlowlessSystemPart<>(stepSystemPart,
				flowlessStepCounter);
		return flowlessSystemPart;
	}
}
