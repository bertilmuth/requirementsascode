package helloworld;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

public class HelloWorld07{
  public String inputColor = "green";
  public String outputColor;
  
  public Model buildModel() {
    Model model = Model.builder()
      .useCase("Handle colors")
        .basicFlow()
          .step("S1").inCase(this::isColorRed).system(this::setColorToRed)
          .step("S2").inCase(this::isColorYellow).system(this::setColorToYellow)
          .step("S3").inCase(this::isColorGreen).system(this::setColorToGreen)
          .step("S4").system(this::displayColor)
      .build();
    
    return model;
  }
  
  private boolean isColorRed() {
    return inputColor.equals("red");
  }
  
  private boolean isColorYellow() {
    return inputColor.equals("yellow");
  }
  
  private boolean isColorGreen() {
    return inputColor.equals("green");
  }
  
  private void setColorToRed() {
    outputColor = "red";
  }
  
  private void setColorToYellow() {
    outputColor = "yellow";
  }
  
  private void setColorToGreen() {
    outputColor = "green";
  }
  
  private void displayColor() {
    System.out.println("The color is: " + outputColor);
  }
  
  public static void main(String[] args) {
    HelloWorld07 example = new HelloWorld07();
    example.start();
  }

  private void start() {
    Model model = buildModel();
    new ModelRunner().run(model);
  }
}
