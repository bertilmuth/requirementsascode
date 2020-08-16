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
    PizzaVolumeCalculator actor = new PizzaVolumeCalculator();
    actor.run();
    Optional<Double> result = Optional.empty();
    
    do {
      System.out.print("Please enter the radius: ");
      actor.reactTo(enterRadius());

      System.out.print("Please enter the height: ");
      actor.reactTo(enterHeight());

      result = actor.reactTo(new CalculateVolume());
    } while (!result.isPresent());
    
    System.out.println("The volume is:  " + result.get());
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
