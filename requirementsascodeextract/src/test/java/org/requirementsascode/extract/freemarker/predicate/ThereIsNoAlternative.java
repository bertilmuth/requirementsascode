package org.requirementsascode.extract.freemarker.predicate;

import java.util.function.Predicate;

import org.requirementsascode.ModelRunner;

public class ThereIsNoAlternative implements Predicate<ModelRunner> {
  @Override
  public boolean test(ModelRunner runner) {
    return true;
  }
}
