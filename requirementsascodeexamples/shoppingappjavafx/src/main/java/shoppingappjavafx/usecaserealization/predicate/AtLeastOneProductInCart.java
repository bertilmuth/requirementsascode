package shoppingappjavafx.usecaserealization.predicate;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecaserealization.RunContext;

public class AtLeastOneProductInCart implements Predicate<UseCaseModelRunner> {
	private RunContext runContext;

	public AtLeastOneProductInCart(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public boolean test(UseCaseModelRunner useCaseModelRunner) {
		PurchaseOrder purchaseOrder = runContext.getPurchaseOrder();
		return purchaseOrder.findProducts().size() > 0;
	}
}
