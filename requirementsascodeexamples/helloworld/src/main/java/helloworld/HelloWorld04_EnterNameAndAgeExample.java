package helloworld;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HelloWorld04_EnterNameAndAgeExample extends AbstractHelloWorldExample{
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;
	private static final Class<EnterText> ENTER_AGE = EnterText.class;
	
	private String firstName;
	private int age;
	
	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		UseCaseModel useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(this::promptUserToEnterFirstName)
					.step("S2").user(ENTER_FIRST_NAME).system(this::saveFirstName)
					.step("S3").system(this::promptUserToEnterAge)
					.step("S4").user(ENTER_AGE).system(this::saveAge)
					.step("S5").system(this::greetUserWithFirstNameAndAge)
			.build();
		return useCaseModel;
	}
	
	private void promptUserToEnterFirstName(UseCaseModelRunner runner) {
		System.out.print("Please enter your first name: ");
	}
	
	private void promptUserToEnterAge(UseCaseModelRunner runner) {
		System.out.print("Please enter your age: ");
	}

	private void saveFirstName(EnterText enterText) {
		firstName = enterText.text;
	}
	
	private void saveAge(EnterText enterText) {
		age = Integer.parseInt(enterText.text);
	}
	
	private void greetUserWithFirstNameAndAge(UseCaseModelRunner runner) {
		System.out.println("Hello, " + firstName + " (" + age + ").");
	}
	
	public static void main(String[] args){		
		HelloWorld04_EnterNameAndAgeExample example = new HelloWorld04_EnterNameAndAgeExample();
		example.start();
	}

	private void start() {
		UseCaseModel useCaseModel = buildWith(newBuilder());
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText());
		useCaseModelRunner.reactTo(enterText());	
	}
}
