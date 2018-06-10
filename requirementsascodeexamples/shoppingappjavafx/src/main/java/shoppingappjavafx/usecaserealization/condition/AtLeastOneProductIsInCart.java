package shoppingappjavafx.usecaserealization.condition;

import org.requirementsascode.Condition;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecaserealization.RunContext;

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
