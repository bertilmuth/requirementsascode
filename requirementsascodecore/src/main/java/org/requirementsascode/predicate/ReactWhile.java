package org.requirementsascode.predicate;

import java.util.Objects;
import java.util.function.Predicate;

import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public class ReactWhile implements Predicate<UseCaseModelRunner>{
  private Predicate<UseCaseModelRunner> completeCondition;
  private Predicate<UseCaseModelRunner> reactWhileCondition;

	public ReactWhile(Step step, Predicate<UseCaseModelRunner> reactWhileCondition) {
		Objects.requireNonNull(step);
    Objects.requireNonNull(reactWhileCondition);
    createReactWhileCondition(step, reactWhileCondition);
	}
	
  @Override
  public boolean test(UseCaseModelRunner runner) {
    return completeCondition.test(runner);
  }
  
  private void createReactWhileCondition(
      Step step, Predicate<UseCaseModelRunner> reactWhileCondition) {
    Predicate<UseCaseModelRunner> performIfConditionIsTrue = step.getPredicate().and(reactWhileCondition);
    Predicate<UseCaseModelRunner> repeatIfConditionIsTrue =
        new After(step).and(reactWhileCondition);
    completeCondition = performIfConditionIsTrue.or(repeatIfConditionIsTrue);
    this.reactWhileCondition = reactWhileCondition;
  }

  public Predicate<UseCaseModelRunner> getReactWhileCondition() {
    return reactWhileCondition;
  }
}
