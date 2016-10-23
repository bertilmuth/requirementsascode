package shoppingfxexample.usecase.event;

import javafx.collections.ObservableList;
import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.PurchaseOrder;

public class DisplayStockedProductsAndPurchaseOrderEvent {
	private ObservableList<Product> productsInStock;
	private PurchaseOrder purchaseOrder;

	public DisplayStockedProductsAndPurchaseOrderEvent(ObservableList<Product> productsInStock, PurchaseOrder purchaseOrder) {
		this.productsInStock = productsInStock;
		this.purchaseOrder = purchaseOrder;
	}

	public ObservableList<Product> getProductsInStock() {
		return productsInStock;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
}