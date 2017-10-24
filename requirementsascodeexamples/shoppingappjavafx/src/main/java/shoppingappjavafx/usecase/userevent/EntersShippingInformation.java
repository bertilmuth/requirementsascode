package shoppingappjavafx.usecase.userevent;

import shoppingappjavafx.domain.ShippingInformation;

public class EntersShippingInformation {
	private ShippingInformation shippingInformation;

	public EntersShippingInformation(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
	}

	public ShippingInformation get() {
		return shippingInformation;
	}

}
