package shoppingappjavafx.usecase;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import shoppingappjavafx.usecase.event.AddProductToCart;
import shoppingappjavafx.usecase.event.CheckoutPurchase;
import shoppingappjavafx.usecase.event.ConfirmPurchase;
import shoppingappjavafx.usecase.event.EnterPaymentDetails;
import shoppingappjavafx.usecase.event.EnterShippingInformation;
import shoppingappjavafx.usecase.event.GoBack;

public class UseCaseModelCreator{
	private static final Class<AddProductToCart> ADD_PRODUCT_TO_CART = AddProductToCart.class;
	private static final Class<CheckoutPurchase> CHECKOUT_PURCHASE = CheckoutPurchase.class;
	private static final Class<EnterShippingInformation> ENTER_SHIPPING_INFORMATION = EnterShippingInformation.class;
	private static final Class<EnterPaymentDetails> ENTER_PAYMENT_DETAILS = EnterPaymentDetails.class;
	private static final Class<ConfirmPurchase> CONFIRM_PURCHASE = ConfirmPurchase.class;
	private static final Class<GoBack> GO_BACK = GoBack.class;
	private static final Class<Throwable> EXCEPTION = Throwable.class;
	
	private BuyProductRealization buyProductRealization;

	public UseCaseModelCreator(BuyProductRealization buyProductRealization){
		this.buyProductRealization = buyProductRealization;
	}
	
	public void create(UseCaseModel useCaseModel) {		
		useCaseModel.useCase("Buy product")
			.basicFlow()
				.step("S1").system(startWithEmptyShoppingCart())
				.step("S2").system(displayProducts())
				.step("S3").user(ADD_PRODUCT_TO_CART).system(addProductToPurchaseOrder()).reactWhile(lessThan10Products())
				.step("S4").user(CHECKOUT_PURCHASE).system(checkoutPurchase())
				.step("S5").system(displayShippingInformationForm())
				.step("S6").user(ENTER_SHIPPING_INFORMATION).system(saveShippingInformation())
				.step("S7").system(displayPaymentDetailsForm())
				.step("S8").user(ENTER_PAYMENT_DETAILS).system(savePaymentDetails())
				.step("S9").system(displayPurchaseOrderSummary())
				.step("S10").user(CONFIRM_PURCHASE).system(initiateShipping())
				.step("S11").continueAt("S1")	
			.flow("Go back from shipping").after("S5").step("S6a_1").user(GO_BACK).continueAfter("S1")
			.flow("Go back from payment").after("S7").step("S8a_1").user(GO_BACK).continueAfter("S4")
			.flow("Checkout after going back").after("S2").when(atLeastOneProductInCart()).step("S3a_1").continueAfter("S3")
			.flow("Handle exceptions").when(anytime()).step("EX").handle(EXCEPTION).system(informUserAndLogException());
	}

	private Predicate<UseCaseRunner> anytime() {
		return r -> true;
	}

	protected Runnable startWithEmptyShoppingCart() {return buyProductRealization::startWithEmptyShoppingCart;}
	protected Runnable displayProducts() {return buyProductRealization::displayProducts;}
	protected Consumer<AddProductToCart> addProductToPurchaseOrder() {return buyProductRealization::addProductToPurchaseOrder;}
	protected Predicate<UseCaseRunner> lessThan10Products() {return buyProductRealization::lessThen10Products;}
	protected Consumer<CheckoutPurchase> checkoutPurchase() {return buyProductRealization::checkoutPurchase;}
	protected Runnable displayShippingInformationForm() {return buyProductRealization::displayShippingInformationForm;}
	protected Consumer<EnterShippingInformation> saveShippingInformation() {return buyProductRealization::saveShippingInformation;}
	protected Runnable displayPaymentDetailsForm() {return buyProductRealization::displayPaymentDetailsForm;}
	protected Consumer<EnterPaymentDetails> savePaymentDetails() {return buyProductRealization::savePaymentDetails;}
	protected Runnable displayPurchaseOrderSummary() {return buyProductRealization::displayPurchaseOrderSummary;}
	protected Consumer<ConfirmPurchase> initiateShipping() {return buyProductRealization::initiateShipping;}
	protected Predicate<UseCaseRunner> atLeastOneProductInCart() {return buyProductRealization::atLeastOneProductInCart;}
	protected Consumer<Throwable> informUserAndLogException() {return buyProductRealization::informUserAndLogException;}
}