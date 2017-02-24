package shoppingfxexample.usecase.event;

import javafx.collections.ObservableList;
import shoppingfxexample.domain.Product;

public class Products {
	private ObservableList<Product> products;

	public Products(ObservableList<Product> products) {
		this.products = products;
	}

	public ObservableList<Product> get() {
		return products;
	}
}