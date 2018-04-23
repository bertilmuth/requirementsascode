package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.ModelRunner;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecaserealization.RunContext;

public class StartsWithEmptyShoppingCart implements Consumer<ModelRunner> {
	private RunContext runContext;

	public StartsWithEmptyShoppingCart(RunContext runContext) {
		this.runContext = runContext;
	}

	@Override
	public void accept(ModelRunner runner) {
		runContext.setPurchaseOrder(new PurchaseOrder());
	}
}
