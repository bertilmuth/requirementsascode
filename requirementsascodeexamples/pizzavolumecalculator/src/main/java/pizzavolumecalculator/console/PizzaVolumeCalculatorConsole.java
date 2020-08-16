package pizzavolumecalculator.console;

import java.util.Optional;
import java.util.Scanner;

import pizzavolumecalculator.actor.PizzaVolumeCalculator;
import pizzavolumecalculator.actor.command.CalculateVolume;
import pizzavolumecalculator.actor.command.EnterHeight;
import pizzavolumecalculator.actor.command.EnterRadius;


public class PizzaVolumeCalculatorConsole {
  private Scanner scanner;
  
  private PizzaVolumeCalculatorConsole(){
    this.scanner = new Scanner(System.in);
  }

  private void start() {
    PizzaVolumeCalculator calculator = new PizzaVolumeCalculator();

    System.out.print("Please enter the radius: ");
    calculator.reactTo(enterRadius());

    System.out.print("Please enter the height: ");
    calculator.reactTo(enterHeight());

    Optional<Double> volume = calculator.reactTo(new CalculateVolume());

    System.out.println("The volume is:  " + volume.get());
  }
  
  public static void main(String[] args) {
    PizzaVolumeCalculatorConsole console = new PizzaVolumeCalculatorConsole();
    console.start();
  }
  
  protected EnterRadius enterRadius() {
    int radius = scanner.nextInt();
    return new EnterRadius(radius);
  }
  
  protected EnterHeight enterHeight() {
    int height = scanner.nextInt();
    return new EnterHeight(height);
  }
}
