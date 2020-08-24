package helloworld;

import java.util.Scanner;
import java.util.function.Consumer;

import org.requirementsascode.Model;

import helloworld.command.EnterText;

public class HelloWorld04 {
  private static final Runnable askForName = HelloWorld04::askForName;
  private static final Consumer<EnterText> saveName = HelloWorld04::saveName;
  private static final Runnable askForAge = HelloWorld04::askForAge;
  private static final Consumer<EnterText> saveAge = HelloWorld04::saveAge;
  private static final Runnable greetUser = HelloWorld04::greetUser;

  private static String firstName;
  private static int age;

  public static void main(String[] args) {
    HelloWorldActor04 actor = new HelloWorldActor04(askForName, saveName, askForAge, saveAge, greetUser);
    actor.run();
    actor.reactTo(enterText());
    actor.reactTo(enterText());
  }

  private static void askForName() {
    System.out.print("Please enter your name: ");
  }

  private static EnterText enterText() {
    Scanner scanner = new Scanner(System.in);
    String text = scanner.next();
    scanner.close();
    return new EnterText(text);
  }

  private static void saveName(EnterText enterText) {
    firstName = enterText.text;
  }

  private static void askForAge() {
    System.out.print("Please enter your age: ");
  }

  private static void saveAge(EnterText enterText) {
    age = Integer.parseInt(enterText.text);
  }

  private static void greetUser() {
    System.out.println("Hello, " + firstName + " (" + age + ").");
  }
}

class HelloWorldActor04 extends AbstractHelloWorldExample {
  private final Runnable asksForName;
  private final Class<EnterText> entersName = EnterText.class;
  private final Consumer<EnterText> savesName;
  private final Runnable asksForAge;
  private final Class<EnterText> entersAge = EnterText.class;
  private final Consumer<EnterText> savesAge;
  private final Runnable greetsUser;

  public HelloWorldActor04(Runnable asksForName, Consumer<EnterText> savesName, Runnable asksForAge,
    Consumer<EnterText> savesAge, Runnable greetsUser) {
    this.asksForName = asksForName;
    this.savesName = savesName;
    this.asksForAge = asksForAge;
    this.savesAge = savesAge;
    this.greetsUser = greetsUser;
  }
  
  @Override
  public Model behavior() {
  	Model model = Model.builder()
  		.useCase("Get greeted")
  			.basicFlow()
  				.step("S1").system(asksForName)
  				.step("S2").user(entersName).system(savesName)
  				.step("S3").system(asksForAge)
  				.step("S4").user(entersAge).system(savesAge)
  				.step("S5").system(greetsUser)
  		.build();
  	
  	return model;
  }
}
