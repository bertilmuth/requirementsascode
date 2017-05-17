package org.requirementsascode.predicate;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;

public class Anytime implements Predicate<UseCaseModelRunner> {
  @Override
  public boolean test(UseCaseModelRunner t) {
    return true;
  }}
