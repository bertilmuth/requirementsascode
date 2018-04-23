package org.requirementsascode.systemreaction;

import java.io.Serializable;
import java.util.function.Consumer;

import org.requirementsascode.FlowStep;
import org.requirementsascode.UseCase;
import org.requirementsascode.ModelRunner;

public class IncludesUseCase implements Consumer<ModelRunner>, Serializable {
  private static final long serialVersionUID = -9078568632090369442L;
  
  private UseCase includedUseCase;
  private FlowStep includeStep;

  public IncludesUseCase(UseCase includedUseCase, FlowStep includeStep) {
    this.includedUseCase = includedUseCase;
    this.includeStep = includeStep;
  }

  @Override
  public void accept(ModelRunner runner) {
    runner.includeUseCase(includedUseCase, includeStep);
  }

  public UseCase getIncludedUseCase() {
    return includedUseCase;
  }
}
