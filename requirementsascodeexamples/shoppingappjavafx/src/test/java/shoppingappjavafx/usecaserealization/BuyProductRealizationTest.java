package shoppingappjavafx.usecaserealization;

import static org.junit.Assert.assertArrayEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.TestModelRunner;

import shoppingappjavafx.domain.PaymentDetails;
import shoppingappjavafx.domain.Product;
import shoppingappjavafx.domain.ShippingInformation;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecase.ShoppingAppModel;
import shoppingappjavafx.usecase.userevent.AddsProductToCart;
import shoppingappjavafx.usecase.userevent.ConfirmsPurchase;
import shoppingappjavafx.usecase.userevent.EntersPaymentDetails;
import shoppingappjavafx.usecase.userevent.EntersShippingInformation;
import shoppingappjavafx.usecase.userevent.StartsCheckoutProcess;
import shoppingappjavafx.usecaserealization.componentinterface.Display;
import shoppingappjavafx.usecaserealization.stubs.DisplayStub;

public class BuyProductRealizationTest {
	private TestModelRunner modelRunner;
	private Model model;

	@Before
	public void setUp() throws Exception {
		modelRunner = new TestModelRunner();
		ModelBuilder modelBuilder = Model.builder();
		
		Stock stock = new Stock();
		Display displayStub = new DisplayStub();
		
		BuyProductRealization shoppingAppUseCaseRealization =
			new BuyProductRealization(stock, displayStub);
		model = 
			new ShoppingAppModel(shoppingAppUseCaseRealization).buildWith(modelBuilder);
	}

	@Test
	public void runsBasicFlow() {
		modelRunner.run(model);
		modelRunner.reactTo(
			new AddsProductToCart(new Product("Hamster Wheel, Black", new BigDecimal(9.95))),
			new StartsCheckoutProcess(),
			new EntersShippingInformation(new ShippingInformation()),
			new EntersPaymentDetails(new PaymentDetails()),
			new ConfirmsPurchase());
		
		assertRecordedStepNames("S1", "S2", "S3", "S4", "S5",
			"S6", "S7", "S8", "S9", "S10", "S11", "S1", "S2");
	}
	
    protected void assertRecordedStepNames(String... expectedStepNames) {
	String[] actualStepNames = modelRunner.getRecordedStepNames();
	assertArrayEquals(expectedStepNames, actualStepNames);
    }
}
