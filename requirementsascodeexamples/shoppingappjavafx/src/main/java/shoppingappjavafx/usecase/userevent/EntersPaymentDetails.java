package shoppingappjavafx.usecase.userevent;

import shoppingappjavafx.domain.PaymentDetails;

public class EntersPaymentDetails {
	private PaymentDetails paymentDetails;

	public EntersPaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public PaymentDetails get() {
		return paymentDetails;
	}
}
