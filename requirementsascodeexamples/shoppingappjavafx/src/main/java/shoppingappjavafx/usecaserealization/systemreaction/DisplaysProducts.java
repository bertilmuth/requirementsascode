package shoppingappjavafx.usecaserealization.systemreaction;

import shoppingappjavafx.domain.Products;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecaserealization.RunContext;
import shoppingappjavafx.usecaserealization.componentinterface.Display;

public class DisplaysProducts implements Runnable {
    private RunContext runContext;
    private Stock stock;
    private Display display;

    public DisplaysProducts(RunContext runContext, Stock stock, Display display) {
	this.runContext = runContext;
	this.stock = stock;
	this.display = display;
    }

    @Override
    public void run() {
	Products products = new Products(stock.findProducts());
	display.displayProductsAndShoppingCartSize(products, runContext.getPurchaseOrder());
    }

}
