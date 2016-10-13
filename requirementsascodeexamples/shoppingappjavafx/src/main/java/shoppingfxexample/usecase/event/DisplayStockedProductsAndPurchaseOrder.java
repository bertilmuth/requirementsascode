package shoppingfxexample.usecase.event;

import javafx.collections.ObservableList;
import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.PurchaseOrder;

public class DisplayStockedProductsAndPurchaseOrder {
	private ObservableList<Product> products;
	private PurchaseOrder purchaseOrder;

	public DisplayStockedProductsAndPurchaseOrder(ObservableList<Product> products, PurchaseOrder purchaseOrder) {
		this.products = products;
		this.purchaseOrder = purchaseOrder;
	}

	public ObservableList<Product> getProducts() {
		return products;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

}
