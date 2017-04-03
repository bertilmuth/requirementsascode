package shoppingappjavafx.usecase;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

import shoppingappjavafx.usecase.event.AddProductToCart;
import shoppingappjavafx.usecase.event.CheckoutPurchase;
import shoppingappjavafx.usecase.event.ConfirmPurchase;
import shoppingappjavafx.usecase.event.EnterPaymentDetails;
import shoppingappjavafx.usecase.event.EnterShippingInformation;
import shoppingappjavafx.usecase.event.GoBack;
import shoppingappjavafx.usecaserealization.BuyProductRealization;

public class ShoppingAppModel{
	private static final Class<AddProductToCart> ADD_PRODUCT_TO_CART = AddProductToCart.class;
	private static final Class<CheckoutPurchase> CHECKOUT_PURCHASE = CheckoutPurchase.class;
	private static final Class<EnterShippingInformation> ENTER_SHIPPING_INFORMATION = EnterShippingInformation.class;
	private static final Class<EnterPaymentDetails> ENTER_PAYMENT_DETAILS = EnterPaymentDetails.class;
	private static final Class<ConfirmPurchase> CONFIRM_PURCHASE = ConfirmPurchase.class;
	private static final Class<GoBack> GO_BACK = GoBack.class;
	private static final Class<Throwable> EXCEPTION = Throwable.class;
	
	private BuyProductRealization buyProductRealization;

	public ShoppingAppModel(BuyProductRealization buyProductRealization){
		this.buyProductRealization = buyProductRealization;
	}
	
	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {		
		UseCaseModel useCaseModel = modelBuilder.useCase("Buy product")
			.basicFlow()
				.step("S1").system(startWithEmptyShoppingCart())
				.step("S2").system(displayProducts())
				.step("S3").user(ADD_PRODUCT_TO_CART).system(addProductToPurchaseOrder()).reactWhile(lessThan10Products())
				.step("S4").user(CHECKOUT_PURCHASE)
				.step("S5").system(displayShippingInformationForm())
				.step("S6").user(ENTER_SHIPPING_INFORMATION).system(saveShippingInformation())
				.step("S7").system(displayPaymentDetailsForm())
				.step("S8").user(ENTER_PAYMENT_DETAILS).system(savePaymentDetails())
				.step("S9").system(displayPurchaseOrderSummary())
				.step("S10").user(CONFIRM_PURCHASE).system(initiateShipping())
				.step("S11").continueAt("S1")	
			.flow("Go back from shipping").after("S5")
				.step("S6a_1").user(GO_BACK)
				.step("S6a_2").continueAfter("S1")
			.flow("Go back from payment").after("S7")
				.step("S8a_1").user(GO_BACK)
				.step("S8a_2").continueAfter("S4")
			.flow("Checkout after going back").after("S2").when(atLeastOneProductInCart()).step("S3a_1").continueAfter("S3")
			.flow("Handle exceptions").when(anytime()).step("EX").handle(EXCEPTION).system(informUserAndLogException())
		.build();
		return useCaseModel;
	}

	private Predicate<UseCaseModelRunner> anytime() {
		return r -> true;
	}

	protected Consumer<UseCaseModelRunner> startWithEmptyShoppingCart() {return buyProductRealization.startWithEmptyShoppingCart();}
	protected Consumer<UseCaseModelRunner> displayProducts() {return buyProductRealization.displayProducts();}
	protected Consumer<AddProductToCart> addProductToPurchaseOrder() {return buyProductRealization.addProductToPurchaseOrder();}
	protected Predicate<UseCaseModelRunner> lessThan10Products() {return buyProductRealization.lessThan10Products();}
	protected Consumer<UseCaseModelRunner> displayShippingInformationForm(){return buyProductRealization.displayShippingInformationForm();}
	protected Consumer<EnterShippingInformation> saveShippingInformation() {return buyProductRealization.saveShippingInformation();}
	protected Consumer<UseCaseModelRunner> displayPaymentDetailsForm() {return buyProductRealization.displayPaymentDetailsForm();}
	protected Consumer<EnterPaymentDetails> savePaymentDetails() {return buyProductRealization.savePaymentDetails();}
	protected Consumer<UseCaseModelRunner> displayPurchaseOrderSummary() {return buyProductRealization.displayPurchaseOrderSummary();}
	protected Consumer<ConfirmPurchase> initiateShipping() {return buyProductRealization.initiateShipping();}
	protected Predicate<UseCaseModelRunner> atLeastOneProductInCart() {return buyProductRealization.atLeastOneProductInCart();}
	protected Consumer<Throwable> informUserAndLogException() {return buyProductRealization.informUserAndLogException();}
}