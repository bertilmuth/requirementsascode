package helloworld;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld06_EnterNameAndAgeWithAnonymousUserExample extends AbstractHelloWorldExample{
	private static final String SYSTEM_PROMPTS_USER_TO_ENTER_FIRST_NAME = "System prompts user to enter first name.";
	private static final String USER_ENTERS_FIRST_NAME = "User enters first name. System saves the first name.";
	private static final String SYSTEM_PROMPTS_USER_TO_ENTER_AGE = "System prompts user to enter age.";
	private static final String USER_ENTERS_AGE = "User enters age. System saves age.";
	private static final String SYSTEM_GREETS_USER = "System greets user with first name and age.";
	private static final String SYSTEM_INFORMS_USER_ABOUT_NON_NUMERICAL_AGE = "System informs user about non-numerical age";
	private static final String SYSTEM_INFORMS_USER_ABOUT_INVALID_AGE = "System informs user about invalid age.";
	private static final String SYSTEM_TERMINATES_APPLICATION = "System terminates application.";
	
	private static final int MIN_AGE = 5;
	private static final int MAX_AGE = 130;
	
	private String firstName;
	private int age;
	
	public void start() {	
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
		
		Actor normalUser = useCaseModel.newActor("Normal User");
		Actor anonymousUser = useCaseModel.newActor("Anonymous User");
				
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep(SYSTEM_PROMPTS_USER_TO_ENTER_FIRST_NAME)
					.system(promptUserToEnterFirstName())
				.newStep(USER_ENTERS_FIRST_NAME)
					.actor(normalUser).handle(EnterTextEvent.class).system(saveFirstName())
				.newStep(SYSTEM_PROMPTS_USER_TO_ENTER_AGE)
					.system(promptUserToEnterAge())
				.newStep(USER_ENTERS_AGE)
					.actor(normalUser, anonymousUser).handle(EnterTextEvent.class).system(saveAge())
				.newStep(SYSTEM_GREETS_USER)
					.system(greetUserWithFirstNameAndAge())
				.newStep(SYSTEM_TERMINATES_APPLICATION)
					.system(terminateApplication())
			.newFlow("AF1. Handle invalid age").after(USER_ENTERS_AGE).when(ageIsInvalid())
				.newStep(SYSTEM_INFORMS_USER_ABOUT_INVALID_AGE)
					.system(informUserAboutInvalidAge())
				.continueAfter(USER_ENTERS_FIRST_NAME)
			.newFlow("AF2. Handle non-numerical age").after(USER_ENTERS_AGE)
				.newStep(SYSTEM_INFORMS_USER_ABOUT_NON_NUMERICAL_AGE)
					.handle(NumberFormatException.class).system(informUserAboutNonNumericalAge())
				.continueAfter(USER_ENTERS_FIRST_NAME)
			;/*.newFlow("AF3.1 Anonymous User does not enter name").atStart()*/
		
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
	
	private Predicate<UseCaseRunner> ageIsInvalid() {
		return r -> age < MIN_AGE || age > MAX_AGE;
	}
	
	private Runnable informUserAboutInvalidAge() {
		return () -> 
			System.out.println("Please enter your real age, between " + MIN_AGE + " and " + MAX_AGE);
	}
	
	private Consumer<NumberFormatException> informUserAboutNonNumericalAge() {
		return exception -> 
			System.out.println("You entered a non-numerical age.");
	}
	
	public static void main(String[] args){
		new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample().start();
	}
}
