package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import shoppingappjavafx.usecase.userevent.EntersPaymentDetails;
import shoppingappjavafx.usecaserealization.RunContext;

public class SavesPaymentDetails implements Consumer<EntersPaymentDetails> {
	private RunContext runContext;
	
	public SavesPaymentDetails(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public void accept(EntersPaymentDetails enterPaymentDetails) {
		runContext.getPurchaseOrder().savePaymentDetails(enterPaymentDetails.get());
	}

}
