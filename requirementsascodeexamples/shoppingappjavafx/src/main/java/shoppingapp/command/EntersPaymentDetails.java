package shoppingapp.command;

import shoppingapp.boundary.internal.domain.PaymentDetails;

public class EntersPaymentDetails {
	private PaymentDetails paymentDetails;

	public EntersPaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public PaymentDetails get() {
		return paymentDetails;
	}
}
