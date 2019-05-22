package shoppingapp.boundary.internal.command_handler;

import shoppingapp.boundary.RunContext;
import shoppingapp.boundary.internal.domain.PurchaseOrder;

public class StartsWithEmptyShoppingCart implements Runnable {
    private RunContext runContext;

    public StartsWithEmptyShoppingCart(RunContext runContext) {
	this.runContext = runContext;
    }

    @Override
    public void run() {
	runContext.setPurchaseOrder(new PurchaseOrder());
    }
}
