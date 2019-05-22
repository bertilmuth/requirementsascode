package shoppingapp.boundary.internal.domain;

import javafx.collections.ObservableList;

public class Products {
	private ObservableList<Product> products;

	public Products(ObservableList<Product> products) {
		this.products = products;
	}

	public ObservableList<Product> get() {
		return products;
	}
}