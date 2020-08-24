package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

import helloworld.command.EnterText;

public class HelloWorld05 {
  public static final Consumer<EnterText> saveName = HelloWorld05::saveName;
  public static final Consumer<EnterText> saveAge = HelloWorld05::saveAge;
  public static final Runnable greetUser = HelloWorld05::greetUser;
  public static final Condition ageIsOutOfBounds = HelloWorld05::ageIsOutOfBounds;

  private static final int MIN_AGE = 5;
  private static final int MAX_AGE = 130;

  private static String firstName;
  private static int age;

  private static void saveName(EnterText enterText) {
    firstName = enterText.text;
  }

  private static void saveAge(EnterText enterText) {
    age = Integer.parseInt(enterText.text);
  }

  private static void greetUser() {
    System.out.println("Hello, " + firstName + " (" + age + ").");
  }

  private static boolean ageIsOutOfBounds() {
    return age < MIN_AGE || age > MAX_AGE;
  }

  public static void main(String[] args) {
    HelloWorldActor05 actor = new HelloWorldActor05(saveName, saveAge, greetUser, ageIsOutOfBounds);
    actor.reactTo(new EnterText("John Q. Public"));
    actor.reactTo(new EnterText("43"));
    System.out.println(actor.getModelRunner().getLatestStep().get());
  }
}

class HelloWorldActor05 extends AbstractActor {
  private final Class<EnterText> entersName = EnterText.class;
  private final Consumer<EnterText> savesName;
  private final Class<EnterText> entersAge = EnterText.class;
  private final Consumer<EnterText> savesAge;
  private final Runnable greetsUser;
  private final Condition ageIsOutOfBounds;
  private final Class<NumberFormatException> numberFormatException = NumberFormatException.class;

  public HelloWorldActor05(Consumer<EnterText> savesName, Consumer<EnterText> savesAge, Runnable greetsUser,
    Condition ageIsOutOfBounds) {
    this.savesName = savesName;
    this.savesAge = savesAge;
    this.greetsUser = greetsUser;
    this.ageIsOutOfBounds = ageIsOutOfBounds;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").user(entersName).system(savesName)
          .step("S2").user(entersAge).system(savesAge)
          .step("S3").system(greetsUser)
        .flow("Handle out-of-bounds age").insteadOf("S3").condition(ageIsOutOfBounds)
          .step("S3a_1").continuesAt("S2")
        .flow("Handle non-numerical age").after("S2")
          .step("S3b_1").on(numberFormatException).continuesAt("S2")
      .build();

    return model;
  }
}
