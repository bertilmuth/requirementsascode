package org.requirementsascode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.requirementsascode.predicate.Anytime;

/**
 * A use case flow defines a sequence of steps that lead the user through the use case.
 *
 * <p>A flow either ends with the user reaching her/his goal, or terminates before, usually because
 * of an exception that occurred.
 *
 * @author b_muth
 */
public class Flow extends UseCaseModelElement {
  private UseCase useCase;
  private Optional<Predicate<UseCaseModelRunner>> optionalFlowPosition;
  private Optional<Predicate<UseCaseModelRunner>> optionalWhen;

  /**
   * Creates a use case flow with the specified name that belongs to the specified use case.
   *
   * @param name the name of the flow to be created
   * @param useCase the use case that will contain the new flow
   */
  Flow(String name, UseCase useCase) {
    super(name, useCase.getUseCaseModel());
    this.useCase = useCase;
    this.optionalFlowPosition = Optional.empty();
    this.optionalWhen = Optional.empty();
  }

  /**
   * Returns the use case this flow is part of.
   *
   * @return the containing use case
   */
  public UseCase getUseCase() {
    return useCase;
  }

  /**
   * Returns the steps contained in this flow. Do not modify the returned collection directly.
   *
   * @return a collection of the steps
   */
  public List<Step> getSteps() {
    List<Step> steps =
        getUseCase()
            .getModifiableSteps()
            .stream()
            .filter(step -> step.getFlow().equals(this))
            .collect(Collectors.toList());
    return Collections.unmodifiableList(steps);
  }

  public void setFlowPosition(Predicate<UseCaseModelRunner> flowPosition) {
    this.optionalFlowPosition = Optional.of(flowPosition);
  }

  public Optional<Predicate<UseCaseModelRunner>> getFlowPosition() {
    return optionalFlowPosition;
  }

  public void setWhen(Predicate<UseCaseModelRunner> when) {
    this.optionalWhen = Optional.of(when);
  }

  public Optional<Predicate<UseCaseModelRunner>> getWhen() {
    return optionalWhen;
  }
  
  public Optional<Predicate<UseCaseModelRunner>> getFlowPredicate() {
    Optional<Predicate<UseCaseModelRunner>> flowPredicate = Optional.empty();
    
    if (optionalFlowPosition.isPresent() || optionalWhen.isPresent()) {
      Anytime anytime = new Anytime();
      Predicate<UseCaseModelRunner> flowPositionOrElseAnytime = optionalFlowPosition.orElse(anytime);
      Predicate<UseCaseModelRunner> whenOrElseAnytime = optionalWhen.orElse(anytime);
      flowPredicate =
          Optional.of(isRunnerInDifferentFlow().and(flowPositionOrElseAnytime).and(whenOrElseAnytime));
    }
    return flowPredicate;
  }
  
  private Predicate<UseCaseModelRunner> isRunnerInDifferentFlow() {
    Predicate<UseCaseModelRunner> isRunnerInDifferentFlow =
        runner ->
            runner.getLatestFlow().map(runnerFlow -> !this.equals(runnerFlow)).orElse(true);
    return isRunnerInDifferentFlow;
  }
}
