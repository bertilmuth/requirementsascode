package org.requirementsascode;

import java.util.function.Consumer;
import java.util.function.Function;

public class FlowlessUserPart<T> {
	private StepUserPart<T> stepUserPart;
	private long flowlessStepCounter;

	FlowlessUserPart(StepUserPart<T> stepUserPart, long flowlessStepCounter) {
		this.stepUserPart = stepUserPart;
		this.flowlessStepCounter = flowlessStepCounter;
	}

	public FlowlessSystemPart<T> system(Runnable systemReactionObject) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReactionObject);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}

	public FlowlessSystemPart<T> system(Consumer<T> systemReactionObject) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReactionObject);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}

	public FlowlessSystemPart<T> systemPublish(Function<T, Object[]> systemReactionObject) {
		StepSystemPart<T> stepSystemPart = stepUserPart.systemPublish(systemReactionObject);
		return new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
	}
}
