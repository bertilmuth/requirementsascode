package pizzavolumecalculator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.AbstractActor;

import pizzavolumecalculator.command.CalculateVolume;
import pizzavolumecalculator.command.EnterHeight;
import pizzavolumecalculator.command.EnterRadius;

public class PizzaVolumeCalculatorTest {
	private AbstractActor calculator;
	
	@Before
	public void setup() {
	   calculator = new PizzaVolumeCalculator();
	   calculator.getModelRunner().startRecording();
	}

	@Test
	public void testBasicFlow() {
	  calculator.reactTo(new EnterRadius(4));
	  calculator.reactTo(new EnterHeight(5));
	  Optional<Double> pizzaVolume = calculator.reactTo(new CalculateVolume());
		assertRecordedStepNames("S1", "S2", "S3");
		
		assertEquals(251.327, pizzaVolume.get(), 0.0009);
	}

	protected void assertRecordedStepNames(String... expectedStepNames) {
		String[] actualStepNames = calculator.getModelRunner().getRecordedStepNames();
		assertArrayEquals(expectedStepNames, actualStepNames);
	}
}
