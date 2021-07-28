package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import helloworld.command.EnterText;
import helloworld.commandhandler.GreetWithEnteredName;

public class HelloWorld03 {
  public static void main(String[] args) {
    HelloWorldActor03 actor = new HelloWorldActor03(new GreetWithEnteredName());
    actor.reactTo(new EnterText("John Q. Public"));
  }
}

class HelloWorldActor03 extends AbstractActor {
  private final Class<EnterText> entersName = EnterText.class;
  private final Consumer<EnterText> greetsUser;

  public HelloWorldActor03(Consumer<EnterText> greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
  protected Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").user(entersName).system(greetsUser)
      .build();

    return model;
  }
}
