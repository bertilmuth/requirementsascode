package shoppingfxexample.domain;

import java.math.BigDecimal;

public class Stock extends ProductContainer{
	public Stock() {
		super();
		initStockWithExampleData();
	}
	
	private void initStockWithExampleData() {
		addProduct(new Product("Levi Straight Leg Jeans 36 inch waist green", new BigDecimal(54.95), "LEV-JN-SL-36-GN"));
		addProduct(new Product("Purple Ugg boots in the Bailey Bow style, size 6", new BigDecimal(54.95), "UGG-BB-PUR-06"));
		addProduct(new Product("Coffee Mug Red, model no.1", new BigDecimal(8.99), "CM01-R"));
	}
}
