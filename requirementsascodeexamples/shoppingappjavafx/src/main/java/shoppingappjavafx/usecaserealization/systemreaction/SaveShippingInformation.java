package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import shoppingappjavafx.usecase.event.EnterShippingInformation;
import shoppingappjavafx.usecaserealization.RunContext;

public class SaveShippingInformation implements Consumer<EnterShippingInformation> {
	private RunContext runContext;

	public SaveShippingInformation(RunContext runContext) {
		this.runContext = runContext;
	}
	
	@Override
	public void accept(EnterShippingInformation enterShippingInformation) {
		runContext.getPurchaseOrder().saveShippingInformation(enterShippingInformation.get());
	}

}
