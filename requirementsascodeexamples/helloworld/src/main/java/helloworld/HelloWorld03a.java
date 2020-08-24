package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Actor;
import org.requirementsascode.Model;

import helloworld.command.EnterText;

public class HelloWorld03a {
  public static void main(String[] args) {
    HelloWorldActor03a actor = new HelloWorldActor03a(HelloWorld03a.validUser(), HelloWorld03a::greetUser);
    sendMessages(actor);
  }

  private static void sendMessages(AbstractActor actor) {
    actor.run();

    // The next command will not be handled, because the actor is wrong
    actor.getModelRunner().as(invalidUser()).reactTo(new EnterText("Ignored Command"));

    // This command will be handled
    actor.getModelRunner().as(validUser()).reactTo("John Q. Public");
  }
  
  private static void greetUser(EnterText enterText) {
    System.out.println("Hello, " + enterText.text + ".");
  }

  public static Actor validUser() {
    return new Actor("Valid User");
  }

  public static Actor invalidUser() {
    return new Actor("Invalid User");
  }
}

class HelloWorldActor03a extends AbstractHelloWorldExample {
  private AbstractActor validUser;
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> greetsUser;

	
  public HelloWorldActor03a(AbstractActor validUser, Consumer<EnterText> greetsUser) {
    this.validUser = validUser;
    this.greetsUser = greetsUser;
  }

	@Override
	public Model behavior() {
		Model model = Model.builder()
			.useCase("Get greeted").as(validUser)
				.basicFlow()
					.step("S1").user(entersName).system(greetsUser)
			.build();
		
		return model;
	}
}
