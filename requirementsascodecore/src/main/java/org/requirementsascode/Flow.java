package org.requirementsascode;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
  private Predicate<UseCaseModelRunner> flowPositionPredicate;
  private Predicate<UseCaseModelRunner> whenPredicate;

  /**
   * Creates a use case flow with the specified name that belongs to the specified use case.
   *
   * @param name the name of the flow to be created
   * @param useCase the use case that will contain the new flow
   */
  Flow(String name, UseCase useCase) {
    super(name, useCase.getUseCaseModel());
    this.useCase = useCase;
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

  public void setFlowPosition(Predicate<UseCaseModelRunner> flowPositionPredicate) {
    this.flowPositionPredicate = flowPositionPredicate;
  }

  public Predicate<UseCaseModelRunner> getFlowPosition() {
    return flowPositionPredicate;
  }

  public void setWhen(Predicate<UseCaseModelRunner> whenPredicate) {
    this.whenPredicate = whenPredicate;
  }

  public Predicate<UseCaseModelRunner> getWhen() {
    return whenPredicate;
  }
}
