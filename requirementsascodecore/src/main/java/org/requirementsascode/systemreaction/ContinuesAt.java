package org.requirementsascode.systemreaction;

import java.io.Serializable;

import org.requirementsascode.UseCase;

public class ContinuesAt extends AbstractContinue implements Serializable{
  private static final long serialVersionUID = -1087791192033164673L;

  public ContinuesAt(UseCase useCase, String stepName) {
		super(stepName, useCase.findStep(stepName).getPreviousStepInFlow().orElse(null));
	}
}
