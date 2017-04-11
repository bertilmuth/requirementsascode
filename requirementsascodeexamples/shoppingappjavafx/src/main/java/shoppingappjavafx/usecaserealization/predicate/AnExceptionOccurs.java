package shoppingappjavafx.usecaserealization.predicate;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;

public class AnExceptionOccurs implements Predicate<UseCaseModelRunner> {
	@Override
	public boolean test(UseCaseModelRunner useCaseModelRunner) {
		return true;
	}
}
