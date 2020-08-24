package helloworld;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

public class HelloWorld01 extends AbstractActor{
	private final Runnable greetsUser = this::greetUser;

	@Override
	public Model behavior() {
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
		HelloWorld01 actor = new HelloWorld01();
		actor.run();
	}
}