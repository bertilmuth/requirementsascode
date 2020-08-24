package helloworld;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

public class HelloWorld07{
  public static final Condition isColorRed = HelloWorld07::isColorRed;
  public static final Condition isColorYellow = HelloWorld07::isColorYellow;
  public static final Condition isColorGreen = HelloWorld07::isColorGreen;
  public static final Runnable setColorToRed = HelloWorld07::setColorToRed;
  public static final Runnable setColorToYellow = HelloWorld07::setColorToYellow;
  public static final Runnable setColorToGreen = HelloWorld07::setColorToGreen;
  public static final Runnable displayColor = HelloWorld07::displayColor;
  
  public static String inputColor = "green";
  public static String outputColor;
  
  private static boolean isColorRed() {
    return inputColor.equals("red");
  }

  private static boolean isColorYellow() {
    return inputColor.equals("yellow");
  }

  private static boolean isColorGreen() {
    return inputColor.equals("green");
  }

  private static void setColorToRed() {
    outputColor = "red";
  }

  private static void setColorToYellow() {
    outputColor = "yellow";
  }

  private static void setColorToGreen() {
    outputColor = "green";
  }

  private static void displayColor() {
    System.out.println("The color is: " + outputColor);
  }

  public static void main(String[] args) {
    HelloWorldActor07 actor = new HelloWorldActor07(isColorRed, isColorYellow, isColorGreen, setColorToRed,
      setColorToYellow, setColorToGreen, displayColor);
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
