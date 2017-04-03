package shoppingappjavafx.usecaserealization;

import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecaserealization.componentinterface.Display;
import shoppingappjavafx.usecaserealization.predicate.AtLeastOneProductInCart;
import shoppingappjavafx.usecaserealization.predicate.LessThan10Products;
import shoppingappjavafx.usecaserealization.systemreaction.AddProductToPurchaseOrder;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayPaymentDetailsForm;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayProducts;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayPurchaseOrderSummary;
import shoppingappjavafx.usecaserealization.systemreaction.DisplayShippingInformationForm;
import shoppingappjavafx.usecaserealization.systemreaction.InformUserAndLogException;
import shoppingappjavafx.usecaserealization.systemreaction.InitiateShipping;
import shoppingappjavafx.usecaserealization.systemreaction.SavePaymentDetails;
import shoppingappjavafx.usecaserealization.systemreaction.SaveShippingInformation;
import shoppingappjavafx.usecaserealization.systemreaction.StartWithEmptyShoppingCart;

public class BuyProductRealization{
	private Stock stock;
	private Display display;
	private RunContext runContext;

	public BuyProductRealization(Stock stock, Display display) {
		this.stock = stock;
		this.display = display;
		this.runContext = new RunContext();
	}
	
	public StartWithEmptyShoppingCart startWithEmptyShoppingCart(){
		return new StartWithEmptyShoppingCart(runContext);
	}
	
	public DisplayProducts displayProducts() {
		return new DisplayProducts(runContext, stock, display);
	}
	
	public AddProductToPurchaseOrder addProductToPurchaseOrder() {
		return new AddProductToPurchaseOrder(runContext);
	}

	public LessThan10Products lessThan10Products() {
		return new LessThan10Products(runContext);
	}

	public DisplayShippingInformationForm displayShippingInformationForm() {
		return new DisplayShippingInformationForm(runContext, display);
	}

	public SaveShippingInformation saveShippingInformation() {
		return new SaveShippingInformation(runContext);
	}
	
	public DisplayPaymentDetailsForm displayPaymentDetailsForm() {
		return new DisplayPaymentDetailsForm(display);
	}

	public SavePaymentDetails savePaymentDetails() {
		return new SavePaymentDetails(runContext);
	}
	
	public DisplayPurchaseOrderSummary displayPurchaseOrderSummary() {
		return new DisplayPurchaseOrderSummary(runContext, display);
	}
	
	public InitiateShipping initiateShipping() {
		return new InitiateShipping();
	}
	
	public AtLeastOneProductInCart atLeastOneProductInCart() {
		return new AtLeastOneProductInCart(runContext);
	}

	public InformUserAndLogException informUserAndLogException() {
		return new InformUserAndLogException();
	}
}
