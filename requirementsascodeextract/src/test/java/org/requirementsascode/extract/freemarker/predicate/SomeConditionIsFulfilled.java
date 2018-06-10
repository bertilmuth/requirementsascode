package org.requirementsascode.extract.freemarker.predicate;

import org.requirementsascode.Condition;

public class SomeConditionIsFulfilled implements Condition {
  @Override
  public boolean evaluate() {
    return true;
  }
}
