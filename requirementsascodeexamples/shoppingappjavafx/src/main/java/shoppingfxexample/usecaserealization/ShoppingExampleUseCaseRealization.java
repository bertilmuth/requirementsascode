package shoppingfxexample.usecaserealization;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import javafx.collections.ObservableList;
import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.domain.ShippingInformation;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.gui.ShoppingApplicationDisplay;
import shoppingfxexample.usecase.ShoppingExampleUseCaseModel;
import shoppingfxexample.usecase.event.CheckoutPurchaseEvent;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrderEvent;

public class ShoppingExampleUseCaseRealization extends ShoppingExampleUseCaseModel{
	private Stock stock;
	private ShoppingApplicationDisplay display;
	private PurchaseOrder purchaseOrder;
	private ObservableList<Product> productsInStock;

	public ShoppingExampleUseCaseRealization(UseCaseModel useCaseModel, Stock stock, ShoppingApplicationDisplay display) {
		Objects.requireNonNull(useCaseModel);
		Objects.requireNonNull(stock);
		Objects.requireNonNull(display);
		this.stock = stock;
		this.display = display;
		super.createModel(useCaseModel);
	}

	@Override
	protected Runnable newPurchaseOrder() {
		return () -> purchaseOrder = new PurchaseOrder();
	}
	
	@Override
	protected Runnable findProductsInStock() {
		return () -> productsInStock = stock.findProducts();		
	}
	
	@Override
	protected Supplier<DisplayStockedProductsAndPurchaseOrderEvent> displayStockedProductsAndPurchaseOrderEvent() {
		return () -> new DisplayStockedProductsAndPurchaseOrderEvent(productsInStock, purchaseOrder);
	}
	
	@Override
	protected Consumer<DisplayStockedProductsAndPurchaseOrderEvent> displayStockedProductsAndPurchaseOrder() {
		return display::displayStockedProductsAndPurchaseOrder;
	}
	
	@Override
	protected void addProduct(Product product) {
		purchaseOrder.addProduct(product);
	}

	@Override
	protected Predicate<UseCaseRunner> lessThenTenProductsBoughtSoFar() {
		return r -> purchaseOrder.findProducts().size() < 10;
	}

	@Override
	protected Consumer<CheckoutPurchaseEvent> enterShippingInformation() {
		return display::enterShippingInformation;
	}

	@Override
	protected void setShippingInformation(ShippingInformation shippingInformation) {
		purchaseOrder.setShippingInformation(shippingInformation);
	}

	@Override
	protected void displayPurchaseOrderSummary() {
		display.displayPurchaseOrderSummary(purchaseOrder);
	}
	
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
}
