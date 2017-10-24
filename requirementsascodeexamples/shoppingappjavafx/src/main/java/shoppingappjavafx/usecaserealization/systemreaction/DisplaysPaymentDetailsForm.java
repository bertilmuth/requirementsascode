package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModelRunner;

import shoppingappjavafx.usecaserealization.componentinterface.Display;

public class DisplaysPaymentDetailsForm implements Consumer<UseCaseModelRunner> {
	private Display display;
	
	public DisplaysPaymentDetailsForm(Display display) {
		this.display = display;
	}
	@Override
	public void accept(UseCaseModelRunner runner) {
		display.displayPaymentDetailsForm();		
	}

}
