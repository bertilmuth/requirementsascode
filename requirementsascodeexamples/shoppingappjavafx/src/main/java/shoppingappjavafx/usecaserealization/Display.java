package shoppingappjavafx.usecaserealization;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.domain.ShippingInformation;
import shoppingappjavafx.usecase.event.Products;

public interface Display {
	void displayProductsAndShoppingCartSize(Products products, PurchaseOrder purchaseOrder);
	
	void displayShippingInformationForm(ShippingInformation shippingInformation);
	
	void displayPaymentDetailsForm();
	
	void displayPurchaseOrderSummary(PurchaseOrder purchaseOrder);
}
