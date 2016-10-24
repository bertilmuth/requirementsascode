package shoppingfxexample.usecase;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.Actor;
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
	private Actor endCustomerActor;

	public ShoppingExampleUseCaseModel(UseCaseModel useCaseModel, Stock stock, ShoppingApplicationDisplay display) {
		store(useCaseModel, stock, display);
		createModel();
	}

	public void store(UseCaseModel useCaseModel, Stock stock, ShoppingApplicationDisplay display) {
		this.useCaseModel = useCaseModel;
		this.stock = stock;
		this.display = display;
		this.endCustomerActor = useCaseModel.newActor("End Customer");
	}

	private void createModel() {
		Actor endCustomer = getEndCustomerActor();
		Actor systemActor = useCaseModel.getSystemActor();
		
		useCaseModel.newUseCase("Buy product")
			.basicFlow()
				.newStep("System creates new purchase order.")
					.system(newPurchaseOrder())
					
				.newStep("System gets products from stock and triggers display of products and purchase order.")
					.system(findProductsInStock()).raise(displayStockedProductsAndPurchaseOrderEvent())
					
				.newStep("System displays stocked products and purchase order.")
					.actors(systemActor)
					.handle(DisplayStockedProductsAndPurchaseOrderEvent.class)
					.system(display::displayStockedProductsAndPurchaseOrder)
					
				.newStep("End Customer decides to buy product. System adds product to end customer's purchase order. (Maximum 10 products.)")
					.actors(endCustomer)
					.handle(BuyProductEvent.class)
					.system(buyProductEvent -> 
						purchaseOrder.addProduct(buyProductEvent.getProduct()))
					.repeatWhile(lessThenTenProductsBoughtSoFar())
					
				.newStep("End Customer checks out. System prompts End Customer to enter shipping information.")
					.actors(endCustomer)
					.handle(CheckoutPurchaseEvent.class)
					.system(display::enterShippingInformation)
					
				.newStep("End Customer enters shipping information. System adds shipping information to purchase order.")
					.actors(endCustomer)
					.handle(EnterShippingInformationEvent.class)
					.system(enterShippingInformation -> 
						purchaseOrder.setShippingInformation(enterShippingInformation.getShippingInformation()))
					
				.newStep("System displays purchase order summary.")
					.system(() -> display.displayPurchaseOrderSummary(purchaseOrder))
				
				.newStep("System finishes purchase and restart.")
					.actors(endCustomer)
					.handle(FinishPurchaseEvent.class)
					.system(fp -> {})
				.restart()
					
			.newFlow("Exception Handling").when(r -> true)
				.newStep("Handle any exception")
					.handle(Throwable.class).system(t -> t.printStackTrace());
	}

	public Actor getEndCustomerActor() {
		return endCustomerActor;
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
