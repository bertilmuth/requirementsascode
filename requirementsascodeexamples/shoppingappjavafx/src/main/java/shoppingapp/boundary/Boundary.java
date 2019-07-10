package shoppingapp.boundary;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import shoppingapp.boundary.driven_port.Display;
import shoppingapp.boundary.driver_port.IReactToCommands;
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
import shoppingapp.boundary.internal.domain.ProductContainer;
import shoppingapp.boundary.internal.rule.AtLeastOneProductIsInCart;
import shoppingapp.boundary.internal.rule.LessThan10Products;

public class Boundary implements IReactToCommands {
	private ModelRunner modelRunner;
	private Model model;

	public Boundary(ProductContainer productContainer, Display display) {
		model = buildModel(productContainer, display);
		modelRunner = new ModelRunner();
	}

	private Model buildModel(ProductContainer productContainer, Display display) {
		RunContext runContext = new RunContext();
		StartsWithEmptyShoppingCart startsWithEmptyShoppingCart = new StartsWithEmptyShoppingCart(runContext);
		DisplaysProducts displaysProducts = new DisplaysProducts(runContext, productContainer, display);
		AddsProductToPurchaseOrder addsProductToPurchaseOrder = new AddsProductToPurchaseOrder(runContext);
		DisplaysShippingInformationForm displaysShippingInformationForm = new DisplaysShippingInformationForm(
				runContext, display);
		SavesShippingInformation savesShippingInformation = new SavesShippingInformation(runContext);
		DisplaysPaymentDetailsForm displaysPaymentDetailsForm = new DisplaysPaymentDetailsForm(display);
		SavesPaymentDetails savesPaymentDetails = new SavesPaymentDetails(runContext);
		DisplaysPurchaseOrderSummary displaysPurchaseOrderSummary = new DisplaysPurchaseOrderSummary(runContext,
				display);
		InitiatesShipping initiatesShipping = new InitiatesShipping();
		LogsException logsException = new LogsException();
		LessThan10Products lessThan10Products = new LessThan10Products(runContext);
		AtLeastOneProductIsInCart atLeastOneProductIsInCart = new AtLeastOneProductIsInCart(runContext);

		Model model = new UseCaseModel().build(startsWithEmptyShoppingCart, displaysProducts,
				addsProductToPurchaseOrder, displaysShippingInformationForm, savesShippingInformation,
				displaysPaymentDetailsForm, savesPaymentDetails, displaysPurchaseOrderSummary, initiatesShipping,
				logsException, lessThan10Products, atLeastOneProductIsInCart, Model.builder());
		return model;
	}
	
	public void run() {
		modelRunner.startRecording().run(model);
	}
	
	@Override
	public boolean canReactTo(Class<? extends Object> command) {
		return modelRunner.canReactTo(command);
	}
	
	@Override
	public void reactTo(Object... commandObjects) {
		modelRunner.reactTo(commandObjects);
	}
	
	public String[] getRecordedStepNames() {
		String[] stepNames = modelRunner.getRecordedStepNames();
		return stepNames;
	}

	public Model getModel() {
		return model;
	}
}
