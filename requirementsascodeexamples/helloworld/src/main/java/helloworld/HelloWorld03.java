package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Model;

import helloworld.usercommand.EnterText;

public class HelloWorld03 extends AbstractHelloWorldExample { 
	private final Runnable asksForName = this::askForName;
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> greetsUser = this::greetUser;

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

	private void askForName() {
		System.out.print("Please enter your name: ");
	}

	private void greetUser(EnterText enterText) {
		System.out.println("Hello, " + enterText.text + ".");
	}

	public static void main(String[] args) {
		HelloWorld03 actor = new HelloWorld03();
		actor.react();
	}

	private void react() {
	  run();
		reactTo(entersText());
	}
}
