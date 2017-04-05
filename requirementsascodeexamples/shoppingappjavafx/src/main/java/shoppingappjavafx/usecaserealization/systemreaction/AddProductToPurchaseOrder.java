package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import shoppingappjavafx.usecase.userevent.AddProductToCart;
import shoppingappjavafx.usecaserealization.RunContext;

public class AddProductToPurchaseOrder implements Consumer<AddProductToCart> {
	private RunContext runContext;

	public AddProductToPurchaseOrder(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public void accept(AddProductToCart addProductToCart) {
		runContext.getPurchaseOrder().addProduct(addProductToCart.get());		
	}

}
