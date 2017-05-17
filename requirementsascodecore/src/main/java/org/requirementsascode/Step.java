package org.requirementsascode;

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
  private Optional<Step> optionalPreviousStepInFlow;
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
   */
  Step(String stepName, Flow useCaseFlow) {
    super(stepName, useCaseFlow.getUseCaseModel());

    this.flow = useCaseFlow;
    this.reactWhile = Optional.empty();
    this.optionalDefinedPredicate = Optional.empty();
    this.optionalPreviousStepInFlow = Optional.empty();
  }

  public Optional<Step> getPreviousStepInFlow() {
    return optionalPreviousStepInFlow;
  }
  
  void setPreviousStepInFlow(Step previousStepInFlow) {
    this.optionalPreviousStepInFlow = Optional.of(previousStepInFlow);
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
  
  void setReactWhile(Predicate<UseCaseModelRunner> reactWhile) {
    this.reactWhile = Optional.of(reactWhile);
  }
  
  Optional<Predicate<UseCaseModelRunner>> getDefinedPredicate() {
    return optionalDefinedPredicate;
  }
  
  void setDefinedPredicate(Predicate<UseCaseModelRunner> definedPredicate) {
    this.optionalDefinedPredicate = Optional.of(definedPredicate);
  }
  
  Predicate<UseCaseModelRunner> getDefaultPredicate() {
    return new After(optionalPreviousStepInFlow).and(noStepWithDefinedPredicateInterrupts());
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
