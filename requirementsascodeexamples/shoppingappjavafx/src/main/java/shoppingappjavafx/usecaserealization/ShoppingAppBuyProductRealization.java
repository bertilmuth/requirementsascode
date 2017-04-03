package shoppingappjavafx.usecaserealization;

import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecase.BuyProductRealization;
import shoppingappjavafx.usecase.InformUserAndLogException;
import shoppingappjavafx.usecaserealization.componentinterface.Display;
import shoppingappjavafx.usecaserealization.predicate.AtLeastOneProductInCart;
import shoppingappjavafx.usecaserealization.predicate.LessThan10Products;
import shoppingappjavafx.usecaserealization.systemreaction.AddProductToPurchaseOrder;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayPaymentDetailsForm;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayProducts;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayPurchaseOrderSummary;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayShippingInformationForm;
import shoppingappjavafx.usecaserealization.systemreaction.InitiateShipping;
import shoppingappjavafx.usecaserealization.systemreaction.SavePaymentDetails;
import shoppingappjavafx.usecaserealization.systemreaction.SaveShippingInformation;
import shoppingappjavafx.usecaserealization.systemreaction.StartWithEmptyShoppingCart;

public class ShoppingAppBuyProductRealization implements BuyProductRealization{
	private Stock stock;
	private Display display;
	private RunContext runContext;

	public ShoppingAppBuyProductRealization(Stock stock, Display display) {
		this.stock = stock;
		this.display = display;
		this.runContext = new RunContext();
	}
	
	@Override
	public StartWithEmptyShoppingCart startWithEmptyShoppingCart(){
		return new StartWithEmptyShoppingCart(runContext);
	}
	
	@Override
	public DisplayProducts displayProducts() {
		return new DisplayProducts(runContext, stock, display);
	}
	
	@Override
	public AddProductToPurchaseOrder addProductToPurchaseOrder() {
		return new AddProductToPurchaseOrder(runContext);
	}

	@Override
	public LessThan10Products lessThan10Products() {
		return new LessThan10Products(runContext);
	}

	@Override
	public DisplayShippingInformationForm displayShippingInformationForm() {
		return new DisplayShippingInformationForm(runContext, display);
	}

	@Override
	public SaveShippingInformation saveShippingInformation() {
		return new SaveShippingInformation(runContext);
	}
	
	@Override
	public DisplayPaymentDetailsForm displayPaymentDetailsForm() {
		return new DisplayPaymentDetailsForm(display);
	}

	@Override
	public SavePaymentDetails savePaymentDetails() {
		return new SavePaymentDetails(runContext);
	}
	
	@Override
	public DisplayPurchaseOrderSummary displayPurchaseOrderSummary() {
		return new DisplayPurchaseOrderSummary(runContext, display);
	}
	
	@Override
	public InitiateShipping initiateShipping() {
		return new InitiateShipping();
	}
	
	@Override
	public AtLeastOneProductInCart atLeastOneProductInCart() {
		return new AtLeastOneProductInCart(runContext);
	}

	@Override
	public InformUserAndLogException informUserAndLogException() {
		return new InformUserAndLogException();
	}
}
