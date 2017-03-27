package helloworld;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld05_EnterNameAndAgeWithValidationExample extends AbstractHelloWorldExample{
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
	
	public void create(UseCaseModel useCaseModel) {					
		useCaseModel.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(promptUserToEnterFirstName())
				.step(S2).user(ENTER_FIRST_NAME).system(saveFirstName())
				.step("S3").system(promptUserToEnterAge())
				.step(S4).user(ENTER_AGE).system(saveAge())
				.step(S5).system(greetUserWithFirstNameAndAge())
				.step("S6").system(stopSystem())
					
			.flow("Handle out-of-bounds age").insteadOf(S5).when(ageIsOutOfBounds())
				.step("S5a_1").system(informUserAboutOutOfBoundsAge())
				.step("S5a_2").continueAfter(S2)
					
			.flow("Handle non-numerical age").insteadOf(S5)
				.step("S5b_1").handle(NON_NUMERICAL_AGE).system(informUserAboutNonNumericalAge())
				.step("S5b_2").continueAfter(S2);		
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
	
	private Predicate<UseCaseRunner> ageIsOutOfBounds() {
		return r -> age < MIN_AGE || age > MAX_AGE;
	}
	
	private Consumer<UseCaseRunner> informUserAboutOutOfBoundsAge() {
		return r -> 
			System.out.println("Please enter your real age, between " + MIN_AGE + " and " + MAX_AGE);
	}
	
	private Consumer<NumberFormatException> informUserAboutNonNumericalAge() {
		return exception -> 
			System.out.println("You entered a non-numerical age.");
	}
	
	public static void main(String[] args){
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
		
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.create(useCaseModel);

		useCaseRunner.run();			
		while(!example.systemStopped())
			useCaseRunner.reactTo(example.enterText());	
		example.exitSystem();
	}
}
