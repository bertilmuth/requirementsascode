package org.requirementsascode;

import static org.requirementsascode.SystemReaction.continueAfterStep;
import static org.requirementsascode.SystemReaction.continueAtStep;
import static org.requirementsascode.SystemReaction.continueExclusivelyAtStep;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.exception.NoSuchElementInUseCase;


/**
 * The part of the step that contains a reference to the event
 * that is allowed to trigger a system reaction for this step.
 * 
 * @author b_muth
 *
 */
public class UseCaseStepUser<T>{
	private UseCaseStep useCaseStep;
	private Class<T> eventClass;
	
	UseCaseStepUser(UseCaseStep useCaseStep, Class<T> eventClass) {
		this.useCaseStep = useCaseStep;
		this.eventClass = eventClass;
	}
	
	/**
	 * Defines the system reaction. 
	 * The system will react as specified to the current step's events,
	 * via {@link UseCaseRunner#reactTo(Object)}.
	 * 
	 * @param systemReaction the specified system reaction
	 * @return the created system part of this step 
	 */
	public UseCaseStepSystem<T> system(Consumer<T> systemReaction) {
		Objects.requireNonNull(systemReaction);
		
		UseCaseStepSystem<T> newSystemPart = new UseCaseStepSystem<>(useCaseStep, systemReaction);
		useCaseStep.setSystem(newSystemPart);
		return newSystemPart;		
	}
	
	/**
	 * Makes the use case runner continue after the specified step.
	 * 
	 * @param stepName name of the step to continue after, in this use case.
	 * @return the use case this step belongs to, to ease creation of further flows
	 * @throws NoSuchElementInUseCase if no step with the specified stepName is found in the current use case
	 */
	public UseCase continueAfter(String stepName) {
		Objects.requireNonNull(stepName);
		
		system(sr -> continueAfterStep(useCaseStep.useCase(), stepName).run());
		return useCaseStep.useCase();
	}
	
	/**
	 * Makes the use case runner continue at the specified step.
	 * If there are alternatives to the specified step, one may be entered
	 * if its condition is enabled.
	 * 
	 * @param stepName name of the step to continue at, in this use case.
	 * @return the use case this step belongs to, to ease creation of further flows
	 * @throws NoSuchElementInUseCase if no step with the specified stepName is found in the current use case
	 */
	public UseCase continueAt(String stepName) {
		Objects.requireNonNull(stepName);
		
		system(sr -> continueAtStep(useCaseStep.useCase(), stepName).run());
		return useCaseStep.useCase();
	}
	
	/**
	 * Makes the use case runner continue directly at the specified step. No alternative
	 * defined by {@link UseCaseFlow#insteadOf(String)} is entered, even if its predicate 
	 * is true.
	 * 
	 * Note: the runner continues at the step only if its predicate is true, and
	 * the step's actor is right.
	 * 
	 * @param stepName
	 *            name of the step to continue at, in this use case.
	 * @return the use case this step belongs to, to ease creation of further
	 *         flows
	 * @throws NoSuchElementInUseCase
	 *             if no step with the specified stepName is found in the
	 *             current use case
	 */
	public UseCase continueExclusivelyAt(String stepName) {
		Objects.requireNonNull(stepName);

		system(sr -> continueExclusivelyAtStep(useCaseStep.useCase(), stepName).run());
		return useCaseStep.useCase();
	}
	
	/**
	 * Makes the use case runner start from the beginning, when no
	 * flow and step has been run.
	 * 
	 * @see UseCaseRunner#restart()
	 * @return the use case this step belongs to, to ease creation of further flows
	 */
	public UseCase restart() {
		system(sr -> useCaseStep.useCaseModel().useCaseRunner().restart());
		return useCaseStep.useCase();
	}
	
	/**
	 * Returns the class of event or exception objects that the system reacts to in this step.
	 * The system reacts to objects that are instances of the returned class or 
	 * instances of any direct or indirect subclass of the returned class.
	 * 
	 * @return the class of events the system reacts to in this step
	 */
	public Class<T> eventClass() {
		return eventClass;
	}
}
