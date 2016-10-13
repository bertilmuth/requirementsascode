package shoppingfxexample.domain;

import java.math.BigDecimal;

public class Product {
	private String productName;
	private BigDecimal priceEuro;
	private String sku;
	
	public Product(String productName, BigDecimal priceEuro, String sku) {
		this.productName = productName;
		this.priceEuro = priceEuro;
		this.sku = sku;
	}

	public String getProductName() {
		return productName;
	}

	public BigDecimal getPriceEuro() {
		return priceEuro;
	}

	public String getSku() {
		return sku;
	}
	
	@Override
	public String toString() {
		return productName;
	}
}
