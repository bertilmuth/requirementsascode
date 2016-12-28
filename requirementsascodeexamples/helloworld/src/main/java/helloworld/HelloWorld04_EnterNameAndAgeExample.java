package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseRunner;

public class HelloWorld04_EnterNameAndAgeExample extends AbstractHelloWorldExample{
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;
	private static final Class<EnterText> ENTER_AGE = EnterText.class;
	
	private String firstName;
	private int age;
	
	public void start() {	
		UseCaseRunner useCaseRunner = new UseCaseRunner();
				
		useCaseRunner.useCaseModel().useCase("Get greeted")
			.basicFlow()
				.step("S1").system(promptUserToEnterFirstName())
				.step("S2").user(ENTER_FIRST_NAME).system(saveFirstName())
				.step("S3").system(promptUserToEnterAge())
				.step("S4").user(ENTER_AGE).system(saveAge())
				.step("S5").system(greetUserWithFirstNameAndAge())
				.step("S6").system(terminateApplication());
		
		useCaseRunner.run();
		
		useCaseRunner.reactTo(enterText());
		useCaseRunner.reactTo(enterText());
	}
	
	private Runnable promptUserToEnterFirstName() {
		return () -> System.out.print("Please enter your first name: ");
	}
	
	private Runnable promptUserToEnterAge() {
		return () -> System.out.print("Please enter your age: ");
	}

	private Consumer<EnterText> saveFirstName() {
		return enterTextEvent -> firstName = enterTextEvent.text;
	}
	
	private Consumer<EnterText> saveAge() {
		return enterTextEvent -> age = Integer.parseInt(enterTextEvent.text);
	}
	
	private Runnable greetUserWithFirstNameAndAge() {
		return () -> System.out.println("Hello, " + firstName + " (" + age + ").");
	}
	
	public static void main(String[] args){
		new HelloWorld04_EnterNameAndAgeExample().start();
	}
}
