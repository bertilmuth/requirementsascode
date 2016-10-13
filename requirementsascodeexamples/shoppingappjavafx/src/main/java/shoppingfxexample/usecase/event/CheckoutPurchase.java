package shoppingfxexample.usecase.event;

import shoppingfxexample.domain.PurchaseOrder;

public class CheckoutPurchase {
	
	private PurchaseOrder purchaseOrder;

	public CheckoutPurchase(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

}
