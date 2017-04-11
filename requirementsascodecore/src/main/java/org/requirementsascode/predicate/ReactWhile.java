package org.requirementsascode.predicate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public class ReactWhile implements Predicate<UseCaseModelRunner>{
  private Predicate<UseCaseModelRunner> reactWhilePredicate;

	public ReactWhile(Step step, Predicate<UseCaseModelRunner> reactWhileCondition) {
		Objects.requireNonNull(step);
    Objects.requireNonNull(reactWhileCondition);
    createReactWhilePredicate(step, reactWhileCondition);
	}
	
  @Override
  public boolean test(UseCaseModelRunner runner) {
    return reactWhilePredicate.test(runner);
  }
  
  private void createReactWhilePredicate(
      Step step, Predicate<UseCaseModelRunner> reactWhileCondition) {
    Predicate<UseCaseModelRunner> performIfConditionIsTrue = step.getPredicate().and(reactWhileCondition);
    Predicate<UseCaseModelRunner> repeatIfConditionIsTrue =
        new After(Optional.of(step)).and(reactWhileCondition);
    reactWhilePredicate = performIfConditionIsTrue.or(repeatIfConditionIsTrue);
  }
}
