package shoppingappjavafx.usecase;

import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;

import shoppingappjavafx.usecase.userevent.AddsProductToCart;
import shoppingappjavafx.usecase.userevent.StartsCheckoutProcess;
import shoppingappjavafx.usecase.userevent.ConfirmsPurchase;
import shoppingappjavafx.usecase.userevent.EntersPaymentDetails;
import shoppingappjavafx.usecase.userevent.EntersShippingInformation;
import shoppingappjavafx.usecase.userevent.SignalsToGoBack;
import shoppingappjavafx.usecaserealization.BuyProductRealization;
import shoppingappjavafx.usecaserealization.condition.AnExceptionOccurs;
import shoppingappjavafx.usecaserealization.condition.AtLeastOneProductIsInCart;
import shoppingappjavafx.usecaserealization.condition.LessThan10Products;
import shoppingappjavafx.usecaserealization.systemreaction.AddsProductToPurchaseOrder;
import shoppingappjavafx.usecaserealization.systemreaction.DisplaysPaymentDetailsForm;
import shoppingappjavafx.usecaserealization.systemreaction.DisplaysProducts;
import shoppingappjavafx.usecaserealization.systemreaction.DisplaysPurchaseOrderSummary;
import shoppingappjavafx.usecaserealization.systemreaction.DisplaysShippingInformationForm;
import shoppingappjavafx.usecaserealization.systemreaction.InitiatesShipping;
import shoppingappjavafx.usecaserealization.systemreaction.LogsException;
import shoppingappjavafx.usecaserealization.systemreaction.SavesPaymentDetails;
import shoppingappjavafx.usecaserealization.systemreaction.SavesShippingInformation;
import shoppingappjavafx.usecaserealization.systemreaction.StartsWithEmptyShoppingCart;

/**
 * Responsible for building the use case model of the shopping app. Its instance is injected with a
 * use case realization that contains the methods to call.
 *
 * @author b_muth
 */
public class ShoppingAppModel {
  /**
   * Conditions
   */
  private LessThan10Products lessThan10Products;
  private AtLeastOneProductIsInCart atLeastOneProductIsInCart;
  private AnExceptionOccurs anExceptionOccurs;

  /**
   * User/system events
   */
  private Class<AddsProductToCart> addsProductToCart;
  private Class<StartsCheckoutProcess> checksOutPurchase;
  private Class<EntersShippingInformation> entersShippingInformation;
  private Class<EntersPaymentDetails> entersPaymentDetails;
  private Class<ConfirmsPurchase> confirmsPurchase;
  private Class<SignalsToGoBack> signalsToGoBack;
  private Class<Throwable> anyException;
  
  /**
   * System reactions
   */
  private StartsWithEmptyShoppingCart startsWithEmptyShoppingCart;
  private DisplaysProducts displaysProducts;
  private AddsProductToPurchaseOrder addsProductToPurchaseOrder;
  private DisplaysShippingInformationForm displaysShippingInformationForm;
  private SavesShippingInformation savesShippingInformation;
  private DisplaysPaymentDetailsForm displaysPaymentDetailsForm;
  private SavesPaymentDetails savesPaymentDetails;
  private DisplaysPurchaseOrderSummary displaysPurchaseOrderSummary;
  private InitiatesShipping initiatesShipping;
  private LogsException logsException;

  /**
   * Builds the use case model using the specified builder.
   *
   * @param modelBuilder the builder used
   * @return the created model
   */
  public Model buildWith(ModelBuilder modelBuilder) {
		Model useCaseModel = modelBuilder.useCase("Buy product")
			.basicFlow()
				.step("S1").system(startsWithEmptyShoppingCart)
				.step("S2").system(displaysProducts)
				.step("S3").user(addsProductToCart).system(addsProductToPurchaseOrder).reactWhile(lessThan10Products)
				.step("S4").user(checksOutPurchase)
				.step("S5").system(displaysShippingInformationForm)
				.step("S6").user(entersShippingInformation).system(savesShippingInformation)
				.step("S7").system(displaysPaymentDetailsForm)
				.step("S8").user(entersPaymentDetails).system(savesPaymentDetails)
				.step("S9").system(displaysPurchaseOrderSummary)
				.step("S10").user(confirmsPurchase).system(initiatesShipping)
				.step("S11").continuesAt("S1")	
			.flow("Go back from shipping").insteadOf("S6")
				.step("S6a_1").user(signalsToGoBack)
				.step("S6a_2").continuesAt("S2")
			.flow("Go back from payment").insteadOf("S8")
				.step("S8a_1").user(signalsToGoBack)
				.step("S8a_2").continuesAt("S5")
			.flow("Checkout after going back").insteadOf("S3").when(atLeastOneProductIsInCart)
				.step("S3a_1").continuesAt("S4")
			.flow("Handle exceptions").when(anExceptionOccurs).step("EX").handles(anyException).system(logsException)
		.build();
		
		return useCaseModel;
	}
  
  public ShoppingAppModel(BuyProductRealization bPR) {
    setupConditions(bPR);
    setupEvents();
    setupSystemReactions(bPR);
  }

  private void setupConditions(BuyProductRealization bPR) {
    this.lessThan10Products = bPR.lessThan10Products();
    this.atLeastOneProductIsInCart = bPR.atLeastOneProductIsInCart();
    this.anExceptionOccurs = bPR.anExceptionOccurs();
  }

  private void setupEvents() {
    this.addsProductToCart = AddsProductToCart.class;
    this.checksOutPurchase = StartsCheckoutProcess.class;
    this.entersShippingInformation = EntersShippingInformation.class;
    this.entersPaymentDetails = EntersPaymentDetails.class;
    this.confirmsPurchase = ConfirmsPurchase.class;
    this.signalsToGoBack = SignalsToGoBack.class;
    this.anyException = Throwable.class;
  }

  private void setupSystemReactions(BuyProductRealization bPR) {
    this.startsWithEmptyShoppingCart = bPR.startsWithEmptyShoppingCart();
    this.displaysProducts = bPR.displaysProducts();
    this.addsProductToPurchaseOrder = bPR.addsProductToPurchaseOrder();
    this.displaysShippingInformationForm = bPR.displaysShippingInformationForm();
    this.savesShippingInformation = bPR.savesShippingInformation();
    this.displaysPaymentDetailsForm = bPR.displaysPaymentDetailsForm();
    this.savesPaymentDetails = bPR.savesPaymentDetails();
    this.displaysPurchaseOrderSummary = bPR.displaysPurchaseOrderSummary();
    this.initiatesShipping = bPR.initiatesShipping();
    this.logsException = bPR.logsException();
  }
}
