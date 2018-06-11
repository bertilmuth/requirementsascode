package shoppingappjavafx.usecaserealization.systemreaction;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecaserealization.RunContext;
import shoppingappjavafx.usecaserealization.componentinterface.Display;

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
