package org.requirementsascode;

import static org.requirementsascode.UseCaseStepPredicates.afterStep;
import static org.requirementsascode.UseCaseStepPredicates.isRunnerAtStart;
import static org.requirementsascode.UseCaseStepPredicates.noOtherStepCouldReactThan;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A use case step, as part of a use case. The use case steps define the
 * behavior of the use case.
 * 
 * A use case step is the core class of requirementsascode, providing all the
 * necessary configuration information to the {@link UseCaseRunner} to cause the
 * system to react to events. A use case step has a predicate, which defines the
 * complete condition that needs to be fulfilled to enable the step, given a
 * matching event occurs.
 * 
 * @author b_muth
 *
 */
public class UseCaseStep extends UseCaseModelElement {
	private UseCaseFlow useCaseFlow;
	private Optional<UseCaseStep> previousStepInFlow;
	private Predicate<UseCaseRunner> predicate;

	private UseCaseStepAs as;
	private UseCaseStepUser<?> user;
	private UseCaseStepSystem<?> system;

	/**
	 * Creates a use case step with the specified name that belongs to the
	 * specified use case flow.
	 * 
	 * @param stepName
	 *            the name of the step to be created
	 * @param useCaseFlow
	 *            the use case flow that will contain the new use case
	 * @param previousStepInFlow
	 *            the step created before the step in its flow, or else an empty
	 *            optional if it is the first step in its flow
	 * @param predicate
	 *            the complete predicate of the step, or else an empty optional
	 *            which implicitly means:
	 *            {@link #afterPreviousStepInFlowUnlessInterruptedByAlternativeFlow()}
	 */
	UseCaseStep(String stepName, UseCaseFlow useCaseFlow, Optional<UseCaseStep> previousStepInFlow,
			Optional<Predicate<UseCaseRunner>> predicate) {
		super(stepName, useCaseFlow.useCaseModel());
		Objects.requireNonNull(previousStepInFlow);
		Objects.requireNonNull(predicate);
		this.useCaseFlow = useCaseFlow;
		this.previousStepInFlow = previousStepInFlow;
		this.predicate = 
			predicate.orElse(afterPreviousStepUnlessInterruptedByAlternativeFlow(this));
	}

	/**
	 * Returns the use case flow this step is part of.
	 * 
	 * @return the containing use case flow
	 */
	public UseCaseFlow flow() {
		return useCaseFlow;
	}

	/**
	 * Returns the use case this step is part of.
	 * 
	 * @return the containing use case
	 */
	public UseCase useCase() {
		return flow().useCase();
	}

	/**
	 * Returns the step created before this step in its flow, or else an empty
	 * optional if this step is the first step in its flow.
	 * 
	 * @return the previous step in this step's flow
	 */
	public Optional<UseCaseStep> previousStepInFlow() {
		return previousStepInFlow;
	}

	/**
	 * Returns the as part of this step.
	 * 
	 * @return the as part
	 */
	public UseCaseStepAs as() {
		return as;
	}
	
	/**
	 * Sets the as part of this step.
	 * 
	 * @param as the as part
	 * 
	 */
	public void setAs(UseCaseStepAs as) {
		this.as = as;
	}

	/**
	 * Returns the user part of this step.
	 * 
	 * @return the event part
	 */
	public UseCaseStepUser<?> user() {
		return user;
	}

	/**
	 * Sets the user part of this step.
	 * 
	 * @param user
	 *            the user part
	 * 
	 */
	public void setUser(UseCaseStepUser<?> user) {
		this.user = user;
	}

	/**
	 * Returns the system part of this step.
	 * 
	 * @return the system part
	 */
	public UseCaseStepSystem<?> system() {
		return system;
	}

	/**
	 * Sets the system part of this step.
	 * 
	 * @param system
	 *            the system part
	 *
	 */
	public void setSystem(UseCaseStepSystem<?> system) {
		this.system = system;
	}

	/**
	 * Returns the predicate of this step
	 * 
	 * @return the predicate of this step
	 */
	public Predicate<UseCaseRunner> predicate() {
		return predicate;
	}

	/**
	 * Sets the predicate of this step
	 * 
	 * @param predicate
	 *            the predicate
	 * 
	 */
	void setPredicate(Predicate<UseCaseRunner> predicate) {
		this.predicate = predicate;
	}

	/**
	 * This predicate makes sure that use case steps following the first step in
	 * a flow are executed in sequence, unless the first step of an alternative
	 * flow interrupts the flow.
	 * 
	 * @param previousStepInFlow the previous step in the flow that contains the step
	 * @return the predicate for running steps in sequence
	 */
	private Predicate<UseCaseRunner> afterPreviousStepUnlessInterruptedByAlternativeFlow(UseCaseStep currentStep) {
		Optional<UseCaseStep> previousStepInFlow = currentStep.previousStepInFlow();
		Predicate<UseCaseRunner> afterPreviousStep = previousStepInFlow.map(s -> afterStep(s))
				.orElse(isRunnerAtStart());
		return afterPreviousStep.and(noOtherStepCouldReactThan(this));
	}
}
