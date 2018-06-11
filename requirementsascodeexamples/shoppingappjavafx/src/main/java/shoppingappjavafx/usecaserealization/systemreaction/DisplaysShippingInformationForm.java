package shoppingappjavafx.usecaserealization.systemreaction;

import shoppingappjavafx.domain.ShippingInformation;
import shoppingappjavafx.usecaserealization.RunContext;
import shoppingappjavafx.usecaserealization.componentinterface.Display;

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
