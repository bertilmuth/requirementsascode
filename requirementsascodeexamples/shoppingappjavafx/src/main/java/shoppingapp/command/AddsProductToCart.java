package shoppingapp.command;

import shoppingapp.boundary.internal.domain.Product;

public class AddsProductToCart {
	private Product product;

	public AddsProductToCart(Product product) {
		this.product = product;
	}

	public Product get() {
		return product;
	}
}
