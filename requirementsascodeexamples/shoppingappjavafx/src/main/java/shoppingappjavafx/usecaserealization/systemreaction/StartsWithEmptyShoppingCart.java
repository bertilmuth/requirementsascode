package shoppingappjavafx.usecaserealization.systemreaction;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecaserealization.RunContext;

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
