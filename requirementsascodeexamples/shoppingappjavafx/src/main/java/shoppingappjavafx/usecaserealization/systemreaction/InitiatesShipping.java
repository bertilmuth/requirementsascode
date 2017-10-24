package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import shoppingappjavafx.usecase.userevent.ConfirmsPurchase;

public class InitiatesShipping implements Consumer<ConfirmsPurchase> {
	public InitiatesShipping() {
	}
	
	@Override
	public void accept(ConfirmsPurchase confirmPurchase) {	
	}

}
