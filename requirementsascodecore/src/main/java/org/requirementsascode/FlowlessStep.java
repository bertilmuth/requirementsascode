package org.requirementsascode;

import java.util.function.Predicate;

/**
 * A step that is not part of a flow (i.e. no flow definition in the model).
 * 
 * @author b_muth
 *
 */
public class FlowlessStep extends Step {
	private static final long serialVersionUID = -5290327128546502292L;

	FlowlessStep(String stepName, UseCase useCase, Condition optionalCondition) {
		super(stepName, useCase, optionalCondition);
	}

	@Override
	public Predicate<ModelRunner> getPredicate() {
		Predicate<ModelRunner> predicate = toPredicate(getConditionOrElseTrue());
		return predicate;
	}

	private Condition getConditionOrElseTrue() {
		Condition condition = getCondition().orElse(() -> true);
		return condition;
	}
}
