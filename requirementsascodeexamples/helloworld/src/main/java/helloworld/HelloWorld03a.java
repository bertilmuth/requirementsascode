package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Actor;
import org.requirementsascode.Model;

import helloworld.usercommand.EnterText;

public class HelloWorld03a extends AbstractHelloWorldExample {
	private final Runnable asksForName = this::askForName;
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> greetsUser = this::greetUser;

	private Actor validUser;
	private Actor invalidUser;
	
  public HelloWorld03a() {
    validUser = new Actor("Valid User");
    invalidUser = new Actor("Invalid User");
  }

	@Override
	public Model behavior() {
		Model model = Model.builder()
			.useCase("Get greeted").as(validUser)
				.basicFlow()
					.step("S1").system(asksForName)
					.step("S2").user(entersName).system(greetsUser)
			.build();
		
		return model;
	}

	private void askForName() {
		System.out.print("Please enter your name: ");
	}

	private void greetUser(EnterText enterText) {
		System.out.println("Hello, " + enterText.text + ".");
	}

	public static void main(String[] args) {
		HelloWorld03a actor = new HelloWorld03a();
		actor.react();
	}

	private void react() {
	  run(); 

		// The next command will not be handled, because the actor is wrong
		getModelRunner().as(invalidUser).reactTo(new EnterText("Ignored Command"));

		// This command will be handled
		getModelRunner().as(validUser).reactTo(entersText());
	}

	public Actor validUser() {
		return validUser;
	}

	public Actor invalidUser() {
		return invalidUser;
	}
}
