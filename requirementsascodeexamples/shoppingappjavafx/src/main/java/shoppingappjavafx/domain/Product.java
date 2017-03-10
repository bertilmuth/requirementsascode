package shoppingappjavafx.domain;

import java.math.BigDecimal;

public class Product {
	private String productName;
	private BigDecimal priceEuro;
	
	public Product(String productName, BigDecimal priceEuro) {
		this.productName = productName;
		this.priceEuro = priceEuro;
	}

	public String getProductName() {
		return productName;
	}

	public BigDecimal getPriceEuro() {
		return priceEuro;
	}
	
	@Override
	public String toString() {
		return productName;
	}
}
