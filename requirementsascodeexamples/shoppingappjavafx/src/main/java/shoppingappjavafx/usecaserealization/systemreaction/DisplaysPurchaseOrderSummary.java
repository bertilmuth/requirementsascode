package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.ModelRunner;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecaserealization.RunContext;
import shoppingappjavafx.usecaserealization.componentinterface.Display;

public class DisplaysPurchaseOrderSummary implements Consumer<ModelRunner> {
	private RunContext runContext;
	private Display display;

	public DisplaysPurchaseOrderSummary(RunContext runContext, Display display) {
		this.runContext = runContext;
		this.display = display;
	}

	@Override
	public void accept(ModelRunner modelRunner) {
		PurchaseOrder purchaseOrder = runContext.getPurchaseOrder();
		display.displayPurchaseOrderSummary(purchaseOrder);
	}

}
