package helloworld;

import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.ModelRunner;

import helloworld.userevent.EntersText;

public class HelloWorld05 extends AbstractHelloWorldExample{
	private static final Class<EntersText> ENTERS_FIRST_NAME = EntersText.class;
	private static final Class<EntersText> ENTERS_AGE = EntersText.class;
	private static final Class<NumberFormatException> NON_NUMERICAL_AGE = NumberFormatException.class;
		
	private static final int MIN_AGE = 5;
	private static final int MAX_AGE = 130;
	
	private String firstName;
	private int age;
	
	public Model buildWith(ModelBuilder modelBuilder) {
		Model model = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(this::promptsUserToEnterFirstName)
					.step("S2").user(ENTERS_FIRST_NAME).system(this::savesFirstName)
					.step("S3").system(this::promptsUserToEnterAge)
					.step("S4").user(ENTERS_AGE).system(this::savesAge)
					.step("S5").system(this::greetsUserWithFirstNameAndAge)
					.step("S6").system(this::stops)
						
				.flow("Handle out-of-bounds age").insteadOf("S5").when(this::ageIsOutOfBounds)
					.step("S5a_1").system(this::informsUserAboutOutOfBoundsAge)
					.step("S5a_2").continuesAt("S3")
						
				.flow("Handle non-numerical age").insteadOf("S5")
					.step("S5b_1").on(NON_NUMERICAL_AGE).system(this::informsUserAboutNonNumericalAge)
					.step("S5b_2").continuesAt("S3")
			.build();
		return model;
	}

	private void promptsUserToEnterFirstName(ModelRunner runner) {
		System.out.print("Please enter your first name: ");
	}
	
	private void promptsUserToEnterAge(ModelRunner runner) {
		System.out.print("Please enter your age: ");
	}

	private void savesFirstName(EntersText enterText) {
		firstName = enterText.text;
	}
	
	private void savesAge(EntersText enterText) {
		age = Integer.parseInt(enterText.text);
	}
	
	private void greetsUserWithFirstNameAndAge(ModelRunner runner) {
		System.out.println("Hello, " + firstName + " (" + age + ").");
	}
	
	private boolean ageIsOutOfBounds(ModelRunner runner) {
		return age < MIN_AGE || age > MAX_AGE;
	}
	
	private void informsUserAboutOutOfBoundsAge(ModelRunner runner) {
		System.out.println("Please enter your real age, between " + MIN_AGE + " and " + MAX_AGE);
	}
	
	private void informsUserAboutNonNumericalAge(NumberFormatException exception) {
		System.out.println("You entered a non-numerical age.");
	}
	
	public static void main(String[] args){		
		HelloWorld05 example = new HelloWorld05();
		example.start(); 
	}

	private void start() {
		Model model = buildWith(Model.builder());
		ModelRunner modelRunner = new ModelRunner();
		modelRunner.run(model);			
		while(!systemStopped())
			modelRunner.reactTo(entersText());	
		exitSystem();	
	}
}
