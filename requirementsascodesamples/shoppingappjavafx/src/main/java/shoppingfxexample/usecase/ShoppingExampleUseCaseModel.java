package shoppingfxexample.usecase;

import java.util.function.Predicate;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.gui.ShoppingApplicationDisplay;
import shoppingfxexample.usecase.event.BuyProduct;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrder;
import shoppingfxexample.usecase.event.EnterShippingInformation;

public class ShoppingExampleUseCaseModel{
	private UseCaseModel useCaseModel;
	private Actor endCustomerActor;

	private ShoppingApplicationDisplay display;
	private PurchaseOrder purchaseOrder;

	public ShoppingExampleUseCaseModel(UseCaseModel useCaseModel, ShoppingApplicationDisplay display) {
		store(useCaseModel, display);
		createModel();
	}

	public void store(UseCaseModel useCaseModel, ShoppingApplicationDisplay display) {
		this.useCaseModel = useCaseModel;
		this.display = display;
	}

	private void createModel() {
		endCustomerActor = useCaseModel.newActor("End Customer");

		useCaseModel.newUseCase("Buy product")
			.basicFlow()
				.newStep("System creates new purchase order.")
					.system(() -> purchaseOrder = new PurchaseOrder())
			
				.newStep("System displays stocked products and purchase order.")
					.handle(DisplayStockedProductsAndPurchaseOrder.class)
					.system(display::displayStockedProductsAndPurchaseOrder)
					
				.newStep("End Customer decides to buy product. System adds product to end customer's purchase order. (Maximum 10 products.)")
					.handle(BuyProduct.class)
					.system(buyProduct -> 
						purchaseOrder.addProduct(buyProduct.getProduct()))
					.repeatWhile(lessThenTenProductsBoughtSoFar())
					
				.newStep("End Customer checks out. System prompts End Customer to enter shipping information.")
					.handle(CheckoutPurchase.class)
					.system(display::enterShippingInformation)
					
				.newStep("Customer enters shipping information. System adds shipping information to purchase order.")
					.handle(EnterShippingInformation.class)
					.system(enterShippingInformation -> 
						purchaseOrder.setShippingInformation(enterShippingInformation.getShippingInformation()))
					
				.newStep("System displays purchase order summary.")
					.system(() -> display.displayPurchaseOrderSummary(purchaseOrder))
			
			.newFlow("Exception Handling").when(r -> true)
				.newStep("Handle any exception")
					.handle(Throwable.class).system(t -> t.printStackTrace());
	}

	public Predicate<UseCaseRunner> lessThenTenProductsBoughtSoFar() {
		return r -> purchaseOrder.getProducts().size() < 10;
	}

	public Actor getEndCustomerActor() {
		return endCustomerActor;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
}
