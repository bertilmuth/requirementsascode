package helloworld;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HelloWorld06_EnterNameAndAgeWithAnonymousUserExample extends AbstractHelloWorldExample{
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;
	private static final Class<EnterText> ENTER_AGE = EnterText.class;
	private static final Class<NumberFormatException> NON_NUMERICAL_AGE = NumberFormatException.class;
	
	private static final int MIN_AGE = 5;
	private static final int MAX_AGE = 130;
	
	private String firstName;
	private int age;
	
	private Actor normalUser;
	private Actor anonymousUser;
	
	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		normalUser = modelBuilder.actor("Normal User");
		anonymousUser = modelBuilder.actor("Anonymous User");
				
		UseCaseModel useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").as(normalUser).system(this::promptUserToEnterFirstName)
					.step("S2").as(normalUser).user(ENTER_FIRST_NAME).system(this::saveFirstName)
					.step("S3").as(normalUser, anonymousUser).system(this::promptUserToEnterAge)
					.step("S4").as(normalUser, anonymousUser).user(ENTER_AGE).system(this::saveAge)
					.step("S5").as(normalUser).system(this::greetUserWithFirstName)
					.step("S6").as(normalUser, anonymousUser).system(this::greetUserWithAge)
					.step("S7").as(normalUser, anonymousUser).system(this::stopSystem)
						
				.flow("Handle out-of-bounds age").insteadOf("S5").when(this::ageIsOutOfBounds)
					.step("S5a_1").system(this::informUserAboutOutOfBoundsAge)
					.step("S5a_2").continueAt("S3")
						
				.flow("Handle non-numerical age").insteadOf("S5")
					.step("S5b_1").handle(NON_NUMERICAL_AGE).system(this::informUserAboutNonNumericalAge)
					.step("S5b_2").continueAt("S3")
					
				.flow("Anonymous greeted with age only").insteadOf("S5").when(this::ageIsOk)
					.step("S5c_1").as(anonymousUser).continueAt("S6")
					
				.flow("Anonymous does not enter name").insteadOf("S1")
					.step("S1a_1").as(anonymousUser).continueAt("S3")
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
	
	private void greetUserWithFirstName(UseCaseModelRunner runner) {
		System.out.println("Hello, " + firstName + ".");
	}
	
	private void greetUserWithAge(UseCaseModelRunner runner) {
		System.out.println("You are " + age + " years old.");
	}
	
	private boolean ageIsOk(UseCaseModelRunner runner) {
		return !ageIsOutOfBounds(runner);
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
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		example.start(); 
	}

	private void start() {
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
		UseCaseModel useCaseModel = buildWith(newBuilder());
		
		useCaseModelRunner.as(anonymousUser()).run(useCaseModel);			
		while(!systemStopped())
			useCaseModelRunner.reactTo(enterText());	
		exitSystem();	
	}

	public Actor normalUser() {
		return normalUser;
	}

	public Actor anonymousUser() {
		return anonymousUser;
	}
}