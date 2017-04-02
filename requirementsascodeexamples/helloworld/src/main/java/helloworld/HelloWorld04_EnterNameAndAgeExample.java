package helloworld;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import java.util.function.Consumer;

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
					.step("S1").system(promptUserToEnterFirstName())
					.step("S2").user(ENTER_FIRST_NAME).system(saveFirstName())
					.step("S3").system(promptUserToEnterAge())
					.step("S4").user(ENTER_AGE).system(saveAge())
					.step("S5").system(greetUserWithFirstNameAndAge())
			.build();
		return useCaseModel;
	}
	
	private Consumer<UseCaseModelRunner> promptUserToEnterFirstName() {
		return r -> System.out.print("Please enter your first name: ");
	}
	
	private Consumer<UseCaseModelRunner> promptUserToEnterAge() {
		return r -> System.out.print("Please enter your age: ");
	}

	private Consumer<EnterText> saveFirstName() {
		return enterText -> firstName = enterText.text;
	}
	
	private Consumer<EnterText> saveAge() {
		return enterText -> age = Integer.parseInt(enterText.text);
	}
	
	private Consumer<UseCaseModelRunner> greetUserWithFirstNameAndAge() {
		return r -> System.out.println("Hello, " + firstName + " (" + age + ").");
	}
	
	public static void main(String[] args){		
		HelloWorld04_EnterNameAndAgeExample example = new HelloWorld04_EnterNameAndAgeExample();
		example.start();
	}

	private void start() {
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
		UseCaseModel useCaseModel = buildWith(newBuilder());
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText());
		useCaseModelRunner.reactTo(enterText());	
	}
}
