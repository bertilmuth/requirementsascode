package shoppingappjavafx.usecaserealization;

import shoppingappjavafx.domain.PurchaseOrder;

public class RunContext {
	private PurchaseOrder purchaseOrder;

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

}
