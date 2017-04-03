package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModelRunner;

import shoppingappjavafx.domain.ShippingInformation;
import shoppingappjavafx.usecaserealization.RunContext;
import shoppingappjavafx.usecaserealization.componentinterface.Display;

public class DisplayShippingInformationForm implements Consumer<UseCaseModelRunner> {
	private RunContext runContext;
	private Display display;

	public DisplayShippingInformationForm(RunContext runContext, Display display) {
		this.runContext = runContext;
		this.display = display;
	}
	
	@Override
	public void accept(UseCaseModelRunner runner) {
		ShippingInformation shippingInformation = runContext.getPurchaseOrder().getShippingInformation();
		display.displayShippingInformationForm(shippingInformation);
	}
}
