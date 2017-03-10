package shoppingappjavafx.gui;

import org.requirementsascode.UseCaseRunner;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.gui.ShoppingApplicationDisplay;
import shoppingappjavafx.usecase.event.Products;

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
