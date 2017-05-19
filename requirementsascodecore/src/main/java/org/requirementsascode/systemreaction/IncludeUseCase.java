package org.requirementsascode.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.Step;
import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseModelRunner;

public class IncludeUseCase implements Consumer<UseCaseModelRunner> {
  private UseCase includedUseCase;
  private Step includeStep;

  public IncludeUseCase(UseCase includedUseCase, Step includeStep) {
    this.includedUseCase = includedUseCase;
    this.includeStep = includeStep;
  }

  @Override
  public void accept(UseCaseModelRunner runner) {
    runner.includeUseCase(includedUseCase, includeStep);
  }

  public UseCase getIncludedUseCase() {
    return includedUseCase;
  }
}
