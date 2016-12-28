package shoppingfxexample.usecase;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.ShippingInformation;
import shoppingfxexample.usecase.event.BuyProduct;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrderEvent;
import shoppingfxexample.usecase.event.EnterShippingInformation;
import shoppingfxexample.usecase.event.FinishPurchase;

public abstract class ShoppingExampleUseCaseModel{
	private static final Class<FinishPurchase> FINISH_PURCHASE = FinishPurchase.class;
	private static final Class<EnterShippingInformation> ENTER_SHIPPING_INFORMATION = EnterShippingInformation.class;
	private static final Class<DisplayStockedProductsAndPurchaseOrderEvent> DISPLAY_STOCKED_PRODUCTS_AND_PURCHASE_ORDER_EVENT 
		= DisplayStockedProductsAndPurchaseOrderEvent.class;
	private static final Class<BuyProduct> BUY_PRODUCT = BuyProduct.class;
	private static final Class<CheckoutPurchase> CHECKOUT_PURCHASE = CheckoutPurchase.class;
	private static final Class<Throwable> ANY_EXCEPTION = Throwable.class;

	private Actor endCustomerActor;

	protected ShoppingExampleUseCaseModel() {
	}

	protected void createModel(UseCaseModel useCaseModel) {
		endCustomerActor = useCaseModel.actor("End Customer");
		
		useCaseModel.useCase("Buy product")
			.basicFlow()
				.step("S1")
					.system(newPurchaseOrder())
					
				.step("S2")
					.system(findProductsInStock()).raise(displayStockedProductsAndPurchaseOrderEvent())
					
				.step("S3")
					.handle(DISPLAY_STOCKED_PRODUCTS_AND_PURCHASE_ORDER_EVENT)
					.system(displayStockedProductsAndPurchaseOrder())
					
				.step("S4")
					.as(endCustomerActor)
					.user(BUY_PRODUCT)
					.system(buyProduct -> addProduct(buyProduct.get()))
					.repeatWhile(lessThenTenProductsBoughtSoFar())
					
				.step("S5")
					.as(endCustomerActor)
					.user(CHECKOUT_PURCHASE)
					.system(enterShippingInformation())
					
				.step("S6")
					.as(endCustomerActor)
					.user(ENTER_SHIPPING_INFORMATION)
					.system(enterShippingInformation -> 
						setShippingInformation(enterShippingInformation.get()))
					
				.step("S7")
					.system(() -> displayPurchaseOrderSummary())
				
				.step("S8")
					.as(endCustomerActor)
					.user(FINISH_PURCHASE)
					.system(fp -> {})
					
				.restart()
					
			.flow("Exception Handling").when(r -> true)
				.step("EX")
					.handle(ANY_EXCEPTION).system(t -> t.printStackTrace());
	}

	protected abstract Runnable newPurchaseOrder();
	protected abstract Runnable findProductsInStock();
	protected abstract Supplier<DisplayStockedProductsAndPurchaseOrderEvent> displayStockedProductsAndPurchaseOrderEvent();
	protected abstract Consumer<DisplayStockedProductsAndPurchaseOrderEvent> displayStockedProductsAndPurchaseOrder();
	protected abstract void addProduct(Product product);
	protected abstract Predicate<UseCaseRunner> lessThenTenProductsBoughtSoFar();
	protected abstract Consumer<CheckoutPurchase> enterShippingInformation();
	protected abstract void setShippingInformation(ShippingInformation shippingInformation);
	protected abstract void displayPurchaseOrderSummary();
	
	public Actor getEndCustomerActor() {
		return endCustomerActor;
	}
}
