package shoppingapp.boundary.internal.domain;

public class PurchaseOrder extends ProductContainer {
	private ShippingInformation shippingInformation;
	private PaymentDetails paymentDetails;
	
	public PurchaseOrder() {
		super();
		shippingInformation = new ShippingInformation();
		paymentDetails = new PaymentDetails();
	}

	public ShippingInformation getShippingInformation() {
		return shippingInformation;
	}
	
	public PaymentDetails paymentDetails() {
		return paymentDetails;
	}

	public void saveShippingInformation(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
	}

	public void savePaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}
}
