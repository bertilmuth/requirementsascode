package org.requirementsascode;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class EndOfFlowReached implements Predicate<UseCaseModelRunner> {
  private Flow flow;

  public EndOfFlowReached(Flow flow) {
    Objects.requireNonNull(flow);
    
    this.flow = flow;
  }

  @Override
  public boolean test(UseCaseModelRunner runner) {
    Step lastStep = getLastStepOf(flow);
    boolean result = runner.getLatestFlow().isPresent() && runner.getLatestStep().isPresent()
        && flow.equals(runner.getLatestFlow().get()) && lastStep.equals(runner.getLatestStep().get());
    
    return result;
  }

  private Step getLastStepOf(Flow flow) {
    List<Step> stepsOfFlow = flow.getSteps();
    int lastStepIndex = stepsOfFlow.size() - 1;
    Step lastStepOfFlow = stepsOfFlow.get(lastStepIndex);
    return lastStepOfFlow;
  }
}
