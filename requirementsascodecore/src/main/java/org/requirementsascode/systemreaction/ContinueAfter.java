package org.requirementsascode.systemreaction;

import java.io.Serializable;

import org.requirementsascode.UseCase;

public class ContinueAfter extends AbstractContinue implements Serializable{
  private static final long serialVersionUID = -8065764394122743979L;

  public ContinueAfter(UseCase useCase, String stepName) {
		super(stepName, useCase.findStep(stepName));
	}
}