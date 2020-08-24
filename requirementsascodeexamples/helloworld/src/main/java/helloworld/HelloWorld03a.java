package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import helloworld.actor.User;
import helloworld.command.EnterText;

public class HelloWorld03a {
  public static void main(String[] args) {
    HelloWorldActor03a helloWorldActor = new HelloWorldActor03a(HelloWorld03a::greetUser);
    User validUser03a = new User(helloWorldActor);
    helloWorldActor.setValidUser(validUser03a);
    validUser03a.run();
  }
  
  private static void greetUser(EnterText enterText) {
    System.out.println("Hello, " + enterText.text + ".");
  }
}

class HelloWorldActor03a extends AbstractActor{
  private AbstractActor validUser;
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> greetsUser;

	
  public HelloWorldActor03a(Consumer<EnterText> greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
	public Model behavior() {
		Model model = Model.builder()
			.useCase("Get greeted").as(validUser)
				.basicFlow()
					.step("S1b").user(entersName).system(greetsUser)
			.build();
		
		return model;
	}
  
  public void setValidUser(AbstractActor validUser) {
    this.validUser = validUser;
  }
}
