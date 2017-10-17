package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.requirementsascode.predicate.After;
import org.requirementsascode.predicate.Anytime;

/**
 * A use case step, as part of a use case. The use case steps define the behavior of the use case.
 *
 * <p>A use case step is the core class of requirementsascode, providing all the necessary
 * configuration information to the {@link UseCaseModelRunner} to cause the system to react to
 * events.
 *
 * @author b_muth
 */
public class Step extends UseCaseModelElement implements Serializable{
  private static final long serialVersionUID = -2926490717985964131L;
  
  private Flow flow;
  private Step previousStepInFlow;
  private Predicate<UseCaseModelRunner> reactWhile;

  private Actor[] actors;
  private Class<?> userEventClass;
  private Consumer<?> systemReaction;
  private Predicate<UseCaseModelRunner> flowPosition;
  private Predicate<UseCaseModelRunner> when;

  /**
   * Creates a use case step with the specified name that belongs to the specified use case flow.
   *
   * @param stepName the name of the step to be created
   * @param useCaseFlow the use case flow that will contain the new use case
   */
  Step(String stepName, Flow useCaseFlow) {
    super(stepName, useCaseFlow.getUseCaseModel());
    this.flow = useCaseFlow;
  }

  public Optional<Step> getPreviousStepInFlow() {
    return Optional.ofNullable(previousStepInFlow);
  }
  
  void setPreviousStepInFlow(Step previousStepInFlow) {
    this.previousStepInFlow = previousStepInFlow;
  }

  public Flow getFlow() {
    return flow;
  }

  public UseCase getUseCase() {
    return getFlow().getUseCase();
  }

  public Predicate<UseCaseModelRunner> getPredicate() {
    Predicate<UseCaseModelRunner> predicate = getDefaultPredicate();
    
    if(reactWhile != null) {
      predicate = reactWhile;
    } else if(getFlowPredicate().isPresent()) {
      predicate = getFlowPredicate().get();
    }
    
    return predicate;
  }
  
  void setReactWhile(Predicate<UseCaseModelRunner> reactWhile) {
    this.reactWhile = reactWhile;
  }
  
  void setFlowPosition(Predicate<UseCaseModelRunner> flowPosition) {
    this.flowPosition = flowPosition;
  }

  public Optional<Predicate<UseCaseModelRunner>> getFlowPosition() {
    return Optional.ofNullable(flowPosition);
  }

  public void setWhen(Predicate<UseCaseModelRunner> when) {
    this.when = when;
  }

  public Optional<Predicate<UseCaseModelRunner>> getWhen() {
    return Optional.ofNullable(when);
  }
  
  public Optional<Predicate<UseCaseModelRunner>> getFlowPredicate() {
    Optional<Predicate<UseCaseModelRunner>> flowPredicate = Optional.empty();

    if (flowPosition != null || when != null) {
      Anytime anytime = new Anytime();
      Predicate<UseCaseModelRunner> flowPositionOrElseAnytime = flowPosition != null? flowPosition : anytime;
      Predicate<UseCaseModelRunner> whenOrElseAnytime = when != null? when : anytime;
      flowPredicate =
          Optional.of(
              isRunnerInDifferentFlow().and(flowPositionOrElseAnytime).and(whenOrElseAnytime));
    }
    return flowPredicate;
  }
  
  private Predicate<UseCaseModelRunner> isRunnerInDifferentFlow() {
    Predicate<UseCaseModelRunner> isRunnerInDifferentFlow =
        runner ->
            runner.getLatestFlow().map(runnerFlow -> !getFlow().equals(runnerFlow)).orElse(true);
    return isRunnerInDifferentFlow;
  }
  
  Predicate<UseCaseModelRunner> getDefaultPredicate() {
    return new After(previousStepInFlow).and(noStepWithDefinedPredicateInterrupts());
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
    return step -> step.getFlowPredicate().isPresent();
  }

  private Predicate<Step> isOtherStepThan(Step theStep) {
    return step -> !step.equals(theStep);
  }
}
