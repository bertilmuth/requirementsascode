package shoppingfxexample.gui;

import org.requirementsascode.UseCaseRunner;

import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.gui.ShoppingApplicationDisplay;
import shoppingfxexample.usecase.event.Products;

public class ShoppingApplicationDisplayDouble extends ShoppingApplicationDisplay {

	public ShoppingApplicationDisplayDouble(UseCaseRunner useCaseRunner) {
		super(useCaseRunner, null);
	}

	public void displayProductsAndShoppingCartSize(Products products, PurchaseOrder purchaseOrder){
	}
	
	public void displayShippingInformationForm(){
	}
	
	public void displayPaymentDetailsForm(){
	}
	
	public void displayPurchaseOrderSummary(PurchaseOrder purchaseOrder){
	}
}
