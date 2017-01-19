package helloworld;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld06_EnterNameAndAgeWithAnonymousUserExample extends AbstractHelloWorldExample{
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;
	private static final Class<EnterText> ENTER_AGE = EnterText.class;
	private static final Class<NumberFormatException> NON_NUMERICAL_AGE = NumberFormatException.class;
	
	private static final String S2 = "S2";
	private static final String S4 = "S4";
	private static final String S5 = "S5";
	
	private static final int MIN_AGE = 5;
	private static final int MAX_AGE = 130;
	
	private String firstName;
	private int age;
	
	private Actor normalUser;
	private Actor anonymousUser;
	
	public void createModelFor(UseCaseRunner useCaseRunner) {	
		UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
		
		normalUser = useCaseModel.actor("Normal User");
		anonymousUser = useCaseModel.actor("Anonymous User");
				
		useCaseModel.useCase("Get greeted")
			.basicFlow()
				.step("S1").as(normalUser).system(promptUserToEnterFirstName())
				.step(S2).as(normalUser).user(ENTER_FIRST_NAME).system(saveFirstName())
				.step("S3").as(normalUser, anonymousUser).system(promptUserToEnterAge())
				.step(S4).as(normalUser, anonymousUser).user(ENTER_AGE).system(saveAge())
				.step("S5").as(normalUser).system(greetUserWithFirstName())
				.step("S6").as(normalUser, anonymousUser).system(greetUserWithAge())
				.step("S7").as(normalUser, anonymousUser).system(stopSystem())
					
			.flow("Handle out-of-bounds age").after(S4).when(ageIsOutOfBounds())
				.step("S4a_1").system(informUserAboutOutOfBoundsAge())
				.continueAfter(S2)
					
			.flow("Handle non-numerical age").after(S4)
				.step("S4b_1").handle(NON_NUMERICAL_AGE)
					.system(informUserAboutNonNumericalAge())
				.continueAfter(S2)
				
			.flow("Anonymous greeted with age only").after(S4).when(ageIsOk())
				.step("S4c_1").as(anonymousUser).continueAfter(S5)
				
			.flow("Anonymous does not enter name").atStart()
				.step("S0a_1").as(anonymousUser).continueAfter(S2);	
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
	
	private Runnable greetUserWithFirstName() {
		return () -> System.out.println("Hello, " + firstName + ".");
	}
	
	private Runnable greetUserWithAge() {
		return () -> System.out.println("You are " + age + " years old.");
	}
	
	private Predicate<UseCaseRunner> ageIsOk() {
		return ageIsOutOfBounds().negate();
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
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		example.createModelFor(useCaseRunner);
		
		useCaseRunner.runAs(example.anonymousUser());			
		while(!example.systemStopped())
			useCaseRunner.reactTo(example.enterText());	
		example.exitSystem();
	}

	public Actor normalUser() {
		return normalUser;
	}

	public Actor anonymousUser() {
		return anonymousUser;
	}
}