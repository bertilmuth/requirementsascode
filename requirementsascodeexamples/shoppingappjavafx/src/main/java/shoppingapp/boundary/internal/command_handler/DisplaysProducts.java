package shoppingapp.boundary.internal.command_handler;

import shoppingapp.boundary.RunContext;
import shoppingapp.boundary.driven_port.Display;
import shoppingapp.boundary.internal.domain.ProductContainer;
import shoppingapp.boundary.internal.domain.Products;

public class DisplaysProducts implements Runnable {
	private RunContext runContext;
	private ProductContainer productContainer;
	private Display display;

	public DisplaysProducts(RunContext runContext, ProductContainer productContainer, Display display) {
		this.runContext = runContext;
		this.productContainer = productContainer;
		this.display = display;
	}

	@Override
	public void run() {
		Products products = new Products(productContainer.findProducts());
		display.displayProducts(products, runContext.getPurchaseOrder());
	}
}
