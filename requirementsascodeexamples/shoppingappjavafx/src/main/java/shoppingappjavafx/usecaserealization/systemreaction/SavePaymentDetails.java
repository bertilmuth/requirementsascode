package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import shoppingappjavafx.usecase.userevent.EnterPaymentDetails;
import shoppingappjavafx.usecaserealization.RunContext;

public class SavePaymentDetails implements Consumer<EnterPaymentDetails> {
	private RunContext runContext;
	
	public SavePaymentDetails(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public void accept(EnterPaymentDetails enterPaymentDetails) {
		runContext.getPurchaseOrder().savePaymentDetails(enterPaymentDetails.get());
	}

}
