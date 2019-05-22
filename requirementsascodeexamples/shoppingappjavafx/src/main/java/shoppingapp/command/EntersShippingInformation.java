package shoppingapp.command;

import shoppingapp.boundary.internal.domain.ShippingInformation;

public class EntersShippingInformation {
	private ShippingInformation shippingInformation;

	public EntersShippingInformation(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
	}

	public ShippingInformation get() {
		return shippingInformation;
	}

}
