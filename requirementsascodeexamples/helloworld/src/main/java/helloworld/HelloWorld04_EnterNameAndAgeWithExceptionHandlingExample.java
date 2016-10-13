package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld04_EnterNameAndAgeWithExceptionHandlingExample extends AbstractHelloWorldExample{
	private static final String SYSTEM_PROMPTS_USER_TO_ENTER_FIRST_NAME = "System prompts user to enter first name";
	private static final String USER_ENTERS_FIRST_NAME = "User enters first name. System saves the first name.";
	private static final String SYSTEM_PROMPTS_USER_TO_ENTER_AGE = "System prompts user to enter age.";
	private static final String USER_ENTERS_AGE = "User enters age. System saves age.";
	private static final String SYSTEM_GREETS_USER = "System greets user with first name and age.";
	private static final String SYSTEM_INFORMS_USER_ABOUT_INVALID_AGE = "System informs user about invalid age";
	private static final String APPLICATION_TERMINATES = "Application terminates";
	
	private String firstName;
	private int age;
	
	public void start() {	
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
				
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep(SYSTEM_PROMPTS_USER_TO_ENTER_FIRST_NAME)
					.system(promptUserToEnterFirstName())
				.newStep(USER_ENTERS_FIRST_NAME)
					.handle(EnterTextEvent.class).system(saveFirstName())
				.newStep(SYSTEM_PROMPTS_USER_TO_ENTER_AGE)
					.system(promptUserToEnterAge())
				.newStep(USER_ENTERS_AGE)
					.handle(EnterTextEvent.class).system(saveAge())
				.newStep(SYSTEM_GREETS_USER)
					.system(greetUserWithFirstNameAndAge())
				.newStep(APPLICATION_TERMINATES)
					.system(terminateApplication())
			.newFlow("AF1. Handle invalid age").after(USER_ENTERS_AGE)
				.newStep(SYSTEM_INFORMS_USER_ABOUT_INVALID_AGE)
					.handle(NumberFormatException.class).system(informUserAboutInvalidAge())
				.continueAfter(USER_ENTERS_FIRST_NAME);
		
		useCaseRunner.run();
		
		while(true)
			useCaseRunner.reactTo(enterTextEvent());					
	}

	private Runnable promptUserToEnterFirstName() {
		return () -> System.out.print("Please enter your first name: ");
	}
	
	private Runnable promptUserToEnterAge() {
		return () -> System.out.print("Please enter your age: ");
	}

	private Consumer<EnterTextEvent> saveFirstName() {
		return enterTextEvent -> firstName = enterTextEvent.getText();
	}
	
	private Consumer<EnterTextEvent> saveAge() {
		return enterTextEvent -> age = Integer.parseInt(enterTextEvent.getText());
	}
	
	private Runnable greetUserWithFirstNameAndAge() {
		return () -> System.out.println("Hello, " + 
			firstName + " (" + age + ").");
	}
	
	private Consumer<NumberFormatException> informUserAboutInvalidAge() {
		return exception -> {
			System.out.println("You entered an invalid age.");
		};
	}
	
	public static void main(String[] args){
		new HelloWorld04_EnterNameAndAgeWithExceptionHandlingExample().start();
	}
}
