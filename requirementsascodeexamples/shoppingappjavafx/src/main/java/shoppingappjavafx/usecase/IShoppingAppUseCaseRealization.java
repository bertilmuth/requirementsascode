package shoppingappjavafx.usecase;

import org.requirementsascode.UseCaseRunner;

import shoppingappjavafx.usecase.event.AddProductToCart;
import shoppingappjavafx.usecase.event.CheckoutPurchase;
import shoppingappjavafx.usecase.event.ConfirmPurchase;
import shoppingappjavafx.usecase.event.EnterPaymentDetails;
import shoppingappjavafx.usecase.event.EnterShippingInformation;

public interface IShoppingAppUseCaseRealization {
	void startWithEmptyShoppingCart();
	void displayProducts();
	void addProductToPurchaseOrder(AddProductToCart addProductToCart);
	boolean lessThen10Products(UseCaseRunner useCaseRunner);
	void checkoutPurchase(CheckoutPurchase checkoutPurchase);
	void displayShippingInformationForm();
	void saveShippingInformation(EnterShippingInformation enterShippingInformation);
	void displayPaymentDetailsForm();
	void savePaymentDetails(EnterPaymentDetails enterPaymentDetails);
	void displayPurchaseOrderSummary();
	void initiateShipping(ConfirmPurchase confirmPurchase); 
	boolean atLeastOneProductInCart(UseCaseRunner useCaseRunner);
	void informUserAndLogException(Throwable t);
}
