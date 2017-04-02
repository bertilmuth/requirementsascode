package helloworld;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HelloWorld06_EnterNameAndAgeWithAnonymousUserExample extends AbstractHelloWorldExample{
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;
	private static final Class<EnterText> ENTER_AGE = EnterText.class;
	private static final Class<NumberFormatException> NON_NUMERICAL_AGE = NumberFormatException.class;
	
	private static final String S1 = "S1";
	private static final String S2 = "S2";
	private static final String S3 = "S3";
	private static final String S4 = "S4";
	private static final String S5 = "S5";
	private static final String S6 = "S6";
	
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
					.step(S1).as(normalUser).system(promptUserToEnterFirstName())
					.step(S2).as(normalUser).user(ENTER_FIRST_NAME).system(saveFirstName())
					.step(S3).as(normalUser, anonymousUser).system(promptUserToEnterAge())
					.step(S4).as(normalUser, anonymousUser).user(ENTER_AGE).system(saveAge())
					.step(S5).as(normalUser).system(greetUserWithFirstName())
					.step(S6).as(normalUser, anonymousUser).system(greetUserWithAge())
					.step("S7").as(normalUser, anonymousUser).system(stopSystem())
						
				.flow("Handle out-of-bounds age").insteadOf(S5).when(ageIsOutOfBounds())
					.step("S5a_1").system(informUserAboutOutOfBoundsAge())
					.step("S5a_2").continueAt(S3)
						
				.flow("Handle non-numerical age").insteadOf(S5)
					.step("S5b_1").handle(NON_NUMERICAL_AGE).system(informUserAboutNonNumericalAge())
					.step("S5b_2").continueAt(S3)
					
				.flow("Anonymous greeted with age only").insteadOf(S5).when(ageIsOk())
					.step("S5c_1").as(anonymousUser).continueAt(S6)
					
				.flow("Anonymous does not enter name").insteadOf(S1)
					.step("S1a_1").as(anonymousUser).continueAt(S3)
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
	
	private Consumer<UseCaseModelRunner> greetUserWithFirstName() {
		return r -> System.out.println("Hello, " + firstName + ".");
	}
	
	private Consumer<UseCaseModelRunner> greetUserWithAge() {
		return r -> System.out.println("You are " + age + " years old.");
	}
	
	private Predicate<UseCaseModelRunner> ageIsOk() {
		return ageIsOutOfBounds().negate();
	}
	
	private Predicate<UseCaseModelRunner> ageIsOutOfBounds() {
		return r -> age < MIN_AGE || age > MAX_AGE;
	}
	
	private Consumer<UseCaseModelRunner> informUserAboutOutOfBoundsAge() {
		return r -> 
			System.out.println("Please enter your real age, between " + MIN_AGE + " and " + MAX_AGE);
	}
	
	private Consumer<NumberFormatException> informUserAboutNonNumericalAge() {
		return exception -> 
			System.out.println("You entered a non-numerical age.");
	}
	
	public static void main(String[] args){		
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		example.start(); 
	}

	private void start() {
		UseCaseModelRunner useCaseRunner = new UseCaseModelRunner();
		UseCaseModel useCaseModel = buildWith(newBuilder());
		
		useCaseRunner.as(anonymousUser()).run(useCaseModel);			
		while(!systemStopped())
			useCaseRunner.reactTo(enterText());	
		exitSystem();	
	}

	public Actor normalUser() {
		return normalUser;
	}

	public Actor anonymousUser() {
		return anonymousUser;
	}
}