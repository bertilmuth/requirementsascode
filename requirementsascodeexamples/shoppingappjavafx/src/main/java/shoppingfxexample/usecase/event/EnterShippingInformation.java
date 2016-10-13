package shoppingfxexample.usecase.event;

import shoppingfxexample.domain.ShippingInformation;

public class EnterShippingInformation {
	private ShippingInformation shippingInformation;

	public EnterShippingInformation(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
	}

	public ShippingInformation getShippingInformation() {
		return shippingInformation;
	}

}
