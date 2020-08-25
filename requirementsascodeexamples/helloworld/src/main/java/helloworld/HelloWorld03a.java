package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import helloworld.actor.ValidUser;
import helloworld.command.EnterText;
import helloworld.commandhandler.GreetUser;

public class HelloWorld03a {
  public static void main(String[] args) {
    HelloWorldActor03a helloWorldActor = new HelloWorldActor03a(new GreetUser());
    ValidUser validUser03a = new ValidUser(helloWorldActor);
    helloWorldActor.setValidUser(validUser03a);
    validUser03a.run();
  }
}

class HelloWorldActor03a extends AbstractActor {
  private AbstractActor validUser;
  private final Class<EnterText> entersName = EnterText.class;
  private final Consumer<EnterText> greetsUser;

  public HelloWorldActor03a(Consumer<EnterText> greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted").as(validUser)
        .basicFlow()
          .step("S1").user(entersName).system(greetsUser)
      .build();

    return model;
  }

  public void setValidUser(AbstractActor validUser) {
    this.validUser = validUser;
  }
}
