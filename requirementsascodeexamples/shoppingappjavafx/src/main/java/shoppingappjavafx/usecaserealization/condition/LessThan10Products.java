package shoppingappjavafx.usecaserealization.condition;

import java.util.function.Predicate;

import org.requirementsascode.ModelRunner;

import shoppingappjavafx.usecaserealization.RunContext;

public class LessThan10Products implements Predicate<ModelRunner> {
	private RunContext runContext;
	
	public LessThan10Products(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public boolean test(ModelRunner runner) {
		return runContext.getPurchaseOrder().findProducts().size() < 10;
	}

}
