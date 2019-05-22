package shoppingapp.boundary.internal.rule;

import org.requirementsascode.Condition;

import shoppingapp.boundary.RunContext;
import shoppingapp.boundary.internal.domain.PurchaseOrder;

public class AtLeastOneProductIsInCart implements Condition {
	private RunContext runContext;

	public AtLeastOneProductIsInCart(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public boolean evaluate() {
		PurchaseOrder purchaseOrder = runContext.getPurchaseOrder();
		return purchaseOrder.findProducts().size() > 0;
	}
}
