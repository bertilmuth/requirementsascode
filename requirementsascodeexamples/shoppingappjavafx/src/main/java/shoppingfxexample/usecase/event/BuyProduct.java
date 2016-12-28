package shoppingfxexample.usecase.event;

import shoppingfxexample.domain.Product;

public class BuyProduct {
	private Product product;

	public BuyProduct(Product product) {
		this.product = product;
	}

	public Product get() {
		return product;
	}
}
