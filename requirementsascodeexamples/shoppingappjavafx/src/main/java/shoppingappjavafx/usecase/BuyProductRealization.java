package shoppingappjavafx.usecase;

import org.requirementsascode.UseCaseModelRunner;

import shoppingappjavafx.usecase.event.AddProductToCart;
import shoppingappjavafx.usecase.event.CheckoutPurchase;
import shoppingappjavafx.usecase.event.ConfirmPurchase;
import shoppingappjavafx.usecase.event.EnterPaymentDetails;
import shoppingappjavafx.usecase.event.EnterShippingInformation;

public interface BuyProductRealization {
	void startWithEmptyShoppingCart(UseCaseModelRunner runner);
	void displayProducts(UseCaseModelRunner runner);
	void addProductToPurchaseOrder(AddProductToCart addProductToCart);
	boolean lessThen10Products(UseCaseModelRunner useCaseRunner);
	void checkoutPurchase(CheckoutPurchase checkoutPurchase);
	void displayShippingInformationForm(UseCaseModelRunner runner);
	void saveShippingInformation(EnterShippingInformation enterShippingInformation);
	void displayPaymentDetailsForm(UseCaseModelRunner runner);
	void savePaymentDetails(EnterPaymentDetails enterPaymentDetails);
	void displayPurchaseOrderSummary(UseCaseModelRunner runner);
	void initiateShipping(ConfirmPurchase confirmPurchase); 
	boolean atLeastOneProductInCart(UseCaseModelRunner useCaseRunner);
	void informUserAndLogException(Throwable t);
}
