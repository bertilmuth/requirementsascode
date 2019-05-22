package shoppingapp.boundary.internal.command_handler;

import shoppingapp.boundary.RunContext;
import shoppingapp.boundary.driven_port.Display;
import shoppingapp.boundary.internal.domain.ShippingInformation;

public class DisplaysShippingInformationForm implements Runnable {
    private RunContext runContext;
    private Display display;

    public DisplaysShippingInformationForm(RunContext runContext, Display display) {
	this.runContext = runContext;
	this.display = display;
    }

    @Override
    public void run() {
	ShippingInformation shippingInformation = runContext.getPurchaseOrder().getShippingInformation();
	display.displayShippingInformationForm(shippingInformation);
    }
}
