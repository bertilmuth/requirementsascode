package shoppingappjavafx.usecase.userevent;

import shoppingappjavafx.domain.Product;

public class AddProductToCart {
	private Product product;

	public AddProductToCart(Product product) {
		this.product = product;
	}

	public Product get() {
		return product;
	}
}
