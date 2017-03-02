package shoppingfxexample.usecaserealization;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.gui.ShoppingApplicationDisplay;
import shoppingfxexample.usecase.ShoppingExampleUseCaseModel;
import shoppingfxexample.usecase.event.AddProductToCart;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.ConfirmPurchase;
import shoppingfxexample.usecase.event.EnterShippingInformation;
import shoppingfxexample.usecase.event.Products;

public class ShoppingExampleUseCaseRealization extends ShoppingExampleUseCaseModel{
	private Stock stock;
	private ShoppingApplicationDisplay display;
	private PurchaseOrder purchaseOrder;

	public ShoppingExampleUseCaseRealization(UseCaseModel useCaseModel, Stock stock, ShoppingApplicationDisplay display) {
		this.stock = stock;
		this.display = display;
		super.createModel(useCaseModel);
	}
	
	@Override
	protected Runnable startWithEmptyShoppingCart(){
		return () -> {
			purchaseOrder = new PurchaseOrder();
		};
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
	protected Predicate<UseCaseRunner> lessThen10Products() {
		return r -> purchaseOrder.findProducts().size() < 10;
	}

	@Override
	protected Consumer<CheckoutPurchase> displayShippingInformationForm() {
		return display::displayShippingInformationForm;
	}

	@Override
	protected Consumer<EnterShippingInformation> saveShippingInformation() {
		return enterShippingInformation -> purchaseOrder.saveShippingInformation(enterShippingInformation.get());
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
	protected Consumer<Throwable> logException() {
		return t -> t.printStackTrace();
	}
}
