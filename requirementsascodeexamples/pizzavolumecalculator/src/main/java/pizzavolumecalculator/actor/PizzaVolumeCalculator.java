package pizzavolumecalculator.actor;

import static java.lang.Math.PI;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

import pizzavolumecalculator.actor.command.CalculateVolume;
import pizzavolumecalculator.actor.command.EnterHeight;
import pizzavolumecalculator.actor.command.EnterRadius;

public class PizzaVolumeCalculator extends AbstractActor{
  private int z;
  private int a;
  
  private Consumer<EnterRadius> saveRadius;
  private Consumer<EnterHeight> saveHeight;
  private Supplier<Double> calculateVolume;
  private Runnable throwIllegalRadiusException;
  private Runnable throwIllegalHeightException;
  private Condition isNegativeRadius;
  private Condition isNegativeHeight;

  public PizzaVolumeCalculator() {
    this.saveRadius = this::saveRadius;
    this.saveHeight = this::saveHeight;
    this.calculateVolume = this::calculateVolume;
    this.throwIllegalRadiusException = this::throwIllegalRadiusException;
    this.throwIllegalHeightException = this::throwIllegalHeightException;
    this.isNegativeRadius = this::isNegativeRadius;
    this.isNegativeHeight = this::isNegativeHeight;
  }
  
  @Override
  public Model behavior() {
		Model model = Model.builder()
			.useCase("Calculate Pizza Volume")
				.basicFlow()
					.step("S1").user(EnterRadius.class).system(saveRadius)
	        .step("S2").user(EnterHeight.class).system(saveHeight)
	        .step("S3").user(CalculateVolume.class).systemPublish(calculateVolume)
	        .step("S4").continuesAt("S1")
	      
	      .flow("Negative radius").after("S1").condition(isNegativeRadius)
	        .step("S1a_1").system(throwIllegalRadiusException)

	      .flow("Negative height").after("S2").condition(isNegativeHeight)
          .step("S2a_1").system(throwIllegalHeightException)
          
			  .build();
		
		return model;
  }
  
  // system reactions
  private void saveRadius(EnterRadius enterRadius) {
    this.z = enterRadius.getRadius();
  }
  
  private void saveHeight(EnterHeight enterHeight) {
    this.a = enterHeight.getHeight();
  }
  
  private Double calculateVolume() {
    return PI * z * z * a;
  }
  
  private void throwIllegalRadiusException() {
    throw new IllegalArgumentException("Please specify a non-negative radius!");
  }
  
  private void throwIllegalHeightException() {
    throw new IllegalArgumentException("Please specify a non-negative height!");
  }
  
  // conditions
  private boolean isNegativeRadius() {
    return z < 0;
  }
  
  private boolean isNegativeHeight() {
    return a < 0;
  }
  
  // convenience method
  public boolean canReactTo(Class<? extends Object> messageClass) {
    return getModelRunner().canReactTo(messageClass);
  }
}