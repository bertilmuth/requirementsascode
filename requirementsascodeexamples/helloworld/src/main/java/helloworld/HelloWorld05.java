package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Condition;
import org.requirementsascode.Model;

import helloworld.command.EnterText;

public class HelloWorld05 extends AbstractHelloWorldExample {
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> savesName = this::saveName;
	private final Class<EnterText> entersAge = EnterText.class;
	private final Consumer<EnterText> savesAge = this::saveAge;
	private final Runnable greetsUser = this::greetUser;
	private final Runnable stops = super::stop;
	private final Condition ageIsOutOfBounds = this::ageIsOutOfBounds;
	private final Runnable displaysAgeIsOutOfBounds = this::displaysAgeIsOutOfBounds;
	private final Consumer<NumberFormatException> displaysAgeIsNonNumerical = this::displayAgeIsNonNumerical;
	private final Class<NumberFormatException> numberFormatException = NumberFormatException.class;

	private static final int MIN_AGE = 5;
	private static final int MAX_AGE = 130;

	private String firstName;
	private int age;

	@Override
  public Model behavior() {
		Model model = Model.builder()
			.useCase("Get greeted")
				.basicFlow()
					.step("S1").user(entersName).system(savesName)
					.step("S2").user(entersAge).system(savesAge)
					.step("S3").system(greetsUser)
					.step("S4").system(stops)
				.flow("Handle out-of-bounds age").insteadOf("S3").condition(ageIsOutOfBounds)
					.step("S3a_1").system(displaysAgeIsOutOfBounds)
					.step("S3a_2").continuesAt("S2")
				.flow("Handle non-numerical age").insteadOf("S3")
					.step("S3b_1").on(numberFormatException).system(displaysAgeIsNonNumerical)
					.step("S3b_2").continuesAt("S2")
			.build();

		return model;
	}

	private void saveName(EnterText enterText) {
		firstName = enterText.text;
	}

	private void saveAge(EnterText enterText) {
		age = Integer.parseInt(enterText.text);
	}

	private void greetUser() {
		System.out.println("Hello, " + firstName + " (" + age + ").");
	}

	private boolean ageIsOutOfBounds() {
		return age < MIN_AGE || age > MAX_AGE;
	}

	private void displaysAgeIsOutOfBounds() {
		System.out.println("Please enter your real age, between " + MIN_AGE + " and " + MAX_AGE);
	}

	private void displayAgeIsNonNumerical(NumberFormatException exception) {
		System.out.println("You entered a non-numerical age.");
	}

	public static void main(String[] args) {
		HelloWorld05 actor = new HelloWorld05();
		actor.react();
	}

	private void react() {
	  run();
		while (!systemStopped())
			reactTo(entersText());
		exitSystem();
	}
}
