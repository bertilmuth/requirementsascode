package shoppingappjavafx.usecaserealization.componentinterface;

import shoppingappjavafx.domain.Products;
import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.domain.ShippingInformation;

public interface Display {
	void displayProductsAndShoppingCartSize(Products products, PurchaseOrder purchaseOrder);
	
	void displayShippingInformationForm(ShippingInformation shippingInformation);
	
	void displayPaymentDetailsForm();
	
	void displayPurchaseOrderSummary(PurchaseOrder purchaseOrder);
}
