package shoppingfxexample.usecase.event;

import shoppingfxexample.domain.PurchaseOrder;

public class DisplayPurchaseOrder {
	private PurchaseOrder purchaseOrder;

	public DisplayPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

}