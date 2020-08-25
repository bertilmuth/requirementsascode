package helloworld.domain;

public class Color {
  private String inputColor = "green";
  private String outputColor;
  
  public boolean isInputColorRed() {
    return inputColor.equals("red");
  }

  public boolean isInputColorYellow() {
    return inputColor.equals("yellow");
  }

  public boolean isInputColorGreen() {
    return inputColor.equals("green");
  }

  public void setOutputColorToRed() {
    outputColor = "red";
  }

  public void setOutputColorToYellow() {
    outputColor = "yellow";
  }

  public void setOutputColorToGreen() {
    outputColor = "green";
  }

  public String getInputColor() {
    return inputColor;
  }

  public void setInputColor(String inputColor) {
    this.inputColor = inputColor;
  }

  public String getOutputColor() {
    return outputColor;
  }
}
