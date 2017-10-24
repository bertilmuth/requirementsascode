package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import shoppingappjavafx.usecase.userevent.AddsProductToCart;
import shoppingappjavafx.usecaserealization.RunContext;

public class AddsProductToPurchaseOrder implements Consumer<AddsProductToCart> {
	private RunContext runContext;

	public AddsProductToPurchaseOrder(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public void accept(AddsProductToCart addProductToCart) {
		runContext.getPurchaseOrder().addProduct(addProductToCart.get());		
	}

}
