package shoppingappjavafx.usecaserealization.condition;

import org.requirementsascode.condition.Condition;

import shoppingappjavafx.usecaserealization.RunContext;

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
