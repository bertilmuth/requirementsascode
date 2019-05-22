package shoppingapp.boundary.internal.command_handler;

import java.util.function.Consumer;

import shoppingapp.boundary.RunContext;
import shoppingapp.command.AddsProductToCart;

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
