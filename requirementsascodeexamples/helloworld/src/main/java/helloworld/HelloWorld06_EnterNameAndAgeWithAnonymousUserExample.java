package helloworld;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.builder.UseCaseModelBuilder;

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
	
	public UseCaseModel buildWith(UseCaseModelBuilder useCaseModelBuilder) {
		normalUser = useCaseModelBuilder.actor("Normal User");
		anonymousUser = useCaseModelBuilder.actor("Anonymous User");
				
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase("Get greeted")
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
	
	private Consumer<UseCaseRunner> greetUserWithFirstName() {
		return r -> System.out.println("Hello, " + firstName + ".");
	}
	
	private Consumer<UseCaseRunner> greetUserWithAge() {
		return r -> System.out.println("You are " + age + " years old.");
	}
	
	private Predicate<UseCaseRunner> ageIsOk() {
		return ageIsOutOfBounds().negate();
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

		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		UseCaseModel useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.as(example.anonymousUser()).run(useCaseModel);			
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