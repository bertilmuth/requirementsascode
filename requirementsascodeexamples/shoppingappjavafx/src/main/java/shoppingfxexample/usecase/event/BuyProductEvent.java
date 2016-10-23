package shoppingfxexample.usecase.event;

import shoppingfxexample.domain.Product;

public class BuyProductEvent {
	private Product product;

	public BuyProductEvent(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}
}
