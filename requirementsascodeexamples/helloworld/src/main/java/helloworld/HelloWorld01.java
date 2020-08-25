package helloworld;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import helloworld.commandhandler.GreetUser;

public class HelloWorld01 {
  public static void main(String[] args) {
    HelloWorldActor01 actor = new HelloWorldActor01(new GreetUser());
    actor.run();
  }
}

class HelloWorldActor01 extends AbstractActor {
  private final Runnable greetsUser;

  public HelloWorldActor01(Runnable greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").system(greetsUser)
      .build();

    return model;
  }
}

