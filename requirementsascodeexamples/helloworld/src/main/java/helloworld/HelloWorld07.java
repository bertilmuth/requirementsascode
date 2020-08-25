package helloworld;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

import helloworld.domain.Color;

public class HelloWorld07 {
  public static void main(String[] args) {
    Color color = new Color();
    HelloWorldActor07 actor = new HelloWorldActor07(color::isInputColorRed, color::isInputColorYellow,
      color::isInputColorGreen, color::setOutputColorToRed, color::setOutputColorToYellow, color::setOutputColorToGreen,
      () -> System.out.println("The output color is: " + color.getOutputColor()));
    actor.run();
  }
}

class HelloWorldActor07 extends AbstractActor {
  private final Condition isColorRed;
  private final Condition isColorYellow;
  private final Condition isColorGreen;
  private final Runnable displayColor;
  private final Runnable setColorToRed;
  private final Runnable setColorToYellow;
  private final Runnable setColorToGreen;

  public HelloWorldActor07(Condition isColorRed, Condition isColorYellow, Condition isColorGreen,
    Runnable setColorToRed, Runnable setColorToYellow, Runnable setColorToGreen, Runnable displayColor) {
    this.isColorRed = isColorRed;
    this.isColorYellow = isColorYellow;
    this.isColorGreen = isColorGreen;
    this.setColorToRed = setColorToRed;
    this.setColorToYellow = setColorToYellow;
    this.setColorToGreen = setColorToGreen;
    this.displayColor = displayColor;
  }
  
  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Handle colors")
        .basicFlow()
          .step("S1").inCase(isColorRed).system(setColorToRed)
          .step("S2").inCase(isColorYellow).system(setColorToYellow)
          .step("S3").inCase(isColorGreen).system(setColorToGreen)
          .step("S4").system(displayColor)
        .build();

    return model;
  }
}
