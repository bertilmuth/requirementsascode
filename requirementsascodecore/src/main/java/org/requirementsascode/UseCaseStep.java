package org.requirementsascode;

import static org.requirementsascode.UseCaseStepPredicate.afterStep;
import static org.requirementsascode.UseCaseStepPredicate.isRunnerAtStart;
import static org.requirementsascode.UseCaseStepPredicate.noOtherStepCouldReactThan;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.exception.NoSuchElementInUseCase;

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
		this.predicate = predicate.orElse(afterPreviousStepInFlowUnlessInterruptedByAlternativeFlow());
	}

	/**
	 * Defines which actors (i.e. user groups) can cause the system to react to
	 * the event of this step.
	 * 
	 * Note: in order for the system to react to one the specified actors,
	 * {@link UseCaseRunner#runAs(Actor...)} needs to be called before
	 * {@link UseCaseRunner#reactTo(Object)}.
	 * 
	 * @param actors
	 *            the actors that defines the user groups
	 * @return the created actor part of this step
	 */
	public UseCaseStepAs as(Actor... actors) {
		Objects.requireNonNull(actors);

		as = new UseCaseStepAs(this, actors);
		return as;
	}

	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * when the step's predicate is true, without needing an event provided via
	 * {@link UseCaseRunner#reactTo(Object)}.
	 * 
	 * As an implicit side effect, the step is connected to the default system
	 * actor (see {@link UseCaseModel#systemActor()}). As another side effect,
	 * the step handles the default system event. Default system events are
	 * raised by the use case runner itself, causing "autonomous system
	 * reactions".
	 * 
	 * @param systemReaction
	 *            the autonomous system reaction
	 * @return the created system part of this step
	 */
	public UseCaseStepSystem<UseCaseRunner> system(Consumer<UseCaseRunner> systemReaction) {
		Objects.requireNonNull(systemReaction);

		Actor systemActor = useCaseModel().systemActor();

		UseCaseStepSystem<UseCaseRunner> systemPart = as(systemActor).system(systemReaction);

		return systemPart;
	}

	/**
	 * Makes the use case runner continue at the specified step. If there are
	 * alternative flows starting at the specified step, one may be entered if
	 * its condition is enabled.
	 * 
	 * Note: the runner continues at the step only if its predicate is true, and
	 * actor is right.
	 * 
	 * @param stepName
	 *            name of the step to continue at, in this use case.
	 * @return the use case this step belongs to, to ease creation of further
	 *         flows
	 * @throws NoSuchElementInUseCase
	 *             if no step with the specified stepName is found in the
	 *             current use case
	 */
	public UseCase continueAt(String stepName) {
		Objects.requireNonNull(stepName);

		system(new ContinueAt(useCase(), stepName));
		return useCase();
	}

	/**
	 * Makes the use case runner continue at the specified step. No alternative
	 * flow starting at the specified step is entered, even if its condition is
	 * enabled.
	 * 
	 * Note: the runner continues at the step only if its predicate is true, and
	 * actor is right.
	 * 
	 * @param stepName
	 *            name of the step to continue at, in this use case.
	 * @return the use case this step belongs to, to ease creation of further
	 *         flows
	 * @throws NoSuchElementInUseCase
	 *             if no step with the specified stepName is found in the
	 *             current use case
	 */
	public UseCase continueWithoutAlternativeAt(String stepName) {
		Objects.requireNonNull(stepName);

		system(new ContinueWithoutAlternativeAt(useCase(), stepName));
		return useCase();
	}

	/**
	 * Makes the use case runner continue after the specified step.
	 * 
	 * @param stepName
	 *            name of the step to continue after, in this use case.
	 * @return the use case this step belongs to, to ease creation of further
	 *         flows
	 * @throws NoSuchElementInUseCase
	 *             if no step with the specified stepName is found in the
	 *             current use case
	 */
	public UseCase continueAfter(String stepName) {
		Objects.requireNonNull(stepName);

		system(new ContinueAfter(useCase(), stepName));
		return useCase();
	}

	/**
	 * Defines the class of user event objects that this step accepts, so that
	 * they can cause a system reaction when the step's predicate is true.
	 * 
	 * Given that the step's predicate is true, the system reacts to objects
	 * that are instances of the specified class or instances of any direct or
	 * indirect subclass of the specified class.
	 * 
	 * As an implicit side effect, the step is connected to the default user
	 * actor (see {@link UseCaseModel#userActor()}).
	 * 
	 * Note: you must provide the event objects at runtime by calling
	 * {@link UseCaseRunner#reactTo(Object)}
	 * 
	 * @param eventClass
	 *            the class of events the system reacts to in this step
	 * @param <T>
	 *            the type of the class
	 * @return the created event part of this step
	 */
	public <T> UseCaseStepUser<T> user(Class<T> eventClass) {
		Objects.requireNonNull(eventClass);

		Actor userActor = useCaseModel().userActor();
		UseCaseStepUser<T> user = as(userActor).user(eventClass);

		return user;
	}

	/**
	 * Defines the class of system event objects or exceptions that this step
	 * accepts, so that they can cause a system reaction when the step's
	 * predicate is true.
	 * 
	 * Given that the step's predicate is true, the system reacts to objects
	 * that are instances of the specified class or instances of any direct or
	 * indirect subclass of the specified class.
	 * 
	 * As an implicit side effect, the step is connected to the default system
	 * actor (see {@link UseCaseModel#systemActor()})..
	 * 
	 * Note: you must provide the event objects at runtime by calling
	 * {@link UseCaseRunner#reactTo(Object)}
	 * 
	 * @param eventOrExceptionClass
	 *            the class of events the system reacts to in this step
	 * @param <T>
	 *            the type of the class
	 * @return the created event part of this step
	 */
	public <T> UseCaseStepUser<T> handle(Class<T> eventOrExceptionClass) {
		Objects.requireNonNull(eventOrExceptionClass);

		Actor systemActor = useCaseModel().systemActor();
		UseCaseStepUser<T> user = as(systemActor).user(eventOrExceptionClass);

		return user;
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
	void setUser(UseCaseStepUser<?> user) {
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
	void setSystem(UseCaseStepSystem<?> system) {
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
	 * @return the predicate for running steps in sequence
	 */
	private Predicate<UseCaseRunner> afterPreviousStepInFlowUnlessInterruptedByAlternativeFlow() {
		Predicate<UseCaseRunner> afterPreviousStep = previousStepInFlow.map(s -> afterStep(s))
				.orElse(isRunnerAtStart());
		return afterPreviousStep.and(noOtherStepCouldReactThan(this));
	}
}
