package shoppingappjavafx.usecase;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;

import shoppingappjavafx.usecase.userevent.AddProductToCart;
import shoppingappjavafx.usecase.userevent.StartCheckoutProcess;
import shoppingappjavafx.usecase.userevent.ConfirmPurchase;
import shoppingappjavafx.usecase.userevent.EnterPaymentDetails;
import shoppingappjavafx.usecase.userevent.EnterShippingInformation;
import shoppingappjavafx.usecase.userevent.SignalToGoBack;
import shoppingappjavafx.usecaserealization.BuyProductRealization;
import shoppingappjavafx.usecaserealization.predicate.AnExceptionOccurs;
import shoppingappjavafx.usecaserealization.predicate.AtLeastOneProductIsInCart;
import shoppingappjavafx.usecaserealization.predicate.LessThan10Products;
import shoppingappjavafx.usecaserealization.systemreaction.AddProductToPurchaseOrder;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayPaymentDetailsForm;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayProducts;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayPurchaseOrderSummary;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayShippingInformationForm;
import shoppingappjavafx.usecaserealization.systemreaction.InitiateShipping;
import shoppingappjavafx.usecaserealization.systemreaction.LogException;
import shoppingappjavafx.usecaserealization.systemreaction.SavePaymentDetails;
import shoppingappjavafx.usecaserealization.systemreaction.SaveShippingInformation;
import shoppingappjavafx.usecaserealization.systemreaction.StartWithEmptyShoppingCart;

/**
 * Responsible for building the use case model of the shopping app. Its instance is injected with a
 * use case realization that contains the methods to call.
 *
 * @author b_muth
 */
public class ShoppingAppModel {
  /**
   * Predicates
   */
  private LessThan10Products lessThan10Products;
  private AtLeastOneProductIsInCart atLeastOneProductIsInCart;
  private AnExceptionOccurs anExceptionOccurs;

  /**
   * User/system events
   */
  private Class<AddProductToCart> addProductToCart;
  private Class<StartCheckoutProcess> checkOutPurchase;
  private Class<EnterShippingInformation> enterShippingInformation;
  private Class<EnterPaymentDetails> enterPaymentDetails;
  private Class<ConfirmPurchase> confirmPurchase;
  private Class<SignalToGoBack> signalToGoBack;
  private Class<Throwable> anyException;
  
  /**
   * System reactions
   */
  private StartWithEmptyShoppingCart startWithEmptyShoppingCart;
  private DisplayProducts displayProducts;
  private AddProductToPurchaseOrder addProductToPurchaseOrder;
  private DisplayShippingInformationForm displayShippingInformationForm;
  private SaveShippingInformation saveShippingInformation;
  private DisplayPaymentDetailsForm displayPaymentDetailsForm;
  private SavePaymentDetails savePaymentDetails;
  private DisplayPurchaseOrderSummary displayPurchaseOrderSummary;
  private InitiateShipping initiateShipping;
  private LogException logException;

  /**
   * Builds the use case model using the specified builder.
   *
   * @param modelBuilder the builder used
   * @return the created model
   */
  public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		UseCaseModel useCaseModel = modelBuilder.useCase("Buy product")
			.basicFlow()
				.step("S1").system(startWithEmptyShoppingCart)
				.step("S2").system(displayProducts)
				.step("S3").user(addProductToCart).system(addProductToPurchaseOrder).reactWhile(lessThan10Products)
				.step("S4").user(checkOutPurchase)
				.step("S5").system(displayShippingInformationForm)
				.step("S6").user(enterShippingInformation).system(saveShippingInformation)
				.step("S7").system(displayPaymentDetailsForm)
				.step("S8").user(enterPaymentDetails).system(savePaymentDetails)
				.step("S9").system(displayPurchaseOrderSummary)
				.step("S10").user(confirmPurchase).system(initiateShipping)
				.step("S11").continueAt("S1")	
			.flow("Go back from shipping").insteadOf("S6")
				.step("S6a_1").user(signalToGoBack)
				.step("S6a_2").continueAt("S2")
			.flow("Go back from payment").insteadOf("S8")
				.step("S8a_1").user(signalToGoBack)
				.step("S8a_2").continueAt("S5")
			.flow("Checkout after going back").insteadOf("S3").when(atLeastOneProductIsInCart)
				.step("S3a_1").continueAt("S4")
			.flow("Handle exceptions").when(anExceptionOccurs).step("EX").handle(anyException).system(logException)
		.build();
		
		return useCaseModel;
	}
  
  public ShoppingAppModel(BuyProductRealization bPR) {
    setupPredicates(bPR);
    setupEvents();
    setupSystemReactions(bPR);
  }

  private void setupPredicates(BuyProductRealization bPR) {
    this.lessThan10Products = bPR.lessThan10Products();
    this.atLeastOneProductIsInCart = bPR.atLeastOneProductIsInCart();
    this.anExceptionOccurs = bPR.anExceptionOccurs();
  }

  private void setupEvents() {
    this.addProductToCart = AddProductToCart.class;
    this.checkOutPurchase = StartCheckoutProcess.class;
    this.enterShippingInformation = EnterShippingInformation.class;
    this.enterPaymentDetails = EnterPaymentDetails.class;
    this.confirmPurchase = ConfirmPurchase.class;
    this.signalToGoBack = SignalToGoBack.class;
    this.anyException = Throwable.class;
  }

  private void setupSystemReactions(BuyProductRealization bPR) {
    this.startWithEmptyShoppingCart = bPR.startWithEmptyShoppingCart();
    this.displayProducts = bPR.displayProducts();
    this.addProductToPurchaseOrder = bPR.addProductToPurchaseOrder();
    this.displayShippingInformationForm = bPR.displayShippingInformationForm();
    this.saveShippingInformation = bPR.saveShippingInformation();
    this.displayPaymentDetailsForm = bPR.displayPaymentDetailsForm();
    this.savePaymentDetails = bPR.savePaymentDetails();
    this.displayPurchaseOrderSummary = bPR.displayPurchaseOrderSummary();
    this.initiateShipping = bPR.initiateShipping();
    this.logException = bPR.logException();
  }
}
