package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

public class HelloUser {
  public static void main(String[] args) {
    GreetingService greeter = new GreetingService(HelloUser::saysHello);
    greeter.reactTo(new RequestHello("Joe"));
  }
  
  private static void saysHello(RequestHello requestsHello) {
    System.out.println("Hello, " + requestsHello.getUserName() + ".");
  }
}

class GreetingService extends AbstractActor {
  private static final Class<RequestHello> requestsHello = RequestHello.class;
  private final Consumer<RequestHello> saysHello;

  public GreetingService(Consumer<RequestHello> saysHello) {
    this.saysHello = saysHello;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .user(requestsHello).system(saysHello)
    .build();
    return model;
  }
}

class RequestHello {
  private String userName;

  public RequestHello(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }
}