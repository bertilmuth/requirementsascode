package org.requirementsascode.flowposition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.requirementsascode.ModelRunner;
import org.requirementsascode.UseCase;

public abstract class FlowPosition implements Predicate<ModelRunner> {
  private UseCase useCase;
  private List<AfterSingleStep> afterForEachSingleStep;

  protected abstract boolean isRunnerAtRightPositionFor(ModelRunner modelRunner);

  public FlowPosition(UseCase useCase) {
    this.useCase = useCase;
    this.afterForEachSingleStep = new ArrayList<>();
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
    AfterSingleStep afterSingleStep = new AfterSingleStep(stepName, useCase);
    afterForEachSingleStep.add(afterSingleStep);
    return this;
  }

  public List<AfterSingleStep> getAfterForEachSingleStep() {
    return afterForEachSingleStep;
  }
}
