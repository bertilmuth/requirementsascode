package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

public class HelloUser {
  public static void main(String[] args) {
    Model model = new ModelBuilder().build(HelloUser::sayHello);
    ModelRunner modelRunner = new ModelRunner().run(model);
    modelRunner.reactTo(new RequestHello("Joe"));
  }
  
  public static void sayHello(RequestHello requestHello) {
    System.out.println("Hello, " + requestHello.getUserName() + ".");
  }
}

class ModelBuilder {
  private static final Class<RequestHello> requestsHello = RequestHello.class;

  public Model build(Consumer<RequestHello> saysHello) {
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