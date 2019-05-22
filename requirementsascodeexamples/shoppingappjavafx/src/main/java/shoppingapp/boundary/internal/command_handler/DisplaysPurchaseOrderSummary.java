package shoppingapp.boundary.internal.command_handler;

import shoppingapp.boundary.RunContext;
import shoppingapp.boundary.driven_port.Display;
import shoppingapp.boundary.internal.domain.PurchaseOrder;

public class DisplaysPurchaseOrderSummary implements Runnable {
    private RunContext runContext;
    private Display display;

    public DisplaysPurchaseOrderSummary(RunContext runContext, Display display) {
	this.runContext = runContext;
	this.display = display;
    }

    @Override
    public void run() {
	PurchaseOrder purchaseOrder = runContext.getPurchaseOrder();
	display.displayPurchaseOrderSummary(purchaseOrder);
    }

}
