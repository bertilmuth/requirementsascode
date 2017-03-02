package shoppingfxexample.usecase;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import shoppingfxexample.usecase.event.AddProductToCart;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.EnterShippingInformation;
import shoppingfxexample.usecase.event.ConfirmPurchase;

public abstract class ShoppingExampleUseCaseModel{
	private static final Class<ConfirmPurchase> CONFIRM_PURCHASE = ConfirmPurchase.class;
	private static final Class<EnterShippingInformation> ENTER_SHIPPING_INFORMATION = EnterShippingInformation.class;
	private static final Class<AddProductToCart> ADD_PRODUCT_TO_CART = AddProductToCart.class;
	private static final Class<CheckoutPurchase> CHECKOUT_PURCHASE = CheckoutPurchase.class;
	private static final Class<Throwable> ANY_EXCEPTION = Throwable.class;

	protected ShoppingExampleUseCaseModel() {
	}

	protected void createModel(UseCaseModel useCaseModel) {		
		useCaseModel.useCase("Buy product")
			.basicFlow()
				.step("S1").system(startWithEmptyShoppingCart())
				.step("S2").system(displayProducts())
				.step("S3").user(ADD_PRODUCT_TO_CART).system(addProductToPurchaseOrder()).repeatWhile(lessThen10Products())
				.step("S4").user(CHECKOUT_PURCHASE).system(displayShippingInformationForm())
				.step("S5").user(ENTER_SHIPPING_INFORMATION).system(saveShippingInformation())
				.step("S6").system(displayPurchaseOrderSummary())
				.step("S7").user(CONFIRM_PURCHASE).system(initiateShipping())
				.restart()	
			.flow("Exception Handling").when(anytime())
				.step("EX").handle(ANY_EXCEPTION).system(logException());
	}

	private Predicate<UseCaseRunner> anytime() {
		return r -> true;
	}

	protected abstract Runnable startWithEmptyShoppingCart();
	protected abstract Runnable displayProducts();
	protected abstract Consumer<AddProductToCart> addProductToPurchaseOrder();
	protected abstract Predicate<UseCaseRunner> lessThen10Products();
	protected abstract Consumer<CheckoutPurchase> displayShippingInformationForm();
	protected abstract Consumer<EnterShippingInformation> saveShippingInformation();
	protected abstract Runnable displayPurchaseOrderSummary();
	protected abstract Consumer<ConfirmPurchase> initiateShipping(); 
	protected abstract Consumer<Throwable> logException();
}
