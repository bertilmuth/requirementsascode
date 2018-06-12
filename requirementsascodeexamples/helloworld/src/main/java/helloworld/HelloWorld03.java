package helloworld;

import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.ModelRunner;

import helloworld.userevent.EntersText;

public class HelloWorld03 extends AbstractHelloWorldExample{
	
	private static final Class<EntersText> ENTERS_FIRST_NAME = EntersText.class;

	public Model buildWith(ModelBuilder modelBuilder) {
		Model model = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
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
	
	public static void main(String[] args){		
		HelloWorld03 example = new HelloWorld03();
		example.start();
	}

	private void start() {
		Model model = buildWith(Model.builder());
		ModelRunner modelRunner = new ModelRunner();
		modelRunner.run(model).reactTo(entersText());
	}
}
