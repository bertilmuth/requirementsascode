package shoppingfxexample.domain;

import java.math.BigDecimal;

public class Stock extends ProductContainer{
	public Stock() {
		super();
		initStockWithExampleData();
	}
	
	private void initStockWithExampleData() {
		addProduct(new Product("Hamster Wheel, Black", new BigDecimal(9.95)));
		addProduct(new Product("Altruistic Style Guide", new BigDecimal(799.95)));
		addProduct(new Product("Plain White T-Shirt", new BigDecimal(10.04)));
	}
}
