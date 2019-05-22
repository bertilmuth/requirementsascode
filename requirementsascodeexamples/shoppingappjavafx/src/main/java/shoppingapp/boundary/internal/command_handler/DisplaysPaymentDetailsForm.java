package shoppingapp.boundary.internal.command_handler;

import shoppingapp.boundary.driven_port.Display;

public class DisplaysPaymentDetailsForm implements Runnable {
    private Display display;

    public DisplaysPaymentDetailsForm(Display display) {
	this.display = display;
    }

    @Override
    public void run() {
	display.displayPaymentDetailsForm();
    }

}
