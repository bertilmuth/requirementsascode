package org.requirementsascode.builder;

import static org.requirementsascode.builder.StepAsPart.stepAsPart;

import java.util.Objects;
import java.util.function.Supplier;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.exception.NoSuchElementInModel;

public class StepInCasePart {
  private Condition aCase;
  private StepPart stepPart;

  public StepInCasePart(Condition aCase, StepPart stepPart) {
    this.aCase = aCase;
    this.stepPart = stepPart;
  }
  
  /**
   * Defines which actors (i.e. user groups) can cause the system to react to the
   * message of this step.
   *
   * @param actors the actors that define the user groups
   * @return the created as part of this step
   */
  public StepAsPart as(AbstractActor... actors) {
    Objects.requireNonNull(actors);
    return stepAsPart(actors, stepPart);
  }
  
  /**
   * Defines the type of user command objects that this step accepts. Commands of
   * this type can cause a system reaction.
   *
   * <p>
   * Given that the step's condition is true, and the actor is right, the system
   * reacts to objects that are instances of the specified class or instances of
   * any direct or indirect subclass of the specified class.
   *
   * @param commandClass the class of commands the system reacts to in this step
   * @param <T>          the type of the class
   * @return the created user part of this step
   */
  public <T> StepUserPart<T> user(Class<T> commandClass) {
    Objects.requireNonNull(commandClass);
    AbstractActor defaultActor = stepPart.getUseCasePart().getDefaultActor();
    StepUserPart<T> userPart = as(defaultActor).user(commandClass);
    return userPart;
  }

  /**
   * Defines the type of system event objects or exceptions that this step
   * handles. Events of the specified type can cause a system reaction.
   *
   * <p>
   * Given that the step's condition is true, and the actor is right, the system
   * reacts to objects that are instances of the specified class or instances of
   * any direct or indirect subclass of the specified class.
   *
   * @param eventOrExceptionClass the class of events the system reacts to in this
   *                              step
   * @param <T>                   the type of the class
   * @return the created user part of this step
   */
  public <T> StepUserPart<T> on(Class<T> eventOrExceptionClass) {
    Objects.requireNonNull(eventOrExceptionClass);
    StepUserPart<T> userPart = as(stepPart.getSystemActor()).user(eventOrExceptionClass);
    return userPart;
  }

  /**
   * Defines an "autonomous system reaction", meaning the system will react
   * without needing a message provided via {@link ModelRunner#reactTo(Object)}.
   *
   * @param systemReaction the autonomous system reaction
   * @return the created system part of this step
   */
  public StepSystemPart<ModelRunner> system(Runnable systemReaction) {
    Objects.requireNonNull(systemReaction);
    StepSystemPart<ModelRunner> systemPart = as(stepPart.getSystemActor()).system(systemReaction);
    return systemPart;
  }

  /**
   * Defines an "autonomous system reaction", meaning the system will react
   * without needing a message provided via {@link ModelRunner#reactTo(Object)}.
   * After executing the system reaction, the runner will publish the returned
   * event.
   *
   * @param systemReaction the autonomous system reaction, that returns a single
   *                       event to be published.
   * @return the created system part of this step
   */
  public StepSystemPart<ModelRunner> systemPublish(Supplier<?> systemReaction) {
    Objects.requireNonNull(systemReaction);
    StepSystemPart<ModelRunner> systemPart = as(stepPart.getSystemActor()).systemPublish(systemReaction);
    return systemPart;
  }

  /**
   * Makes the model runner continue after the specified step.
   *
   * @param stepName name of the step to continue after, in this use case.
   * @return the use case part this step belongs to, to ease creation of further
   *         flows
   * @throws NoSuchElementInModel if no step with the specified stepName is found
   *                              in the current use case
   */
  public UseCasePart continuesAfter(String stepName) {
    Objects.requireNonNull(stepName);
    UseCasePart useCasePart = as(stepPart.getSystemActor()).continuesAfter(stepName);
    return useCasePart;
  }

  /**
   * Makes the model runner continue at the specified step. If there are
   * alternative flows starting at the specified step, one may be entered if its
   * condition is enabled.
   *
   * @param stepName name of the step to continue at, in this use case.
   * @return the use case part this step belongs to, to ease creation of further
   *         flows
   * @throws NoSuchElementInModel if no step with the specified stepName is found
   *                              in the current use case
   */
  public UseCasePart continuesAt(String stepName) {
    Objects.requireNonNull(stepName);
    UseCasePart useCasePart = as(stepPart.getSystemActor()).continuesAt(stepName);
    return useCasePart;
  }

  /**
   * Makes the model runner continue at the specified step. No alternative flow
   * starting at the specified step is entered, even if its condition is enabled.
   *
   * @param stepName name of the step to continue at, in this use case.
   * @return the use case part this step belongs to, to ease creation of further
   *         flows
   * @throws NoSuchElementInModel if no step with the specified stepName is found
   *                              in the current use case
   */
  public UseCasePart continuesWithoutAlternativeAt(String stepName) {
    Objects.requireNonNull(stepName);
    UseCasePart useCasePart = as(stepPart.getSystemActor()).continuesWithoutAlternativeAt(stepName);
    return useCasePart;
  }
}
