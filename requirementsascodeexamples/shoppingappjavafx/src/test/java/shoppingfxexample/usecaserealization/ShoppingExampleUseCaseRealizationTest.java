package shoppingfxexample.usecaserealization;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.SystemReactionTrigger;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;

import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.ShippingInformation;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.gui.ShoppingApplicationDisplayDouble;
import shoppingfxexample.usecase.event.AddProductToCart;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.ConfirmPurchase;
import shoppingfxexample.usecase.event.EnterShippingInformation;

public class ShoppingExampleUseCaseRealizationTest {
	private String runStepNames;
	private UseCaseRunner useCaseRunner;

	@Before
	public void setUp() throws Exception {
		runStepNames = "";
		useCaseRunner = new UseCaseRunner(trackRunStepNames());
		
		Stock stock = new Stock();
		ShoppingApplicationDisplayDouble shoppingApplicationDisplayDouble = new ShoppingApplicationDisplayDouble(useCaseRunner);
		new ShoppingExampleUseCaseRealization(useCaseRunner.useCaseModel(), stock, shoppingApplicationDisplayDouble);
	}
	private Consumer<SystemReactionTrigger> trackRunStepNames() {
		return systemReactionTrigger -> {
			runStepNames += trackStepName(systemReactionTrigger.useCaseStep());
			systemReactionTrigger.trigger();
		};
	}
	private String trackStepName(UseCaseStep useCaseStep) {
		String trackedStepName = "";
		if(useCaseStep.name().startsWith("S")){
			trackedStepName = useCaseStep.name() + ";";
		}
		return trackedStepName;
	}

	@Test
	public void runsBasicFlow() {
		useCaseRunner.run();
		useCaseRunner.reactTo(
			new AddProductToCart(new Product("Hamster Wheel, Black", new BigDecimal(9.95))),
			new CheckoutPurchase(),
			new EnterShippingInformation(new ShippingInformation()),
			new ConfirmPurchase());
		
		assertEquals("S1;S2;S3;S4;S5;S6;S7;S1;S2;", runStepNames);
	}
}
