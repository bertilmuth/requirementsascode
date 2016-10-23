package shoppingfxexample.usecase;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import javafx.collections.ObservableList;
import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.gui.ShoppingApplicationDisplay;
import shoppingfxexample.usecase.event.BuyProductEvent;
import shoppingfxexample.usecase.event.CheckoutPurchaseEvent;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrderEvent;
import shoppingfxexample.usecase.event.EnterShippingInformationEvent;
import shoppingfxexample.usecase.event.FinishPurchaseEvent;

public class ShoppingExampleUseCaseModel{
	private UseCaseModel useCaseModel;
	private Stock stock;
	private ShoppingApplicationDisplay display;
	private PurchaseOrder purchaseOrder;
	private ObservableList<Product> productsInStock;

	public ShoppingExampleUseCaseModel(UseCaseModel useCaseModel, Stock stock, ShoppingApplicationDisplay display) {
		store(useCaseModel, stock, display);
		createModel();
	}

	public void store(UseCaseModel useCaseModel, Stock stock, ShoppingApplicationDisplay display) {
		this.useCaseModel = useCaseModel;
		this.stock = stock;
		this.display = display;
	}

	private void createModel() {
		useCaseModel.newUseCase("Buy product")
			.basicFlow()
				.newStep("System creates new purchase order.")
					.system(newPurchaseOrder())
					
				.newStep("System gets products from stock and triggers display of products and purchase order.")
					.system(findProductsInStock()).raise(displayStockedProductsAndPurchaseOrderEvent())
					
				.newStep("System displays stocked products and purchase order.")
					.actor(useCaseModel.getSystemActor())
					.handle(DisplayStockedProductsAndPurchaseOrderEvent.class)
					.system(display::displayStockedProductsAndPurchaseOrder)
					
				.newStep("End Customer decides to buy product. System adds product to end customer's purchase order. (Maximum 10 products.)")
					.handle(BuyProductEvent.class)
					.system(buyProduct -> 
						purchaseOrder.addProduct(buyProduct.getProduct()))
					.repeatWhile(lessThenTenProductsBoughtSoFar())
					
				.newStep("End Customer checks out. System prompts End Customer to enter shipping information.")
					.handle(CheckoutPurchaseEvent.class)
					.system(display::enterShippingInformation)
					
				.newStep("Customer enters shipping information. System adds shipping information to purchase order.")
					.handle(EnterShippingInformationEvent.class)
					.system(enterShippingInformation -> 
						purchaseOrder.setShippingInformation(enterShippingInformation.getShippingInformation()))
					
				.newStep("System displays purchase order summary.")
					.system(() -> display.displayPurchaseOrderSummary(purchaseOrder))
				
				.newStep("System finishes purchase and restart.")
					.handle(FinishPurchaseEvent.class)
					.system(fp -> {})
					.restart()
					
			.newFlow("Exception Handling").when(r -> true)
				.newStep("Handle any exception")
					.handle(Throwable.class).system(t -> t.printStackTrace());
	}

	private Runnable newPurchaseOrder() {
		return () -> purchaseOrder = new PurchaseOrder();
	}
	
	private Runnable findProductsInStock() {
		return () -> productsInStock = stock.findProducts();		
	}
	
	private Supplier<DisplayStockedProductsAndPurchaseOrderEvent> displayStockedProductsAndPurchaseOrderEvent() {
		return () -> new DisplayStockedProductsAndPurchaseOrderEvent(productsInStock, purchaseOrder);
	}

	public Predicate<UseCaseRunner> lessThenTenProductsBoughtSoFar() {
		return r -> purchaseOrder.findProducts().size() < 10;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
}
