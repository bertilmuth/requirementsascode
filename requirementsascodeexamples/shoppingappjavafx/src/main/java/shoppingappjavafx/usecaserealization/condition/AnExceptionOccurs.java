package shoppingappjavafx.usecaserealization.condition;

import java.util.function.Predicate;

import org.requirementsascode.ModelRunner;

public class AnExceptionOccurs implements Predicate<ModelRunner> {
	@Override
	public boolean test(ModelRunner useCaseModelRunner) {
		return true;
	}
}
