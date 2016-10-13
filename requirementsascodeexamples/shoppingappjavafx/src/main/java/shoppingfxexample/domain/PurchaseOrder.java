package shoppingfxexample.domain;

public class PurchaseOrder extends ProductContainer {
	private ShippingInformation shippingInformation;
	
	public PurchaseOrder() {
		super();
	}

	public ShippingInformation getShippingInformation() {
		return shippingInformation;
	}

	public void setShippingInformation(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
	}
}
