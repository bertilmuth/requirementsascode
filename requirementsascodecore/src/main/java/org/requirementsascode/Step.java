package org.requirementsascode;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.requirementsascode.predicate.After;

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
  private Optional<Predicate<UseCaseModelRunner>> reactWhile;

  private Actor[] actors;
  private Class<?> userEventClass;
  private Consumer<?> systemReaction;
  private After afterStep;
  private Predicate<UseCaseModelRunner> defaultPredicate;

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
    this.reactWhile = Optional.empty();
        
    setAfterStepAsDefault(previousStepInFlow);
  }
  
  void setAfterStepAsDefault(Optional<Step> step) {
    this.afterStep = new After(step);
    this.defaultPredicate = afterStep.and(noStepWithDefinedConditionInterrupts());
  }

  public Optional<Step> getPreviousStepInFlow() {
    return previousStepInFlow;
  }
  
  public boolean isFirstStepInFlow() {
    return !getPreviousStepInFlow().isPresent();
  }

  public Flow getFlow() {
    return flow;
  }

  public UseCase getUseCase() {
    return getFlow().getUseCase();
  }

  private boolean hasDefaultPredicate() {
    return defaultPredicate.equals(getPredicate());
  }

  public Predicate<UseCaseModelRunner> getPredicate() {
    Optional<Predicate<UseCaseModelRunner>> predicate = Optional.empty();

    if (reactWhile.isPresent()) {
      predicate = reactWhile;
    } else if (isFirstStepInFlow()) {
      predicate = getFlow().getFlowPredicate();
    }

    return predicate.orElse(defaultPredicate);
  }

  void setReactWhile(Predicate<UseCaseModelRunner> reactWhile) {
    this.reactWhile = Optional.of(reactWhile);
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
  
  private Predicate<UseCaseModelRunner> noStepWithDefinedConditionInterrupts() {
    return useCaseModelRunner -> {
      Class<?> theStepsEventClass = getUserEventClass();
      UseCaseModel useCaseModel = getUseCaseModel();

      Stream<Step> stepsStream = useCaseModel.getModifiableSteps().stream();
      Stream<Step> stepsWithDefinedConditionsStream =
          stepsStream.filter(hasDefinedCondition().and(isOtherStepThan(this)));

      Set<Step> stepsWithDefinedConditionsThatCanReact =
          useCaseModelRunner.stepsInStreamThatCanReactTo(
              theStepsEventClass, stepsWithDefinedConditionsStream);
      return stepsWithDefinedConditionsThatCanReact.size() == 0;
    };
  }

  private Predicate<Step> hasDefinedCondition() {
    return step -> !step.hasDefaultPredicate();
  }

  private Predicate<Step> isOtherStepThan(Step theStep) {
    return step -> !step.equals(theStep);
  }
}
