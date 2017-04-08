package org.requirementsascode.predicate;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;

public interface FlowPosition extends Predicate<UseCaseModelRunner> {
  String getStepName();
}
