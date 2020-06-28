package org.requirementsascode.builder;

import java.util.Objects;

import org.requirementsascode.Actor;
import org.requirementsascode.Model;

public class FlowlessToPart {
	private UseCasePart useCasePart;
	private StepSystemPart<?> stepSystemPart;
	private long flowlessStepCounter;

	FlowlessToPart(UseCasePart useCasePart, StepSystemPart<?> stepSystemPart, Actor recipient, long flowlessStepCounter) {
		this.useCasePart = Objects.requireNonNull(useCasePart);
		this.stepSystemPart = Objects.requireNonNull(stepSystemPart);
		this.flowlessStepCounter = flowlessStepCounter;
		stepSystemPart.to(recipient);
	}

	public Model build() {
		return stepSystemPart.build();
	}
}
