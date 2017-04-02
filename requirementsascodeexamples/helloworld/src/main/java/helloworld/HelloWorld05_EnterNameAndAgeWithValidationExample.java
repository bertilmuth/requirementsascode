package helloworld;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HelloWorld05_EnterNameAndAgeWithValidationExample extends AbstractHelloWorldExample{
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;
	private static final Class<EnterText> ENTER_AGE = EnterText.class;
	private static final Class<NumberFormatException> NON_NUMERICAL_AGE = NumberFormatException.class;
		
	private static final int MIN_AGE = 5;
	private static final int MAX_AGE = 130;
	
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
					.step("S6").system(this::stopSystem)
						
				.flow("Handle out-of-bounds age").insteadOf("S5").when(this::ageIsOutOfBounds)
					.step("S5a_1").system(this::informUserAboutOutOfBoundsAge)
					.step("S5a_2").continueAfter("S2")
						
				.flow("Handle non-numerical age").insteadOf("S5")
					.step("S5b_1").handle(NON_NUMERICAL_AGE).system(this::informUserAboutNonNumericalAge)
					.step("S5b_2").continueAfter("S2")
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
	
	private boolean ageIsOutOfBounds(UseCaseModelRunner runner) {
		return age < MIN_AGE || age > MAX_AGE;
	}
	
	private void informUserAboutOutOfBoundsAge(UseCaseModelRunner runner) {
		System.out.println("Please enter your real age, between " + MIN_AGE + " and " + MAX_AGE);
	}
	
	private void informUserAboutNonNumericalAge(NumberFormatException exception) {
		System.out.println("You entered a non-numerical age.");
	}
	
	public static void main(String[] args){		
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.start(); 
	}

	private void start() {
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
		UseCaseModel useCaseModel = buildWith(newBuilder());
		
		useCaseModelRunner.run(useCaseModel);			
		while(!systemStopped())
			useCaseModelRunner.reactTo(enterText());	
		exitSystem();	
	}
}
