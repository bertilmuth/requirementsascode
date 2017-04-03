package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModelRunner;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecaserealization.RunContext;

public class StartWithEmptyShoppingCart implements Consumer<UseCaseModelRunner> {
	private RunContext runContext;

	public StartWithEmptyShoppingCart(RunContext runContext) {
		this.runContext = runContext;
	}

	@Override
	public void accept(UseCaseModelRunner runner) {
		runContext.setPurchaseOrder(new PurchaseOrder());
	}
}
