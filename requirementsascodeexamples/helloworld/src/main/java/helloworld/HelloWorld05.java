package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

import helloworld.command.EnterText;
import helloworld.commandhandler.GreetPersonWithName;
import helloworld.commandhandler.SaveAge;
import helloworld.commandhandler.SaveName;
import helloworld.domain.Person;

public class HelloWorld05 {
  public static void main(String[] args) {
    Person person = new Person();
    HelloWorldActor05 actor = new HelloWorldActor05(new SaveName(person), new SaveAge(person),
      new GreetPersonWithName(person), person::ageIsOutOfBounds);
    actor.reactTo(new EnterText("John Q. Public"));
    actor.reactTo(new EnterText("43"));
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
