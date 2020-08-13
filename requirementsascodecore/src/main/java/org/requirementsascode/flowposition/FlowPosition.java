package org.requirementsascode.flowposition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.requirementsascode.ModelRunner;
import org.requirementsascode.UseCase;

public abstract class FlowPosition implements Predicate<ModelRunner> {
  private UseCase useCase;
  private List<After> afterOtherSteps;

  protected abstract boolean isRunnerAtRightPositionFor(ModelRunner modelRunner);

  public FlowPosition(UseCase useCase) {
    this.useCase = useCase;
    this.afterOtherSteps = new ArrayList<>();
  }

  @Override
  public final boolean test(ModelRunner modelRunner) {
    resolveStep();

    boolean isRunnerAtRightPosition = isRunnerAtRightPositionFor(modelRunner);
    return isRunnerAtRightPosition;
  }

  public abstract void resolveStep();

  public final UseCase getUseCase() {
    return useCase;
  }

  public FlowPosition orAfter(String stepName, UseCase useCase) {
    After afterOtherStep = new After(stepName, useCase);
    afterOtherSteps.add(afterOtherStep);
    return this;
  }

  public List<After> getAfterOtherSteps() {
    return afterOtherSteps;
  }
}
