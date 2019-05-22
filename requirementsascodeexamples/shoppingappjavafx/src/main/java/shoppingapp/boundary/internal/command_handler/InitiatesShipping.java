package shoppingapp.boundary.internal.command_handler;

import java.util.function.Consumer;

import shoppingapp.command.ConfirmsPurchase;

public class InitiatesShipping implements Consumer<ConfirmsPurchase> {
	public InitiatesShipping() {
	}
	
	@Override
	public void accept(ConfirmsPurchase confirmPurchase) {	
	}

}
