package shoppingfxexample.usecase.event;

import shoppingfxexample.domain.ShippingInformation;

public class EnterShippingInformationEvent {
	private ShippingInformation shippingInformation;

	public EnterShippingInformationEvent(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
	}

	public ShippingInformation getShippingInformation() {
		return shippingInformation;
	}

}
