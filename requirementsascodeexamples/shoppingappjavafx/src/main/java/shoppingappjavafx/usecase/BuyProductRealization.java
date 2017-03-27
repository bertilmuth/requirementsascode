package shoppingappjavafx.usecase;

import org.requirementsascode.UseCaseRunner;

import shoppingappjavafx.usecase.event.AddProductToCart;
import shoppingappjavafx.usecase.event.CheckoutPurchase;
import shoppingappjavafx.usecase.event.ConfirmPurchase;
import shoppingappjavafx.usecase.event.EnterPaymentDetails;
import shoppingappjavafx.usecase.event.EnterShippingInformation;

public interface BuyProductRealization {
	void startWithEmptyShoppingCart(UseCaseRunner runner);
	void displayProducts(UseCaseRunner runner);
	void addProductToPurchaseOrder(AddProductToCart addProductToCart);
	boolean lessThen10Products(UseCaseRunner useCaseRunner);
	void checkoutPurchase(CheckoutPurchase checkoutPurchase);
	void displayShippingInformationForm(UseCaseRunner runner);
	void saveShippingInformation(EnterShippingInformation enterShippingInformation);
	void displayPaymentDetailsForm(UseCaseRunner runner);
	void savePaymentDetails(EnterPaymentDetails enterPaymentDetails);
	void displayPurchaseOrderSummary(UseCaseRunner runner);
	void initiateShipping(ConfirmPurchase confirmPurchase); 
	boolean atLeastOneProductInCart(UseCaseRunner useCaseRunner);
	void informUserAndLogException(Throwable t);
}
