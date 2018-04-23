package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.ModelRunner;

import shoppingappjavafx.domain.ShippingInformation;
import shoppingappjavafx.usecaserealization.RunContext;
import shoppingappjavafx.usecaserealization.componentinterface.Display;

public class DisplaysShippingInformationForm implements Consumer<ModelRunner> {
	private RunContext runContext;
	private Display display;

	public DisplaysShippingInformationForm(RunContext runContext, Display display) {
		this.runContext = runContext;
		this.display = display;
	}
	
	@Override
	public void accept(ModelRunner runner) {
		ShippingInformation shippingInformation = runContext.getPurchaseOrder().getShippingInformation();
		display.displayShippingInformationForm(shippingInformation);
	}
}
