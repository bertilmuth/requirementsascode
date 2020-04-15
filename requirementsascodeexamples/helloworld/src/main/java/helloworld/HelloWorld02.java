package helloworld;

import org.requirementsascode.Condition;
import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

public class HelloWorld02 {
	private final Runnable greetsUser = this::greetUser;
	private final Condition lessThan3 = this::lessThan3;

	private int greetingsCounter = 0;

	public Model buildModel() {
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
		HelloWorld02 example = new HelloWorld02();
		example.start();
	}

	private void start() {
		Model model = buildModel();
		new ModelRunner().run(model);
	}
}