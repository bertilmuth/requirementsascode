package actor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.requirementsascode.AbstractActor;

public class ActorExampleTest {

  @Test
  public void greetsJack() {
    OutputAdapter outputAdapter = mock(OutputAdapter.class);
    final SayHello sayHello = new SayHello(outputAdapter);
    AbstractActor greetingService = new GreetingService(sayHello);
    greetingService.reactTo(new RequestHello("Jack"));
    
    verify(outputAdapter).showMessage("Hello, Jack.");
  }
}
