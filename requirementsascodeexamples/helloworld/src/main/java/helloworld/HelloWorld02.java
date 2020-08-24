package helloworld;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

public class HelloWorld02 extends AbstractActor{
	private final Runnable greetsUser = this::greetUser;
	private final Condition lessThan3 = this::lessThan3;

	private int greetingsCounter = 0;

	@Override
	public Model behavior() {
		Model model = Model.builder()
			.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(greetsUser).reactWhile(lessThan3)
			.build();

		return model;
	}

	private void greetUser() {
		System.out.println("Hello, User! Nice to see you.");
		greetingsCounter++;
	}

	private boolean lessThan3() {
		return greetingsCounter < 3;
	}

	public static void main(String[] args) {
		HelloWorld02 actor = new HelloWorld02();
		actor.run();
	}
}