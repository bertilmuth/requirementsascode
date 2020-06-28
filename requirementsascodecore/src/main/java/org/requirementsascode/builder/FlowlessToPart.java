package org.requirementsascode.builder;

import java.util.Objects;

public class FlowlessToPart {
	private UseCasePart useCasePart;
	private StepSystemPart stepSystemPart;
	private long flowlessStepCounter;

	public FlowlessToPart(UseCasePart useCasePart, StepSystemPart stepSystemPart, long flowlessStepCounter) {
		this.useCasePart = Objects.requireNonNull(useCasePart);
		this.stepSystemPart = Objects.requireNonNull(stepSystemPart);
		this.flowlessStepCounter = flowlessStepCounter;
	}
}
