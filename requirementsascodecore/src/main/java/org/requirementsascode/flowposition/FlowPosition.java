package org.requirementsascode.flowposition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.Step;
import org.requirementsascode.UseCase;

public abstract class FlowPosition implements Predicate<ModelRunner>{
  private UseCase useCase;
	private FlowStep step;
  private String stepName;
  private List<FlowStep> mergeSteps;

	protected abstract boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner);

	public FlowPosition(String stepName, UseCase useCase) {
	  this.stepName = stepName;
	  this.useCase = useCase;
	  this.mergeSteps = new ArrayList<>();
  }

  @Override
	public final boolean test(ModelRunner modelRunner) {
    if(step == null) {
      this.step = resolveStep();
    }
    
		boolean isRunnerAtRightPositionForStepOrAfterAnyMergedStep = isRunnerAtRightPositionFor(step, modelRunner)
			|| afterAnyMergedStep(modelRunner);
		return isRunnerAtRightPositionForStepOrAfterAnyMergedStep;
	}

  public FlowStep resolveStep() {
    FlowStep flowStep = null;
    if(useCase != null && stepName != null) {
      flowStep = (FlowStep) useCase.findStep(stepName);
    }
    return flowStep;
  }

	private boolean afterAnyMergedStep(ModelRunner modelRunner) {
		boolean isAfterStep = false;
		for (FlowStep mergeStep : mergeSteps) {
			After isAfterMergeStep = new After(mergeStep.getName(), mergeStep.getUseCase());
      if(isAfterMergeStep.test(modelRunner)) {
				isAfterStep = true;
				break;
			}
		}
		return isAfterStep;
	}

	public final Step getStep() {
		return step;
	}

	public FlowPosition orAfter(FlowStep mergeStep) {
		mergeSteps.add(mergeStep);
		return this;
	}
}
