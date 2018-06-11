package shoppingappjavafx.usecaserealization.systemreaction;

import shoppingappjavafx.usecaserealization.componentinterface.Display;

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
