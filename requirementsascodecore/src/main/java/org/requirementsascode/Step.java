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
 private Optional<Predicate<UseCaseModelRunner>> optionalDefinedPredicate;

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
    this.optionalDefinedPredicate = Optional.empty();
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

  public Predicate<UseCaseModelRunner> getPredicate() {
    Predicate<UseCaseModelRunner> predicate =
        reactWhile.orElse(optionalDefinedPredicate.orElse(getDefaultPredicate()));
    return predicate;
  }
  
  Optional<Predicate<UseCaseModelRunner>> getDefinedPredicate() {
    return optionalDefinedPredicate;
  }
  
  void setDefinedPredicate(Predicate<UseCaseModelRunner> definedPredicate) {
    this.optionalDefinedPredicate = Optional.of(definedPredicate);
  }
  
  Predicate<UseCaseModelRunner> getDefaultPredicate() {
    return new After(previousStepInFlow).and(noStepWithDefinedPredicateInterrupts());
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
  
  private Predicate<UseCaseModelRunner> noStepWithDefinedPredicateInterrupts() {
    return useCaseModelRunner -> {
      Class<?> theStepsEventClass = getUserEventClass();
      UseCaseModel useCaseModel = getUseCaseModel();

      Stream<Step> stepsStream = useCaseModel.getModifiableSteps().stream();
      Stream<Step> stepsWithDefinedPredicatesStream =
          stepsStream.filter(hasDefinedPredicate().and(isOtherStepThan(this)));

      Set<Step> stepsWithDefinedConditionsThatCanReact =
          useCaseModelRunner.stepsInStreamThatCanReactTo(
              theStepsEventClass, stepsWithDefinedPredicatesStream);
      return stepsWithDefinedConditionsThatCanReact.size() == 0;
    };
  }

  private Predicate<Step> hasDefinedPredicate() {
    return step -> step.getDefinedPredicate().isPresent();
  }

  private Predicate<Step> isOtherStepThan(Step theStep) {
    return step -> !step.equals(theStep);
  }
}
