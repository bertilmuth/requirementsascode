package shoppingappjavafx.usecaserealization.condition;

import java.util.function.Predicate;

import org.requirementsascode.ModelRunner;

import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecaserealization.RunContext;

public class AtLeastOneProductIsInCart implements Predicate<ModelRunner> {
	private RunContext runContext;

	public AtLeastOneProductIsInCart(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public boolean test(ModelRunner useCaseModelRunner) {
		PurchaseOrder purchaseOrder = runContext.getPurchaseOrder();
		return purchaseOrder.findProducts().size() > 0;
	}
}
