package helloworld;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseRunner;

public class HelloWorld05_EnterNameAndAgeWithValidationExample extends AbstractHelloWorldExample{
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;
	private static final Class<EnterText> ENTER_AGE = EnterText.class;
	private static final Class<NumberFormatException> NON_NUMERICAL_AGE = NumberFormatException.class;
	
	private static final String S2 = "S2";	
	private static final String S4 = "S4";	
	
	private static final int MIN_AGE = 5;
	private static final int MAX_AGE = 130;
	
	private String firstName;
	private int age;
	
	public void start() {	
		UseCaseRunner useCaseRunner = new UseCaseRunner();
				
		useCaseRunner.useCaseModel().useCase("Get greeted")
			.basicFlow()
				.step("S1").system(promptUserToEnterFirstName())
				.step(S2).user(ENTER_FIRST_NAME).system(saveFirstName())
				.step("S3").system(promptUserToEnterAge())
				.step(S4).user(ENTER_AGE).system(saveAge())
				.step("S5").system(greetUserWithFirstNameAndAge())
				.step("S6").system(terminateApplication())
					
			.flow("Handle out-of-bounds age").after(S4).when(ageIsOutOfBounds())
				.step("S4a_1").system(informUserAboutOutOfBoundsAge())
				.continueAfter(S2)
					
			.flow("Handle non-numerical age").after(S4)
				.step("S4b_1").handle(NON_NUMERICAL_AGE)
					.system(informUserAboutNonNumericalAge())
				.continueAfter(S2);
		
		useCaseRunner.run();
		
		while(true)
			useCaseRunner.reactTo(enterText());					
	}

	private Runnable promptUserToEnterFirstName() {
		return () -> System.out.print("Please enter your first name: ");
	}
	
	private Runnable promptUserToEnterAge() {
		return () -> System.out.print("Please enter your age: ");
	}

	private Consumer<EnterText> saveFirstName() {
		return enterText -> firstName = enterText.text;
	}
	
	private Consumer<EnterText> saveAge() {
		return enterText -> age = Integer.parseInt(enterText.text);
	}
	
	private Runnable greetUserWithFirstNameAndAge() {
		return () -> System.out.println("Hello, " + firstName + " (" + age + ").");
	}
	
	private Predicate<UseCaseRunner> ageIsOutOfBounds() {
		return r -> age < MIN_AGE || age > MAX_AGE;
	}
	
	private Runnable informUserAboutOutOfBoundsAge() {
		return () -> 
			System.out.println("Please enter your real age, between " + MIN_AGE + " and " + MAX_AGE);
	}
	
	private Consumer<NumberFormatException> informUserAboutNonNumericalAge() {
		return exception -> 
			System.out.println("You entered a non-numerical age.");
	}
	
	public static void main(String[] args){
		new HelloWorld05_EnterNameAndAgeWithValidationExample().start();
	}
}
