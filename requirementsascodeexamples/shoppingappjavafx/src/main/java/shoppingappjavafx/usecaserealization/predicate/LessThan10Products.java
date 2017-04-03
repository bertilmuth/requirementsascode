package shoppingappjavafx.usecaserealization.predicate;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;

import shoppingappjavafx.usecaserealization.RunContext;

public class LessThan10Products implements Predicate<UseCaseModelRunner> {
	private RunContext runContext;
	
	public LessThan10Products(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public boolean test(UseCaseModelRunner runner) {
		return runContext.getPurchaseOrder().findProducts().size() < 10;
	}

}
