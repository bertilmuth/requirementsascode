package shoppingappjavafx.usecaserealization.condition;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;

public class AnExceptionOccurs implements Predicate<UseCaseModelRunner> {
	@Override
	public boolean test(UseCaseModelRunner useCaseModelRunner) {
		return true;
	}
}
