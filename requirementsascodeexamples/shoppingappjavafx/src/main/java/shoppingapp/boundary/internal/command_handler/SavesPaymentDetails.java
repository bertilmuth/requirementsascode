package shoppingapp.boundary.internal.command_handler;

import java.util.function.Consumer;

import shoppingapp.boundary.RunContext;
import shoppingapp.command.EntersPaymentDetails;

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
