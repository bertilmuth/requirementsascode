package shoppingapp.boundary.driven_port;

import shoppingapp.boundary.internal.domain.Products;
import shoppingapp.boundary.internal.domain.PurchaseOrder;
import shoppingapp.boundary.internal.domain.ShippingInformation;

public interface Display {
	void displayProductsAndShoppingCartSize(Products products, PurchaseOrder purchaseOrder);
	
	void displayShippingInformationForm(ShippingInformation shippingInformation);
	
	void displayPaymentDetailsForm();
	
	void displayPurchaseOrderSummary(PurchaseOrder purchaseOrder);
}
