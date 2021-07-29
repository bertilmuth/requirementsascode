package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Behavior;
import org.requirementsascode.BehaviorModel;
import org.requirementsascode.Model;
import org.requirementsascode.StatelessBehavior;

public class HelloUser {
  public static void main(String[] args) {
    GreeterModel greeterModel = new GreeterModel(HelloUser::saysHello);
    Behavior greeter = StatelessBehavior.of(greeterModel);
    greeter.reactTo(new SayHelloRequest("Joe"));
  }
  
  private static void saysHello(SayHelloRequest requestsHello) {
    System.out.println("Hello, " + requestsHello.getUserName() + ".");
  }
}

class GreeterModel implements BehaviorModel {
  private final Consumer<SayHelloRequest> sayHello;

  public GreeterModel(Consumer<SayHelloRequest> sayHello) {
    this.sayHello = sayHello;
  }

  @Override
  public Model model() {
    Model model = Model.builder()
      .user(SayHelloRequest.class).system(sayHello)
    .build();
    return model;
  }
}

class SayHelloRequest {
  private final String userName;

  public SayHelloRequest(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }
}