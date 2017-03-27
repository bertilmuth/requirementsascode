package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.builder.UseCaseModelBuilder;

public class HelloWorld04_EnterNameAndAgeExample extends AbstractHelloWorldExample{
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;
	private static final Class<EnterText> ENTER_AGE = EnterText.class;
	
	private String firstName;
	private int age;
	
	public UseCaseModel buildWith(UseCaseModelBuilder useCaseModelBuilder) {
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(promptUserToEnterFirstName())
					.step("S2").user(ENTER_FIRST_NAME).system(saveFirstName())
					.step("S3").system(promptUserToEnterAge())
					.step("S4").user(ENTER_AGE).system(saveAge())
					.step("S5").system(greetUserWithFirstNameAndAge())
			.build();
		return useCaseModel;
	}
	
	private Consumer<UseCaseRunner> promptUserToEnterFirstName() {
		return r -> System.out.print("Please enter your first name: ");
	}
	
	private Consumer<UseCaseRunner> promptUserToEnterAge() {
		return r -> System.out.print("Please enter your age: ");
	}

	private Consumer<EnterText> saveFirstName() {
		return enterText -> firstName = enterText.text;
	}
	
	private Consumer<EnterText> saveAge() {
		return enterText -> age = Integer.parseInt(enterText.text);
	}
	
	private Consumer<UseCaseRunner> greetUserWithFirstNameAndAge() {
		return r -> System.out.println("Hello, " + firstName + " (" + age + ").");
	}
	
	public static void main(String[] args){
		UseCaseRunner useCaseRunner = new UseCaseRunner();

		HelloWorld04_EnterNameAndAgeExample example = new HelloWorld04_EnterNameAndAgeExample();
		UseCaseModel useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.run(useCaseModel);		
		useCaseRunner.reactTo(example.enterText());
		useCaseRunner.reactTo(example.enterText());
	}
}
