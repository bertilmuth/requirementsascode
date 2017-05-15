package org.requirementsascode;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.NoSuchElementInModel;
import org.requirementsascode.predicate.After;
import org.requirementsascode.predicate.InsteadOf;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 *
 * @see Flow
 * @author b_muth
 */
public class FlowPart {
  private Flow flow;
  private UseCase useCase;
  private UseCasePart useCasePart;

  FlowPart(Flow flow, UseCasePart useCasePart) {
    this.flow = flow;
    this.useCasePart = useCasePart;
    this.useCase = useCasePart.useCase();
  }

  /**
   * Creates the first step of this flow, with the specified name.
   *
   * @param stepName the name of the step to be created
   * @return the newly created step, to ease creation of further steps
   * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
   */
  public StepPart step(String stepName) {
    Step step =
        useCasePart.useCase().newStep(stepName, flow, Optional.empty());
    step.setDefinedPredicate(flow.getFlowPredicate());
    return new StepPart(step, this);
  }

  /**
   * Starts the flow after the specified step has been run, in this flow's use case. You should use
   * after to handle exceptions that occured in the specified step.
   *
   * @param stepName the name of the step to start the flow after
   * @return this use case flow, to ease creation of the predicate and the first step of the flow
   * @throws NoSuchElementInModel if the specified step is not found in this flow's use case
   */
  public FlowPart after(String stepName) {
    Step step = useCase.findStep(stepName);
    flow.setFlowPosition(new After(Optional.of(step)));
    return this;
  }

  /**
   * Starts the flow as an alternative to the specified step, in this flow's use case.
   *
   * @param stepName the name of the specified step
   * @return this use case flow, to ease creation of the predicate and the first step of the flow
   * @throws NoSuchElementInModel if the specified step is not found in this flow's use case
   */
  public FlowPart insteadOf(String stepName) {
    Step step = useCase.findStep(stepName);
    flow.setFlowPosition(new InsteadOf(step));
    return this;
  }

  /**
   * Constrains the flow's predicate: only if the specified predicate is true as well (beside the
   * step condition), the flow is started.
   *
   * @param whenPredicate the condition that constrains when the flow is started
   * @return this use case flow, to ease creation of the predicate and the first step of the flow
   */
  public FlowPart when(Predicate<UseCaseModelRunner> whenPredicate) {
    Objects.requireNonNull(whenPredicate);

    flow.setWhen(whenPredicate);
    return this;
  }

  Flow getUseCaseFlow() {
    return flow;
  }

  UseCasePart getUseCasePart() {
    return useCasePart;
  }

  UseCaseModelBuilder getUseCaseModelBuilder() {
    return useCasePart.useCaseModelBuilder();
  }
}
