package behavior;

import java.util.function.Consumer;

import org.requirementsascode.Behavior;
import org.requirementsascode.BehaviorModel;
import org.requirementsascode.Model;
import org.requirementsascode.StatelessBehavior;

public class BehaviorExample {
  public static void main(String[] args) {
    ConsolePrinter consolePrinter = new ConsolePrinter(); 
    GreetingServiceModel greetingServiceModel = new GreetingServiceModel(consolePrinter);
    Behavior greetingService = StatelessBehavior.of(greetingServiceModel);
    
    greetingService.reactTo(new SayHelloRequest("Joe"));
  }
}

/**
 * The behavior model defines that a consumer reacts to SayHelloRequest.
 * 
 * @author b_muth
 *
 */
class GreetingServiceModel implements BehaviorModel {
  private final Consumer<String> outputPort;

  public GreetingServiceModel(Consumer<String> outputPort) {
    this.outputPort = outputPort;
  }
  
  @Override
  public Model model() {
    Model model = Model.builder()
      .user(SayHelloRequest.class).system(sayHello())
    .build();
    return model;
  }
  
  private SayHello sayHello() {
    return new SayHello(outputPort);
  }
}

/**
 * Command class
 */
class SayHelloRequest {
  private final String userName;

  public SayHelloRequest(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }
}

/**
 * Message handler
 */
class SayHello implements Consumer<SayHelloRequest> {
  private final Consumer<String> outputPort;

  public SayHello(Consumer<String> outputPort) {
    this.outputPort = outputPort;
  }

  public void accept(SayHelloRequest requestHello) {
    String greeting = Greeting.forUser(requestHello.getUserName());
    outputPort.accept(greeting);
  }
}

/**
 * Infrastructure class
 */
class ConsolePrinter implements Consumer<String>{
  public void accept(String message) {
    System.out.println(message);
  }
}

/**
 * Domain class
 */
class Greeting {
  public static String forUser(String userName) {
    return "Hello, " + userName + ".";
  }
}
