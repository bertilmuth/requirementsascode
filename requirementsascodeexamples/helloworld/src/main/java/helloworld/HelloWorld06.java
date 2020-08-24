package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Actor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

import helloworld.usercommand.EnterText;

public class HelloWorld06 extends AbstractHelloWorldExample {
	private final Runnable asksForName = this::askForName;
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> savesName = this::saveName;
	private final Runnable asksForAge = this::askForAge;
	private final Class<EnterText> entersAge = EnterText.class;
	private final Consumer<EnterText> savesAge = this::saveAge;
	private final Runnable greetsUserWithName = this::greetUserWithName;
	private final Runnable greetsUserWithAge = this::greetUserWithAge;
	private final Runnable stops = super::stop;
	private final Condition ageIsOutOfBounds = this::ageIsOutOfBounds;
	private final Runnable displaysAgeIsOutOfBounds = this::displaysAgeIsOutOfBounds;
	private final Consumer<NumberFormatException> displaysAgeIsNonNumerical = this::displayAgeIsNonNumerical;
	private final Class<NumberFormatException> numberFormatException = NumberFormatException.class;
	private final Condition ageIsOk = this::ageIsOk;

  private static final int MIN_AGE = 5;
  private static final int MAX_AGE = 130;

  private String firstName;
  private int age;

  private Actor normalUser;
  private Actor anonymousUser;
  
  public HelloWorld06() {
    normalUser = new Actor("Normal User");
    anonymousUser = new Actor("Anonymous User");
  }

  @Override
  public Model behavior() {
		Model model = Model.builder()
			.useCase("Get greeted")
				.basicFlow()
					.step("S1").as(normalUser).system(asksForName)
					.step("S2").as(normalUser).user(entersName).system(savesName)
					.step("S3").as(normalUser, anonymousUser).system(asksForAge)
					.step("S4").as(normalUser, anonymousUser).user(entersAge).system(savesAge)
					.step("S5").as(normalUser).system(greetsUserWithName)
					.step("S6").as(normalUser, anonymousUser).system(greetsUserWithAge)
					.step("S7").as(normalUser, anonymousUser).system(stops)
				.flow("Handle out-of-bounds age").insteadOf("S5").condition(ageIsOutOfBounds)
					.step("S5a_1").system(displaysAgeIsOutOfBounds)
					.step("S5a_2").continuesAt("S3")
				.flow("Handle non-numerical age").insteadOf("S5")
					.step("S5b_1").on(numberFormatException).system(displaysAgeIsNonNumerical)
					.step("S5b_2").continuesAt("S3")
				.flow("Anonymous greeted with age only").insteadOf("S5").condition(ageIsOk)
					.step("S5c_1").as(anonymousUser).continuesAt("S6")
				.flow("Anonymous does not enter name").insteadOf("S1")
					.step("S1a_1").as(anonymousUser).continuesAt("S3")
			.build();
		
		return model;
  }

	private void askForName() {
		System.out.print("Please enter your name: ");
	}

	private void saveName(EnterText enterText) {
		firstName = enterText.text;
	}

	private void askForAge() {
		System.out.print("Please enter your age: ");
	}

	private void saveAge(EnterText enterText) {
		age = Integer.parseInt(enterText.text);
	}

	private void greetUserWithName() {
		System.out.println("Hello, " + firstName + " (" + age + ").");
	}

	private void greetUserWithAge() {
		System.out.println("You are " + age + " years old.");
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

	private boolean ageIsOk() {
		return !ageIsOutOfBounds();
	}

	public static void main(String[] args) {
		HelloWorld06 actor = new HelloWorld06();
		actor.react();
	}

	private void react() {
	  getModelRunner().as(anonymousUser()).run(behavior());
	  
		while (!systemStopped())
		   reactTo(entersText());
		exitSystem();
	}

	public Actor normalUser() {
		return normalUser;
	}

	public Actor anonymousUser() {
		return anonymousUser;
	}
}