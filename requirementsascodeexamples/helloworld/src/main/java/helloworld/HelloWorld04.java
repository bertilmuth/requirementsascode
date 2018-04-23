package helloworld;

import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.ModelRunner;

import helloworld.userevent.EntersText;

public class HelloWorld04 extends AbstractHelloWorldExample{
	private static final Class<EntersText> ENTERS_FIRST_NAME = EntersText.class;
	private static final Class<EntersText> ENTERS_AGE = EntersText.class;
	
	private String firstName;
	private int age;
	
	public Model buildWith(ModelBuilder modelBuilder) {
		Model useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(this::promptsUserToEnterFirstName)
					.step("S2").user(ENTERS_FIRST_NAME).system(this::savesFirstName)
					.step("S3").system(this::promptsUserToEnterAge)
					.step("S4").user(ENTERS_AGE).system(this::savesAge)
					.step("S5").system(this::greetsUserWithFirstNameAndAge)
			.build();
		return useCaseModel;
	}
	
	private void promptsUserToEnterFirstName(ModelRunner runner) {
		System.out.print("Please enter your first name: ");
	}
	
	private void promptsUserToEnterAge(ModelRunner runner) {
		System.out.print("Please enter your age: ");
	}

	private void savesFirstName(EntersText enterText) {
		firstName = enterText.text;
	}
	
	private void savesAge(EntersText enterText) {
		age = Integer.parseInt(enterText.text);
	}
	
	private void greetsUserWithFirstNameAndAge(ModelRunner runner) {
		System.out.println("Hello, " + firstName + " (" + age + ").");
	}
	
	public static void main(String[] args){		
		HelloWorld04 example = new HelloWorld04();
		example.start();
	}

	private void start() {
		Model useCaseModel = buildWith(Model.builder());
		ModelRunner useCaseModelRunner = new ModelRunner();
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText());
		useCaseModelRunner.reactTo(entersText());	
	}
}
