package shoppingapp.boundary.internal.rule;

import org.requirementsascode.Condition;

import shoppingapp.boundary.RunContext;

public class LessThan10Products implements Condition {
	private RunContext runContext;
	
	public LessThan10Products(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public boolean evaluate() {
		return runContext.getPurchaseOrder().findProducts().size() < 10;
	}

}
