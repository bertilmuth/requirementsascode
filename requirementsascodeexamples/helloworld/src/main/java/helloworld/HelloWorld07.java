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
  private final Condition isInputColorRed;
  private final Condition isInputColorYellow;
  private final Condition isInputColorGreen;
  private final Runnable displayColor;
  private final Runnable setOutputColorToRed;
  private final Runnable setOutputColorToYellow;
  private final Runnable setOutputColorToGreen;

  public HelloWorldActor07(Condition isInputColorRed, Condition isInputColorYellow, Condition isInputColorGreen,
    Runnable setOutputColorToRed, Runnable setOutputColorToYellow, Runnable setOutputColorToGreen, Runnable displayColor) {
    this.isInputColorRed = isInputColorRed;
    this.isInputColorYellow = isInputColorYellow;
    this.isInputColorGreen = isInputColorGreen;
    this.setOutputColorToRed = setOutputColorToRed;
    this.setOutputColorToYellow = setOutputColorToYellow;
    this.setOutputColorToGreen = setOutputColorToGreen;
    this.displayColor = displayColor;
  }
  
  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Handle colors")
        .basicFlow()
          .step("S1").inCase(isInputColorRed).system(setOutputColorToRed)
          .step("S2").inCase(isInputColorYellow).system(setOutputColorToYellow)
          .step("S3").inCase(isInputColorGreen).system(setOutputColorToGreen)
          .step("S4").system(displayColor)
        .build();

    return model;
  }
}
