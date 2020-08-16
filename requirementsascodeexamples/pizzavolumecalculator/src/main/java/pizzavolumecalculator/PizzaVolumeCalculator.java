package pizzavolumecalculator;

import static java.lang.Math.PI;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import pizzavolumecalculator.command.CalculateVolume;
import pizzavolumecalculator.command.EnterHeight;
import pizzavolumecalculator.command.EnterRadius;

public class PizzaVolumeCalculator extends AbstractActor{
  private Supplier<Double> calculateVolume;
  private int z;
  private int a;
  private Consumer<EnterRadius> saveRadius;
  private Consumer<EnterHeight> saveHeight;

  public PizzaVolumeCalculator() {
    this.saveRadius = this::saveRadius;
    this.saveHeight = this::saveHeight;
    this.calculateVolume = this::calculateVolume;
  }
  
  @Override
  public Model behavior() {
		Model model = Model.builder()
			.useCase("Calculate Pizza Volume")
				.basicFlow()
					.step("S1").user(EnterRadius.class).system(saveRadius)
	        .step("S2").user(EnterHeight.class).system(saveHeight)
	        .step("S3").user(CalculateVolume.class).systemPublish(calculateVolume)
			.build();
		
		return model;
  }
  
  private void saveRadius(EnterRadius enterRadius) {
    this.z = enterRadius.getRadius();
  }
  
  private void saveHeight(EnterHeight enterHeight) {
    this.a = enterHeight.getHeight();
  }
  
  private Double calculateVolume() {
    return PI * z * z * a;
  }
}