package shoppingapp.boundary.stubs;

import shoppingapp.boundary.driven_port.Display;
import shoppingapp.boundary.internal.domain.Products;
import shoppingapp.boundary.internal.domain.PurchaseOrder;
import shoppingapp.boundary.internal.domain.ShippingInformation;

public class DisplayStub implements Display {
	@Override
	public void displayProducts(Products products, PurchaseOrder purchaseOrder) {
	}

	@Override
	public void displayShippingInformationForm(ShippingInformation shippingInformation) {
	}

	@Override
	public void displayPaymentDetailsForm() {
	}

	@Override
	public void displayPurchaseOrderSummary(PurchaseOrder purchaseOrder) {
	}
}
