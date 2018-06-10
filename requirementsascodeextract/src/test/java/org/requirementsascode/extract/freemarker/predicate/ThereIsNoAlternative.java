package org.requirementsascode.extract.freemarker.predicate;

import org.requirementsascode.condition.Condition;

public class ThereIsNoAlternative implements Condition {
  @Override
  public boolean evaluate() {
    return true;
  }
}
