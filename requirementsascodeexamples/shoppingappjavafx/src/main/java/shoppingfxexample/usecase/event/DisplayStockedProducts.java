package shoppingfxexample.usecase.event;

import javafx.collections.ObservableList;
import shoppingfxexample.domain.Product;

public class DisplayStockedProducts {
	private ObservableList<Product> products;

	public DisplayStockedProducts(ObservableList<Product> products) {
		this.products = products;
	}

	public ObservableList<Product> getProducts() {
		return products;
	}
}
