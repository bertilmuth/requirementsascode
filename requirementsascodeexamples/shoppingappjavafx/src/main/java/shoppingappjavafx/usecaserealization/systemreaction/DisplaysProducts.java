package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.ModelRunner;

import shoppingappjavafx.domain.Products;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecaserealization.RunContext;
import shoppingappjavafx.usecaserealization.componentinterface.Display;

public class DisplaysProducts implements Consumer<ModelRunner> {
	private RunContext runContext;
	private Stock stock;
	private Display display;

	public DisplaysProducts(RunContext runContext, Stock stock, Display display) {
		this.runContext = runContext;
		this.stock = stock;
		this.display = display;
	}

	@Override
	public void accept(ModelRunner runner) {
		Products products = new Products(stock.findProducts());
		display.displayProductsAndShoppingCartSize(products, runContext.getPurchaseOrder());
	}

}
