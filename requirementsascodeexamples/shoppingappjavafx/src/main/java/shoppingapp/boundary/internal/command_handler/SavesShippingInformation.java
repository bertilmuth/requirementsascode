package shoppingapp.boundary.internal.command_handler;

import java.util.function.Consumer;

import shoppingapp.boundary.RunContext;
import shoppingapp.command.EntersShippingInformation;

public class SavesShippingInformation implements Consumer<EntersShippingInformation> {
	private RunContext runContext;

	public SavesShippingInformation(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public void accept(EntersShippingInformation enterShippingInformation) {
		runContext.getPurchaseOrder().saveShippingInformation(enterShippingInformation.get());
	}

}
