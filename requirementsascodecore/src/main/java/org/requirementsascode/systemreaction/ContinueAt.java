package org.requirementsascode.systemreaction;

import java.io.Serializable;

import org.requirementsascode.UseCase;

public class ContinueAt extends AbstractContinue implements Serializable{
  private static final long serialVersionUID = -1087791192033164673L;

  public ContinueAt(UseCase useCase, String stepName) {
		super(stepName, useCase.findStep(stepName).getPreviousStepInFlow());
	}
}
