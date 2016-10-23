package shoppingfxexample.usecase.event;

import shoppingfxexample.domain.PurchaseOrder;

public class CheckoutPurchaseEvent {
	
	private PurchaseOrder purchaseOrder;

	public CheckoutPurchaseEvent(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

}
