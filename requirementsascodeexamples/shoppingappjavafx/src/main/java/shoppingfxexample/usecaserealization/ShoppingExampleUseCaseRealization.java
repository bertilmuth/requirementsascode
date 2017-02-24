package shoppingfxexample.usecaserealization;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.gui.ShoppingApplicationDisplay;
import shoppingfxexample.usecase.ShoppingExampleUseCaseModel;
import shoppingfxexample.usecase.event.BuyProduct;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.EnterShippingInformation;
import shoppingfxexample.usecase.event.FinishPurchase;
import shoppingfxexample.usecase.event.Products;

public class ShoppingExampleUseCaseRealization extends ShoppingExampleUseCaseModel{
	private Stock stock;
	private ShoppingApplicationDisplay display;
	private PurchaseOrder purchaseOrder;
	private Products products; 

	public ShoppingExampleUseCaseRealization(UseCaseModel useCaseModel, Stock stock, ShoppingApplicationDisplay display) {
		Objects.requireNonNull(useCaseModel);
		Objects.requireNonNull(stock);
		Objects.requireNonNull(display);
		this.stock = stock;
		this.display = display;
		super.createModel(useCaseModel);
	}

	@Override
	protected Runnable createPurchaseOrder() {
		return () -> purchaseOrder = new PurchaseOrder();
	}
	
	@Override
	protected Runnable findProducts() {
		return () -> products = new Products(stock.findProducts());		
	}
	
	@Override
	protected Runnable displayProducts() {
		return () -> display.displayProductsAndShoppingCartSize(products, purchaseOrder);
	}
	
	@Override
	protected Consumer<BuyProduct> addProductToPurchaseOrder() {
		return buyProduct -> purchaseOrder.addProduct(buyProduct.get());
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
	
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
	
	@Override
	protected Consumer<FinishPurchase> finishPurchase() {
		return fp -> {};
	}

	@Override
	protected Consumer<Throwable> logException() {
		return t -> t.printStackTrace();
	}
}
