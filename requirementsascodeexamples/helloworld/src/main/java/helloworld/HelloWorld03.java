package helloworld;

import java.util.Scanner;
import java.util.function.Consumer;

import org.requirementsascode.Model;

import helloworld.usercommand.EnterText;

public class HelloWorld03{
  public static void main(String[] args) {
    HelloWorldActor03 actor = new HelloWorldActor03(HelloWorld03::askForName, HelloWorld03::greetUser);
    actor.run();
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
  
  private static void greetUser(EnterText enterText) {
    System.out.println("Hello, " + enterText.text + ".");
  }
}

class HelloWorldActor03 extends AbstractHelloWorldExample { 
	private final Runnable asksForName;
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> greetsUser;
	
  public HelloWorldActor03(Runnable asksForName, Consumer<EnterText> greetsUser) {
    this.asksForName = asksForName;
    this.greetsUser = greetsUser;
  }

	@Override
	public Model behavior() {
		Model model = Model.builder()
			.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(asksForName)
					.step("S2").user(entersName).system(greetsUser)
			.build();
		
		return model;
	}
}
