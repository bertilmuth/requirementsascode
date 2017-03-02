package shoppingfxexample.usecaserealization;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.test.TestUseCaseRunner;

import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.ShippingInformation;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.gui.ShoppingApplicationDisplayDouble;
import shoppingfxexample.usecase.event.AddProductToCart;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.ConfirmPurchase;
import shoppingfxexample.usecase.event.EnterShippingInformation;

public class ShoppingExampleUseCaseRealizationTest {
	private TestUseCaseRunner runner;

	@Before
	public void setUp() throws Exception {
		runner = new TestUseCaseRunner();
		
		Stock stock = new Stock();
		ShoppingApplicationDisplayDouble shoppingApplicationDisplayDouble = new ShoppingApplicationDisplayDouble(runner);
		new ShoppingExampleUseCaseRealization(runner.useCaseModel(), stock, shoppingApplicationDisplayDouble);
	}

	@Test
	public void runsBasicFlow() {
		runner.run();
		runner.reactTo(
			new AddProductToCart(new Product("Hamster Wheel, Black", new BigDecimal(9.95))),
			new CheckoutPurchase(),
			new EnterShippingInformation(new ShippingInformation()),
			new ConfirmPurchase());
		
		assertEquals("S1;S2;S3;S4;S5;S6;S7;S1;S2;", runner.runStepNames());
	}
}
