package shoppingapp.boundary.internal.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class ProductContainer {

	private ObservableList<Product> products;
	
	public ProductContainer() {
		this.products = FXCollections.observableArrayList();
	}

	public void addProduct(Product product) {
		products.add(product);
	}

	public void removeProduct(Product product) {
		products.remove(product);
	}

	public ObservableList<Product> findProducts() {
		return products;
	}
	
	public List<String> getProductSummary() {
		Map<String, Long> productToNumberBoughtMap = products.stream()
			.collect(Collectors.groupingBy(Product::getProductName, Collectors.counting()));
		
		List<String> productSummary = productToNumberBoughtMap.entrySet().stream()
			.map(entry -> entry.getValue() + "x " + entry.getKey())
			.collect(Collectors.toList());
				
		return productSummary;
	}
}