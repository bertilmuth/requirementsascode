package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

import helloworld.actor.AnonymousUser;
import helloworld.actor.NormalUser;
import helloworld.command.EnterText;
import helloworld.commandhandler.GreetPersonWithAge;
import helloworld.commandhandler.GreetPersonWithName;
import helloworld.commandhandler.SaveAge;
import helloworld.commandhandler.SaveName;
import helloworld.domain.Person;

public class HelloWorld06 {
  public static void main(String[] args) {
    Person person = new Person();
    HelloWorldActor06 helloWorldActor = new HelloWorldActor06(new SaveName(person), new SaveAge(person), new GreetPersonWithName(person),
      new GreetPersonWithAge(person), person::ageIsOk, person::ageIsOutOfBounds);

    NormalUser normalUser = new NormalUser(helloWorldActor, "Jane", "21");
    AnonymousUser anonymousUser = new AnonymousUser(helloWorldActor, "43");
    helloWorldActor.setNormalUser(normalUser);
    helloWorldActor.setAnonymousUser(anonymousUser);

    normalUser.run();
  }
}

class HelloWorldActor06 extends AbstractActor {
  private final Class<EnterText> entersName = EnterText.class;
  private final Consumer<EnterText> savesName;
  private final Class<EnterText> entersAge = EnterText.class;
  private final Consumer<EnterText> savesAge;
  private final Runnable greetsUserWithName;
  private final Runnable greetsUserWithAge;
  private final Condition ageIsOk;
  private final Condition ageIsOutOfBounds;
  private final Class<NumberFormatException> numberFormatException = NumberFormatException.class;

  private AbstractActor normalUser;
  private AbstractActor anonymousUser;

  public HelloWorldActor06(Consumer<EnterText> savesName, Consumer<EnterText> savesAge, Runnable greetsUserWithName,
    Runnable greetsUserWithAge, Condition ageIsOk, Condition ageIsOutOfBounds) {
    this.savesName = savesName;
    this.savesAge = savesAge;
    this.greetsUserWithName = greetsUserWithName;
    this.greetsUserWithAge = greetsUserWithAge;
    this.ageIsOk = ageIsOk;
    this.ageIsOutOfBounds = ageIsOutOfBounds;
  }

  @Override
  protected Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").as(normalUser).user(entersName).system(savesName)
          .step("S2").as(normalUser, anonymousUser).user(entersAge).system(savesAge)
          .step("S3").as(normalUser).system(greetsUserWithName)
          .step("S4").as(normalUser, anonymousUser).system(greetsUserWithAge)
        .flow("Handle out-of-bounds age").after("S2").condition(ageIsOutOfBounds)
          .step("S3a_1").continuesAt("S2")
        .flow("Handle non-numerical age").anytime()
          .step("S3b_1").on(numberFormatException).continuesAt("S2")
        .flow("Anonymous greeted with age only").after("S2").condition(ageIsOk)
          .step("S3c_1").as(anonymousUser).continuesAt("S4")
        .flow("Anonymous does not enter name").insteadOf("S1")
          .step("S1a_1").as(anonymousUser).user(entersAge).system(savesAge)
          .step("S1a_2").continuesAfter("S2")
      .build();

    return model;
  }

  public void setNormalUser(AbstractActor normalUser) {
    this.normalUser = normalUser;
  }

  public void setAnonymousUser(AbstractActor anonymousUser) {
    this.anonymousUser = anonymousUser;
  }
}