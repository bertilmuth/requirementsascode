package shoppingapp.boundary;

import static org.junit.Assert.assertArrayEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import shoppingapp.boundary.Boundary;
import shoppingapp.boundary.driven_port.Display;
import shoppingapp.boundary.internal.domain.PaymentDetails;
import shoppingapp.boundary.internal.domain.Product;
import shoppingapp.boundary.internal.domain.ShippingInformation;
import shoppingapp.boundary.internal.domain.Stock;
import shoppingapp.boundary.stubs.DisplayStub;
import shoppingapp.command.AddsProductToCart;
import shoppingapp.command.ConfirmsPurchase;
import shoppingapp.command.EntersPaymentDetails;
import shoppingapp.command.EntersShippingInformation;
import shoppingapp.command.ChecksOutPurchase;

public class BoundaryTest {
	private Boundary boundary;

	@Before
	public void setUp() throws Exception {
		Stock stock = new Stock();
		Display displayStub = new DisplayStub();
		boundary = new Boundary(stock, displayStub);
		boundary.run();
	}

	@Test
	public void runsBasicFlow() {
		boundary.reactTo(new AddsProductToCart(new Product("Hamster Wheel, Black", new BigDecimal(9.95))),
				new ChecksOutPurchase(), new EntersShippingInformation(new ShippingInformation()),
				new EntersPaymentDetails(new PaymentDetails()), new ConfirmsPurchase());

		assertRecordedStepNames("S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8", "S9", "S10", "S11", "S1", "S2");
	}

	protected void assertRecordedStepNames(String... expectedStepNames) {
		String[] actualStepNames = boundary.getRecordedStepNames();
		assertArrayEquals(expectedStepNames, actualStepNames);
	}
}
