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
	private BuyProductRealization bPR;

	public ShoppingAppModel(BuyProductRealization buyProductRealization){
		this.bPR = buyProductRealization;
	}
	
	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {		
		UseCaseModel useCaseModel = modelBuilder.useCase("Buy product")
			.basicFlow()
				.step("S1").system(startWithEmptyShoppingCart())
				.step("S2").system(displayProducts())
				.step("S3").user(addProductToCart()).system(addProductToPurchaseOrder()).reactWhile(lessThan10Products())
				.step("S4").user(checkoutPurchase())
				.step("S5").system(displayShippingInformationForm())
				.step("S6").user(enterShippingInformation()).system(saveShippingInformation())
				.step("S7").system(displayPaymentDetailsForm())
				.step("S8").user(enterPaymentDetails()).system(savePaymentDetails())
				.step("S9").system(displayPurchaseOrderSummary())
				.step("S10").user(confirmPurchase()).system(initiateShipping())
				.step("S11").continueAt("S1")	
			.flow("Go back from shipping").after("S5")
				.step("S6a_1").user(goBack())
				.step("S6a_2").continueAfter("S1")
			.flow("Go back from payment").after("S7")
				.step("S8a_1").user(goBack())
				.step("S8a_2").continueAfter("S4")
			.flow("Checkout after going back").after("S2").when(atLeastOneProductInCart()).step("S3a_1").continueAfter("S3")
			.flow("Handle exceptions").when(anytime()).step("EX").handle(anyException()).system(informUserAndLogException())
		.build();
		
		return useCaseModel;
	}

	private Predicate<UseCaseModelRunner> anytime() {
		return r -> true;
	}

	private Consumer<UseCaseModelRunner> startWithEmptyShoppingCart() {return bPR.startWithEmptyShoppingCart();}
	private Consumer<UseCaseModelRunner> displayProducts() {return bPR.displayProducts();}
	private Class<AddProductToCart> addProductToCart() {return AddProductToCart.class;} 
	private Consumer<AddProductToCart> addProductToPurchaseOrder() {return bPR.addProductToPurchaseOrder();}
	private Predicate<UseCaseModelRunner> lessThan10Products() {return bPR.lessThan10Products();}
	private Class<CheckoutPurchase> checkoutPurchase() {return CheckoutPurchase.class;} 
	private Consumer<UseCaseModelRunner> displayShippingInformationForm(){return bPR.displayShippingInformationForm();}
	private Class<EnterShippingInformation> enterShippingInformation(){return EnterShippingInformation.class;}
	private Consumer<EnterShippingInformation> saveShippingInformation() {return bPR.saveShippingInformation();}
	private Consumer<UseCaseModelRunner> displayPaymentDetailsForm() {return bPR.displayPaymentDetailsForm();}
	private Class<EnterPaymentDetails> enterPaymentDetails(){return EnterPaymentDetails.class;}
	private Consumer<EnterPaymentDetails> savePaymentDetails() {return bPR.savePaymentDetails();}
	private Consumer<UseCaseModelRunner> displayPurchaseOrderSummary() {return bPR.displayPurchaseOrderSummary();}
	private Class<ConfirmPurchase> confirmPurchase(){return ConfirmPurchase.class;}
	private Consumer<ConfirmPurchase> initiateShipping() {return bPR.initiateShipping();}
	private Class<GoBack> goBack(){return GoBack.class;}
	private Predicate<UseCaseModelRunner> atLeastOneProductInCart() {return bPR.atLeastOneProductInCart();}
	private Class<Throwable> anyException(){return Throwable.class;}
	private Consumer<Throwable> informUserAndLogException() {return bPR.informUserAndLogException();}
}