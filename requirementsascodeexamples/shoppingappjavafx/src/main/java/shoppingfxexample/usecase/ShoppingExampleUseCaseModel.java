package shoppingfxexample.usecase;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.ShippingInformation;
import shoppingfxexample.usecase.event.BuyProductEvent;
import shoppingfxexample.usecase.event.CheckoutPurchaseEvent;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrderEvent;
import shoppingfxexample.usecase.event.EnterShippingInformationEvent;
import shoppingfxexample.usecase.event.FinishPurchaseEvent;

public abstract class ShoppingExampleUseCaseModel{
	private Actor endCustomerActor;

	protected ShoppingExampleUseCaseModel() {
	}

	protected void createModel(UseCaseModel useCaseModel) {
		endCustomerActor = useCaseModel.newActor("End Customer");
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
					.system(displayStockedProductsAndPurchaseOrder())
					
				.newStep("End Customer decides to buy product. System adds product to end customer's purchase order. (Maximum 10 products.)")
					.actors(endCustomerActor)
					.handle(BuyProductEvent.class)
					.system(buyProductEvent -> addProduct(buyProductEvent.getProduct()))
					.repeatWhile(lessThenTenProductsBoughtSoFar())
					
				.newStep("End Customer checks out. System prompts End Customer to enter shipping information.")
					.actors(endCustomerActor)
					.handle(CheckoutPurchaseEvent.class)
					.system(enterShippingInformation())
					
				.newStep("End Customer enters shipping information. System adds shipping information to purchase order.")
					.actors(endCustomerActor)
					.handle(EnterShippingInformationEvent.class)
					.system(enterShippingInformation -> 
						setShippingInformation(enterShippingInformation.getShippingInformation()))
					
				.newStep("System displays purchase order summary.")
					.system(() -> displayPurchaseOrderSummary())
				
				.newStep("System finishes purchase and restart.")
					.actors(endCustomerActor)
					.handle(FinishPurchaseEvent.class)
					.system(fp -> {})
				.restart()
					
			.newFlow("Exception Handling").when(r -> true)
				.newStep("Handle any exception")
					.handle(Throwable.class).system(t -> t.printStackTrace());
	}

	protected abstract Runnable newPurchaseOrder();
	protected abstract Runnable findProductsInStock();
	protected abstract Supplier<DisplayStockedProductsAndPurchaseOrderEvent> displayStockedProductsAndPurchaseOrderEvent();
	protected abstract Consumer<DisplayStockedProductsAndPurchaseOrderEvent> displayStockedProductsAndPurchaseOrder();
	protected abstract void addProduct(Product product);
	protected abstract Predicate<UseCaseRunner> lessThenTenProductsBoughtSoFar();
	protected abstract Consumer<CheckoutPurchaseEvent> enterShippingInformation();
	protected abstract void setShippingInformation(ShippingInformation shippingInformation);
	protected abstract void displayPurchaseOrderSummary();
	
	public Actor getEndCustomerActor() {
		return endCustomerActor;
	}
}
