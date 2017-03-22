package shoppingappjavafx.usecaserealization;

import org.requirementsascode.UseCaseRunner;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecase.BuyProductRealization;
import shoppingappjavafx.usecase.event.AddProductToCart;
import shoppingappjavafx.usecase.event.CheckoutPurchase;
import shoppingappjavafx.usecase.event.ConfirmPurchase;
import shoppingappjavafx.usecase.event.EnterPaymentDetails;
import shoppingappjavafx.usecase.event.EnterShippingInformation;
import shoppingappjavafx.usecase.event.Products;

public class ShoppingAppBuyProductRealization implements BuyProductRealization{
	private Stock stock;
	private Display display;
	private PurchaseOrder purchaseOrder;

	public ShoppingAppBuyProductRealization(Stock stock, Display display) {
		this.stock = stock;
		this.display = display;
	}
	
	@Override
	public void startWithEmptyShoppingCart(){
		purchaseOrder = new PurchaseOrder();
	}
	
	@Override
	public void displayProducts() {
		Products products = new Products(stock.findProducts());
		display.displayProductsAndShoppingCartSize(products, purchaseOrder);
	}
	
	@Override
	public void addProductToPurchaseOrder(AddProductToCart addProductToCart) {
		purchaseOrder.addProduct(addProductToCart.get());
	}
	
	@Override
	public void checkoutPurchase(CheckoutPurchase checkoutPurchase) {
	}

	@Override
	public boolean lessThen10Products(UseCaseRunner r) {
		return purchaseOrder.findProducts().size() < 10;
	}

	@Override
	public void displayShippingInformationForm() {
		display.displayShippingInformationForm(purchaseOrder.shippingInformation());
	}

	@Override
	public void saveShippingInformation(EnterShippingInformation enterShippingInformation) {
		purchaseOrder.saveShippingInformation(enterShippingInformation.get());
	}
	
	@Override
	public void displayPaymentDetailsForm() {
		display.displayPaymentDetailsForm();
	}

	@Override
	public void savePaymentDetails(EnterPaymentDetails enterPaymentDetails) {
		purchaseOrder.savePaymentDetails(enterPaymentDetails.get());
	}
	
	@Override
	public void displayPurchaseOrderSummary() {
		display.displayPurchaseOrderSummary(purchaseOrder);
	}
	
	@Override
	public void initiateShipping(ConfirmPurchase confirmPurchase) {
	}
	
	@Override
	public boolean atLeastOneProductInCart(UseCaseRunner r) {
		return purchaseOrder.findProducts().size() > 0;
	}

	@Override
	public void informUserAndLogException(Throwable t) {
		t.printStackTrace();
	}
}
