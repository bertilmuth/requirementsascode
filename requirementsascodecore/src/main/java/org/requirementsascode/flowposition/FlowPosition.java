package org.requirementsascode.flowposition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.requirementsascode.ModelRunner;
import org.requirementsascode.UseCase;

public abstract class FlowPosition implements Predicate<ModelRunner> {
  private UseCase useCase;
  private List<AfterSingleStep> afters;

  protected abstract boolean isRunnerAtRightPositionFor(ModelRunner modelRunner);

  public FlowPosition(UseCase useCase) {
    this.useCase = useCase;
    this.afters = new ArrayList<>();
  }

  @Override
  public final boolean test(ModelRunner modelRunner) {
    resolveSteps();

    boolean isRunnerAtRightPosition = isRunnerAtRightPositionFor(modelRunner);
    return isRunnerAtRightPosition;
  }

  public abstract void resolveSteps();

  public final UseCase getUseCase() {
    return useCase;
  }

  public FlowPosition orAfter(String stepName, UseCase useCase) {
    AfterSingleStep afterStep = new AfterSingleStep(stepName, useCase);
    afters.add(afterStep);
    return this;
  }

  List<AfterSingleStep> getAfters() {
    return afters;
  }
}
