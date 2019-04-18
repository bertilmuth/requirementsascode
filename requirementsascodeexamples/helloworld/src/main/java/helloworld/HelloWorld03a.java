package helloworld;

import org.requirementsascode.Actor;
import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.ModelRunner;

import helloworld.userevent.EntersText;

public class HelloWorld03a extends AbstractHelloWorldExample {

	private static final Class<EntersText> ENTERS_FIRST_NAME = EntersText.class;
	private Actor validUser;
	private Actor invalidUser;

    public Model buildWith(ModelBuilder modelBuilder) {
    	validUser = modelBuilder.actor("Valid User");
    	invalidUser = modelBuilder.actor("Invalid User");
    	
		Model model = modelBuilder.useCase("Get greeted")
			.as(validUser).basicFlow()
				.step("S1").system(this::promptsUserToEnterFirstName)
				.step("S2").user(ENTERS_FIRST_NAME).system(this::greetsUserWithFirstName)
			.build();
		return model;
    }

	private void promptsUserToEnterFirstName() {
		System.out.print("Please enter your first name: ");
	}

	private void greetsUserWithFirstName(EntersText enterText) {
		System.out.println("Hello, " + enterText.text + ".");
	}

	public static void main(String[] args) {
		HelloWorld03a example = new HelloWorld03a();
		example.start();
	}

	private void start() {
		Model model = buildWith(Model.builder());
		ModelRunner modelRunner = new ModelRunner().run(model);
		
		// The next event will not be handled, because the actor is wrong
		modelRunner.as(invalidUser).reactTo(new EntersText("Ignored Event"));
		
		// This event will be handled
		modelRunner.as(validUser).reactTo(entersText());
	}

	public Actor validUser() {
		return validUser;
	}

	public Actor invalidUser() {
		return invalidUser;
	}
}
