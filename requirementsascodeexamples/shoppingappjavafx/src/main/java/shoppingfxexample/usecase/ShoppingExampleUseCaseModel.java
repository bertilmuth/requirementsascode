package shoppingfxexample.usecase;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import shoppingfxexample.usecase.event.BuyProduct;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.EnterShippingInformation;
import shoppingfxexample.usecase.event.FinishPurchase;

public abstract class ShoppingExampleUseCaseModel{
	private static final Class<FinishPurchase> FINISH_PURCHASE = FinishPurchase.class;
	private static final Class<EnterShippingInformation> ENTER_SHIPPING_INFORMATION = EnterShippingInformation.class;
	private static final Class<BuyProduct> BUY_PRODUCT = BuyProduct.class;
	private static final Class<CheckoutPurchase> CHECKOUT_PURCHASE = CheckoutPurchase.class;
	private static final Class<Throwable> ANY_EXCEPTION = Throwable.class;

	protected ShoppingExampleUseCaseModel() {
	}

	protected void createModel(UseCaseModel useCaseModel) {		
		useCaseModel.useCase("Buy product")
			.basicFlow()
				.step("S1").system(createPurchaseOrder())
				.step("S2").system(findProducts())
				.step("S3").system(displayProducts())
				.step("S4").user(BUY_PRODUCT).system(addProductToPurchaseOrder()).repeatWhile(lessThen10Products())
				.step("S5").user(CHECKOUT_PURCHASE).system(displayShippingInformationForm())
				.step("S6").user(ENTER_SHIPPING_INFORMATION).system(saveShippingInformation())
				.step("S7").system(displayPurchaseOrderSummary())
				.step("S8").user(FINISH_PURCHASE).system(finishPurchase())
				.restart()	
			.flow("Exception Handling").when(anytime())
				.step("EX").handle(ANY_EXCEPTION).system(logException());
	}

	private Predicate<UseCaseRunner> anytime() {
		return r -> true;
	}

	protected abstract Runnable createPurchaseOrder();
	protected abstract Runnable findProducts();
	protected abstract Runnable displayProducts();
	protected abstract Consumer<BuyProduct> addProductToPurchaseOrder();
	protected abstract Predicate<UseCaseRunner> lessThen10Products();
	protected abstract Consumer<CheckoutPurchase> displayShippingInformationForm();
	protected abstract Consumer<EnterShippingInformation> saveShippingInformation();
	protected abstract Runnable displayPurchaseOrderSummary();
	protected abstract Consumer<FinishPurchase> finishPurchase(); 
	protected abstract Consumer<Throwable> logException();
}
