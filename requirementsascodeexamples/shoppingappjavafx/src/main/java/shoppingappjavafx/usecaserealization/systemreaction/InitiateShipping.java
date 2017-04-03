package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

import shoppingappjavafx.usecase.event.ConfirmPurchase;

public class InitiateShipping implements Consumer<ConfirmPurchase> {
	public InitiateShipping() {
	}
	
	@Override
	public void accept(ConfirmPurchase confirmPurchase) {	
	}

}
