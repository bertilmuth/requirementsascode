package shoppingfxexample.usecase.event;

import shoppingfxexample.domain.PaymentDetails;

public class EnterPaymentDetails {
	private PaymentDetails paymentDetails;

	public EnterPaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public PaymentDetails get() {
		return paymentDetails;
	}
}
