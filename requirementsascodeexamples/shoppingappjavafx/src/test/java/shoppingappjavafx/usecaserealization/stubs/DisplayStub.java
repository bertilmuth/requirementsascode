package shoppingappjavafx.usecaserealization.stubs;

import shoppingapp.boundary.driven_port.Display;
import shoppingapp.boundary.internal.domain.Products;
import shoppingapp.boundary.internal.domain.PurchaseOrder;
import shoppingapp.boundary.internal.domain.ShippingInformation;

public class DisplayStub implements Display {
	public void displayProductsAndShoppingCartSize(Products products, PurchaseOrder purchaseOrder){
	}
	
	public void displayShippingInformationForm(ShippingInformation shippingInformation){
	}
	
	public void displayPaymentDetailsForm(){
	}
	
	public void displayPurchaseOrderSummary(PurchaseOrder purchaseOrder){
	}
}
