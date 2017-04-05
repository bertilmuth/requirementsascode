package shoppingappjavafx.usecase.userevent;

import shoppingappjavafx.domain.ShippingInformation;

public class EnterShippingInformation {
	private ShippingInformation shippingInformation;

	public EnterShippingInformation(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
	}

	public ShippingInformation get() {
		return shippingInformation;
	}

}
