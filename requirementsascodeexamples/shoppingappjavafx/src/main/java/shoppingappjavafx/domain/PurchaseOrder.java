package shoppingappjavafx.domain;

public class PurchaseOrder extends ProductContainer {
	private ShippingInformation shippingInformation;
	private PaymentDetails paymentDetails;
	
	public PurchaseOrder() {
		super();
	}

	public ShippingInformation shippingInformation() {
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
