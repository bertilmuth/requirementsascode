package helloworld;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

public class HelloWorld01 {
	private final Runnable greetsUser = this::greetUser;

	public Model buildModel() {
		Model model = Model.builder()
			.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(greetsUser)
			.build();
		
		return model;
	}

	private void greetUser() {
		System.out.println("Hello, User.");
	}

	public static void main(String[] args) {
		HelloWorld01 example = new HelloWorld01();
		example.start();
	}

	private void start() {
		Model model = buildModel();
		new ModelRunner().run(model);
	}
}