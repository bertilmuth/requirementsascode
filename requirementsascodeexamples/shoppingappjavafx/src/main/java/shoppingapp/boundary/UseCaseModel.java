package shoppingapp.boundary;

import org.requirementsascode.Model;
import org.requirementsascode.builder.ModelBuilder;

import shoppingapp.boundary.internal.command_handler.AddsProductToPurchaseOrder;
import shoppingapp.boundary.internal.command_handler.DisplaysPaymentDetailsForm;
import shoppingapp.boundary.internal.command_handler.DisplaysProducts;
import shoppingapp.boundary.internal.command_handler.DisplaysPurchaseOrderSummary;
import shoppingapp.boundary.internal.command_handler.DisplaysShippingInformationForm;
import shoppingapp.boundary.internal.command_handler.InitiatesShipping;
import shoppingapp.boundary.internal.command_handler.LogsException;
import shoppingapp.boundary.internal.command_handler.SavesPaymentDetails;
import shoppingapp.boundary.internal.command_handler.SavesShippingInformation;
import shoppingapp.boundary.internal.command_handler.StartsWithEmptyShoppingCart;
import shoppingapp.boundary.internal.rule.AtLeastOneProductIsInCart;
import shoppingapp.boundary.internal.rule.LessThan10Products;
import shoppingapp.command.AddsProductToCart;
import shoppingapp.command.ChecksOutPurchase;
import shoppingapp.command.ConfirmsPurchase;
import shoppingapp.command.EntersPaymentDetails;
import shoppingapp.command.EntersShippingInformation;
import shoppingapp.command.SignalsToGoBack;

/**
 * Responsible for building the model of the shopping app. 
 *
 * @author b_muth
 */
class UseCaseModel {
	/**
	 * User/system command classes
	 */
	private static final Class<AddsProductToCart> addsProductToCart = AddsProductToCart.class;
	private static final Class<ChecksOutPurchase> checksOutPurchase = ChecksOutPurchase.class;
	private static final Class<EntersShippingInformation> entersShippingInformation = EntersShippingInformation.class;
	private static final Class<EntersPaymentDetails> entersPaymentDetails = EntersPaymentDetails.class;
	private static final Class<ConfirmsPurchase> confirmsPurchase = ConfirmsPurchase.class;
	private static final Class<SignalsToGoBack> signalsToGoBack = SignalsToGoBack.class;
	private static final Class<Throwable> anyException = Throwable.class;

    /**
     * Builds the model using the specified builder.
     * @param displaysShippingInformationForm 
     * @param savesShippingInformation 
     * @param displaysPaymentDetailsForm 
     * @param savesPaymentDetails 
     * @param displaysPurchaseOrderSummary 
     * @param initiatesShipping 
     * @param logsException 
     * @param lessThan10Products 
     * @param atLeastOneProductIsInCart 
     *
     * @param modelBuilder
     *            the builder used
     * @return the created model
     */
	public Model build(StartsWithEmptyShoppingCart startsWithEmptyShoppingCart, DisplaysProducts displaysProducts,
			AddsProductToPurchaseOrder addsProductToPurchaseOrder,
			DisplaysShippingInformationForm displaysShippingInformationForm,
			SavesShippingInformation savesShippingInformation, DisplaysPaymentDetailsForm displaysPaymentDetailsForm,
			SavesPaymentDetails savesPaymentDetails, DisplaysPurchaseOrderSummary displaysPurchaseOrderSummary,
			InitiatesShipping initiatesShipping, LogsException logsException, LessThan10Products lessThan10Products,
			AtLeastOneProductIsInCart atLeastOneProductIsInCart, ModelBuilder modelBuilder) {
		
			Model model = modelBuilder.useCase("Buy product")
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
			.flow("Go back from shipping").insteadOf("S6").step("S6a_1").user(signalsToGoBack).step("S6a_2").continuesAt("S2")
			.flow("Go back from payment").insteadOf("S8").step("S8a_1").user(signalsToGoBack).step("S8a_2").continuesAt("S5")
			.flow("Checkout after going back").insteadOf("S3").condition(atLeastOneProductIsInCart).step("S3a_1").continuesAt("S4")
			.flow("Handle exceptions").step("EX").on(anyException).system(logsException)
		.build();
		
		return model;
    }
}
