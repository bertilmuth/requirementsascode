package pizzavolumecalculator.actor;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.requirementsascode.AbstractActor;

import pizzavolumecalculator.actor.command.CalculateVolume;
import pizzavolumecalculator.actor.command.EnterHeight;
import pizzavolumecalculator.actor.command.EnterRadius;

public class PizzaVolumeCalculatorTest {
  private AbstractActor calculator;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

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
		assertRecordedStepNames("S1", "S2", "S3", "S4");
		
		assertEquals(251.327, pizzaVolume.get(), 0.01);
	}
	
  @Test
  public void testBasicFlowTwice() {
    calculator.reactTo(new EnterRadius(2));
    calculator.reactTo(new EnterHeight(3));
    Optional<Double> pizzaVolume = calculator.reactTo(new CalculateVolume());
    
    assertEquals(37.69, pizzaVolume.get(), 0.01);

    calculator.reactTo(new EnterRadius(4));
    calculator.reactTo(new EnterHeight(5));
    pizzaVolume = calculator.reactTo(new CalculateVolume());
    assertRecordedStepNames("S1", "S2", "S3", "S4", "S1", "S2", "S3", "S4");

    assertEquals(251.32, pizzaVolume.get(), 0.01);
  }

  @Test
  public void testIllegalRadius() {
    thrown.expect(IllegalArgumentException.class);
    calculator.reactTo(new EnterRadius(-1));
  }

  @Test
  public void testIllegalHeight() {
    thrown.expect(IllegalArgumentException.class);
    calculator.reactTo(new EnterRadius(5));
    calculator.reactTo(new EnterHeight(-1));
  }

  protected void assertRecordedStepNames(String... expectedStepNames) {
    String[] actualStepNames = calculator.getModelRunner().getRecordedStepNames();
    assertArrayEquals(expectedStepNames, actualStepNames);
  }
}
