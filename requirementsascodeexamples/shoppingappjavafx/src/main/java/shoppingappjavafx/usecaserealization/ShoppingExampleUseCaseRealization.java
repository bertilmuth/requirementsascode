package shoppingappjavafx.usecaserealization;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseRunner;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecase.ShoppingExampleUseCaseModel;
import shoppingappjavafx.usecase.event.AddProductToCart;
import shoppingappjavafx.usecase.event.CheckoutPurchase;
import shoppingappjavafx.usecase.event.ConfirmPurchase;
import shoppingappjavafx.usecase.event.EnterPaymentDetails;
import shoppingappjavafx.usecase.event.EnterShippingInformation;
import shoppingappjavafx.usecase.event.Products;
import shoppingappjavafx.usecaserealization.interfaces.Display;

public class ShoppingExampleUseCaseRealization extends ShoppingExampleUseCaseModel{
	private Stock stock;
	private Display display;
	private PurchaseOrder purchaseOrder;

	public ShoppingExampleUseCaseRealization(Stock stock, Display display) {
		this.stock = stock;
		this.display = display;
	}
	
	@Override
	protected Runnable startWithEmptyShoppingCart(){
		return () -> purchaseOrder = new PurchaseOrder();
	}
	
	@Override
	protected Runnable displayProducts() {
		return () -> {
			Products products = new Products(stock.findProducts());
			display.displayProductsAndShoppingCartSize(products, purchaseOrder);
		};
	}
	
	@Override
	protected Consumer<AddProductToCart> addProductToPurchaseOrder() {
		return addProductToCart -> purchaseOrder.addProduct(addProductToCart.get());
	}
	
	@Override
	protected Consumer<CheckoutPurchase> checkoutPurchase() {
		return cp -> {;};
	}

	@Override
	protected Predicate<UseCaseRunner> lessThen10Products() {
		return r -> purchaseOrder.findProducts().size() < 10;
	}

	@Override
	protected Runnable displayShippingInformationForm() {
		return () -> display.displayShippingInformationForm(purchaseOrder.shippingInformation());
	}

	@Override
	protected Consumer<EnterShippingInformation> saveShippingInformation() {
		return enterShippingInformation -> purchaseOrder.saveShippingInformation(enterShippingInformation.get());
	}
	
	@Override
	protected Runnable displayPaymentDetailsForm() {
		return display::displayPaymentDetailsForm;
	}

	@Override
	protected Consumer<EnterPaymentDetails> savePaymentDetails() {
		return enterPaymentDetails -> purchaseOrder.savePaymentDetails(enterPaymentDetails.get());
	}
	
	@Override
	protected Runnable displayPurchaseOrderSummary() {
		return () -> display.displayPurchaseOrderSummary(purchaseOrder);
	}
	
	@Override
	protected Consumer<ConfirmPurchase> initiateShipping() {
		return fp -> {};
	}
	
	@Override
	protected Predicate<UseCaseRunner> atLeastOneProductInCart() {
		return r -> purchaseOrder.findProducts().size() > 0;
	}

	@Override
	protected Consumer<Throwable> informUserAndLogException() {
		return t -> t.printStackTrace();
	}
}
