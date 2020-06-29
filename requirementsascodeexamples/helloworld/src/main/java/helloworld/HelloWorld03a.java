package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Actor;
import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.builder.ModelBuilder;

import helloworld.usercommand.EnterText;

public class HelloWorld03a extends AbstractHelloWorldExample {
	private final Runnable asksForName = this::askForName;
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> greetsUser = this::greetUser;

	private Actor validUser;
	private Actor invalidUser;

	public Model buildModel() {
		ModelBuilder builder = Model.builder();
		validUser = new Actor("Valid User");
		invalidUser = new Actor("Invalid User");

		Model model = builder
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
		HelloWorld03a example = new HelloWorld03a();
		example.start();
	}

	private void start() {
		Model model = buildModel();
		ModelRunner modelRunner = new ModelRunner().run(model);

		// The next command will not be handled, because the actor is wrong
		modelRunner.as(invalidUser).reactTo(new EnterText("Ignored Command"));

		// This command will be handled
		modelRunner.as(validUser).reactTo(entersText());
	}

	public Actor validUser() {
		return validUser;
	}

	public Actor invalidUser() {
		return invalidUser;
	}
}
