package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import helloworld.command.EnterText;
import helloworld.commandhandler.GreetPerson;
import helloworld.commandhandler.SaveAge;
import helloworld.commandhandler.SaveName;
import helloworld.domain.Person;

public class HelloWorld04 {
  public static void main(String[] args) {
    Person person = new Person();
    HelloWorldActor04 actor = new HelloWorldActor04(new SaveName(person), new SaveAge(person), new GreetPerson(person));
    actor.reactTo(new EnterText("John Q. Public"));
    actor.reactTo(new EnterText("43"));
  }
}

class HelloWorldActor04 extends AbstractActor {
  private final Class<EnterText> entersName = EnterText.class;
  private final Consumer<EnterText> savesName;
  private final Class<EnterText> entersAge = EnterText.class;
  private final Consumer<EnterText> savesAge;
  private final Runnable greetsUser;

  public HelloWorldActor04(Consumer<EnterText> savesName, Consumer<EnterText> savesAge, Runnable greetsUser) {
    this.savesName = savesName;
    this.savesAge = savesAge;
    this.greetsUser = greetsUser;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").user(entersName).system(savesName)
          .step("S2").user(entersAge).system(savesAge)
          .step("S3").system(greetsUser)
        .build();

    return model;
  }
}
