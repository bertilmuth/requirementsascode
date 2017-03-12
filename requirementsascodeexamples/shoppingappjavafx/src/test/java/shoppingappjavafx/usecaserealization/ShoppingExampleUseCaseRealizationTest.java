package shoppingappjavafx.usecaserealization;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.TestUseCaseRunner;
import org.requirementsascode.UseCaseModel;

import shoppingappjavafx.domain.PaymentDetails;
import shoppingappjavafx.domain.Product;
import shoppingappjavafx.domain.ShippingInformation;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecase.ShoppingAppUseCaseModelCreator;
import shoppingappjavafx.usecase.event.AddProductToCart;
import shoppingappjavafx.usecase.event.CheckoutPurchase;
import shoppingappjavafx.usecase.event.ConfirmPurchase;
import shoppingappjavafx.usecase.event.EnterPaymentDetails;
import shoppingappjavafx.usecase.event.EnterShippingInformation;
import shoppingappjavafx.usecaserealization.stubs.ShoppingApplicationDisplayStub;

public class ShoppingExampleUseCaseRealizationTest {
	private TestUseCaseRunner useCaseRunner;
	private UseCaseModel useCaseModel;

	@Before
	public void setUp() throws Exception {
		useCaseRunner = new TestUseCaseRunner();
		useCaseModel = useCaseRunner.useCaseModel();
		
		Stock stock = new Stock();
		IShoppingAppDisplay shoppingApplicationDisplayStub = new ShoppingApplicationDisplayStub();
		
		ShoppingAppUseCaseRealization shoppingAppUseCaseRealization =
			new ShoppingAppUseCaseRealization(stock, shoppingApplicationDisplayStub);
		new ShoppingAppUseCaseModelCreator(shoppingAppUseCaseRealization).create(useCaseModel);
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
