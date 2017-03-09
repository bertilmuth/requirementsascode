package shoppingfxexample.usecaserealization;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.TestUseCaseRunner;
import org.requirementsascode.UseCaseModel;

import shoppingfxexample.domain.PaymentDetails;
import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.ShippingInformation;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.gui.ShoppingApplicationDisplayDouble;
import shoppingfxexample.usecase.event.AddProductToCart;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.ConfirmPurchase;
import shoppingfxexample.usecase.event.EnterPaymentDetails;
import shoppingfxexample.usecase.event.EnterShippingInformation;

public class ShoppingExampleUseCaseRealizationTest {
	private TestUseCaseRunner useCaseRunner;
	private UseCaseModel useCaseModel;

	@Before
	public void setUp() throws Exception {
		useCaseRunner = new TestUseCaseRunner();
		useCaseModel = useCaseRunner.useCaseModel();
		
		Stock stock = new Stock();
		ShoppingApplicationDisplayDouble shoppingApplicationDisplayDouble = new ShoppingApplicationDisplayDouble(useCaseRunner);
		new ShoppingExampleUseCaseRealization(stock, shoppingApplicationDisplayDouble).create(useCaseModel);
	}

	@Test
	public void runsBasicFlow() {
		useCaseRunner.run();
		useCaseRunner.reactTo(
			new AddProductToCart(new Product("Hamster Wheel, Black", new BigDecimal(9.95))),
			new CheckoutPurchase(),
			new EnterShippingInformation(new ShippingInformation()),
			new EnterPaymentDetails(new PaymentDetails()),
			new ConfirmPurchase());
		
		assertEquals("S1;S2;S3;S4;S5;S6;S7;S8;S9;S10;S11;S1;S2;", useCaseRunner.runStepNames());
	}
}
