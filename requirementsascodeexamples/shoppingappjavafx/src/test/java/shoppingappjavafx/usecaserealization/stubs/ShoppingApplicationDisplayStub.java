package shoppingappjavafx.usecaserealization.stubs;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.domain.ShippingInformation;
import shoppingappjavafx.usecase.event.Products;
import shoppingappjavafx.usecaserealization.IShoppingAppDisplay;

public class ShoppingApplicationDisplayStub implements IShoppingAppDisplay {
	public void displayProductsAndShoppingCartSize(Products products, PurchaseOrder purchaseOrder){
	}
	
	public void displayShippingInformationForm(ShippingInformation shippingInformation){
	}
	
	public void displayPaymentDetailsForm(){
	}
	
	public void displayPurchaseOrderSummary(PurchaseOrder purchaseOrder){
	}
}
