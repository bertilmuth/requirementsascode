package helloworld;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

public class HelloWorld02{
  public static void main(String[] args) {
    HelloWorldActor02 actor = new HelloWorldActor02(HelloWorld02::greetUser);
    actor.run();
  }
  
  private static void greetUser() {
    System.out.println("Hello, User.");
  }
}

class HelloWorldActor02 extends AbstractActor{
	private final Runnable greetsUser;
	private final Condition lessThan3 = this::lessThan3;

	private int greetingsCounter = 0;

  public HelloWorldActor02(Runnable greetsUser) {
    this.greetsUser = greetsUser;
  }

	@Override
	public Model behavior() {
		Model model = Model.builder()
			.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(greetsUser).reactWhile(lessThan3)
			.build();

		return model;
	}

	private boolean lessThan3() {
		return greetingsCounter++ < 3;
	}
}