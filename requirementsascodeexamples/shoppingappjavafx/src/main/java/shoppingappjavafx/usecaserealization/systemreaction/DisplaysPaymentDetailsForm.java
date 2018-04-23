package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.ModelRunner;

import shoppingappjavafx.usecaserealization.componentinterface.Display;

public class DisplaysPaymentDetailsForm implements Consumer<ModelRunner> {
	private Display display;
	
	public DisplaysPaymentDetailsForm(Display display) {
		this.display = display;
	}
	@Override
	public void accept(ModelRunner runner) {
		display.displayPaymentDetailsForm();		
	}

}
