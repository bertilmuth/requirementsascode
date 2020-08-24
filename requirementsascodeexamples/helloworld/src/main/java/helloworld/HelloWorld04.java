package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import helloworld.command.EnterText;

public class HelloWorld04 {
  private static final Consumer<EnterText> saveName = HelloWorld04::saveName;
  private static final Consumer<EnterText> saveAge = HelloWorld04::saveAge;
  private static final Runnable greetUser = HelloWorld04::greetUser;

  private static String firstName;
  private static int age;

  public static void main(String[] args) {
    HelloWorldActor04 actor = new HelloWorldActor04(saveName, saveAge, greetUser);
    actor.run();
    actor.reactTo(new EnterText("John Q. Public"));
    actor.reactTo(new EnterText("43"));
  }

  private static void saveName(EnterText enterText) {
    firstName = enterText.text;
  }

  private static void saveAge(EnterText enterText) {
    age = Integer.parseInt(enterText.text);
  }

  private static void greetUser() {
    System.out.println("Hello, " + firstName + " (" + age + ").");
  }
}

class HelloWorldActor04 extends AbstractActor{
  private final Class<EnterText> entersName = EnterText.class;
  private final Consumer<EnterText> savesName;
  private final Class<EnterText> entersAge = EnterText.class;
  private final Consumer<EnterText> savesAge;
  private final Runnable greetsUser;

  public HelloWorldActor04(Consumer<EnterText> savesName, Consumer<EnterText> savesAge, Runnable greetsUser) {
    this.savesName = savesName;
    this.savesAge = savesAge;
    this.greetsUser = greetsUser;
  }
  
  @Override
  public Model behavior() {
  	Model model = Model.builder()
  		.useCase("Get greeted")
  			.basicFlow()
  				.step("S1").user(entersName).system(savesName)
  				.step("S2").user(entersAge).system(savesAge)
  				.step("S3").system(greetsUser)
  		.build();
  	
  	return model;
  }
}
