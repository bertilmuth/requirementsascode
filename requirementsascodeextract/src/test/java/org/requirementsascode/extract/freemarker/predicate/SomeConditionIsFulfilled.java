package org.requirementsascode.extract.freemarker.predicate;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;

public class SomeConditionIsFulfilled implements Predicate<UseCaseModelRunner> {
  @Override
  public boolean test(UseCaseModelRunner runner) {
    return true;
  }
}
