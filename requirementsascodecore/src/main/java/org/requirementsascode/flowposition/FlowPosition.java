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
  private List<After> orAfterSteps;

	protected abstract boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner);

	public FlowPosition(String stepName, UseCase useCase) {
	  this.stepName = stepName;
	  this.useCase = useCase;
	  this.orAfterSteps = new ArrayList<>();
  }

  @Override
	public final boolean test(ModelRunner modelRunner) {
    if(step == null) {
      resolveStep();
    }
    
		boolean isRunnerAtRightPositionForStepOrAfterAnyMergedStep = isRunnerAtRightPositionFor(step, modelRunner)
			|| afterAnyOtherStep(modelRunner);
		return isRunnerAtRightPositionForStepOrAfterAnyMergedStep;
	}

  public FlowStep resolveStep() {
    FlowStep flowStep = null;
    if(useCase != null && stepName != null) {
      this.step = (FlowStep) useCase.findStep(stepName);
    }
    return flowStep;
  }

	private boolean afterAnyOtherStep(ModelRunner modelRunner) {
		boolean isAfterStep = false;
		for (After orAfterStep : orAfterSteps) {
      if(orAfterStep.test(modelRunner)) {
				isAfterStep = true;
				break;
			}
		}
		return isAfterStep;
	}

	public final Step getStep() {
		return step;
	}

	public FlowPosition orAfter(String stepName, UseCase useCase) {
	  After orAfterStep = new After(stepName, useCase);
		orAfterSteps.add(orAfterStep);
		return this;
	}
}
