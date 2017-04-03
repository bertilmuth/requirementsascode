package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModelRunner;

import shoppingappjavafx.domain.Products;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecaserealization.RunContext;
import shoppingappjavafx.usecaserealization.componentinterface.Display;

public class DisplayProducts implements Consumer<UseCaseModelRunner> {
	private RunContext runContext;
	private Stock stock;
	private Display display;

	public DisplayProducts(RunContext runContext, Stock stock, Display display) {
		this.runContext = runContext;
		this.stock = stock;
		this.display = display;
	}

	@Override
	public void accept(UseCaseModelRunner runner) {
		Products products = new Products(stock.findProducts());
		display.displayProductsAndShoppingCartSize(products, runContext.getPurchaseOrder());
	}

}
