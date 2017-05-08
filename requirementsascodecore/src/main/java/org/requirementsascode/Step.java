package org.requirementsascode;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A use case step, as part of a use case. The use case steps define the behavior of the use case.
 *
 * <p>A use case step is the core class of requirementsascode, providing all the necessary
 * configuration information to the {@link UseCaseModelRunner} to cause the system to react to
 * events.
 *
 * @author b_muth
 */
public class Step extends UseCaseModelElement {
  private Flow flow;
  private Optional<Step> previousStepInFlow;
  private Predicate<UseCaseModelRunner> defaultPredicate;
  private Optional<Predicate<UseCaseModelRunner>> optionalPredicate;

  private Actor[] actors;
  private Class<?> userEventClass;
  private Consumer<?> systemReaction;

  /**
   * Creates a use case step with the specified name that belongs to the specified use case flow.
   *
   * @param stepName the name of the step to be created
   * @param useCaseFlow the use case flow that will contain the new use case
   * @param previousStepInFlow the step created before the step in its flow, or else an empty
   *     optional if it is the first step in its flow
   */
  Step(String stepName, Flow useCaseFlow, Optional<Step> previousStepInFlow) {
    super(stepName, useCaseFlow.getUseCaseModel());
    Objects.requireNonNull(previousStepInFlow);

    this.flow = useCaseFlow;
    this.previousStepInFlow = previousStepInFlow;
    this.optionalPredicate = Optional.empty();
  }

  public Optional<Step> getPreviousStepInFlow() {
    return previousStepInFlow;
  }

  public Flow getFlow() {
    return flow;
  }

  public UseCase getUseCase() {
    return getFlow().getUseCase();
  }
  
  void setDefaultPredicate(
      Predicate<UseCaseModelRunner> defaultPredicate) {
    this.defaultPredicate = defaultPredicate;
  }
  
  Predicate<UseCaseModelRunner> getDefaultPredicate() {
    return defaultPredicate;
  }
  
  public boolean hasDefaultPredicate() {
    return getDefaultPredicate().equals(getPredicate());
  }

  public Predicate<UseCaseModelRunner> getPredicate() {
    return optionalPredicate.orElse(getDefaultPredicate());
  }

  void setPredicate(Predicate<UseCaseModelRunner> predicate) {
    this.optionalPredicate = Optional.of(predicate);
  }

  public Actor[] getActors() {
    return actors;
  }

  void setActors(Actor[] actors) {
    this.actors = actors;
  }

  public Class<?> getUserEventClass() {
    return userEventClass;
  }

  void setUserEventClass(Class<?> userEventClass) {
    this.userEventClass = userEventClass;
  }

  public Consumer<?> getSystemReaction() {
    return systemReaction;
  }

  void setSystemReaction(Consumer<?> systemReaction) {
    this.systemReaction = systemReaction;
  }
}
