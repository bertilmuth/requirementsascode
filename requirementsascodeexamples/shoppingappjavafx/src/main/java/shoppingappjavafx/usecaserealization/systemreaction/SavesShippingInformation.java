package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import shoppingappjavafx.usecase.userevent.EntersShippingInformation;
import shoppingappjavafx.usecaserealization.RunContext;

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
