package behavior;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.requirementsascode.Behavior;
import org.requirementsascode.StatelessBehavior;

public class BehaviorExampleTest {

  @Test
  public void greetsJack() {
    ConsolePrinter mockConsolePrinter = mock(ConsolePrinter.class);
    GreetingServiceModel greetingServiceModel = new GreetingServiceModel(mockConsolePrinter);
    Behavior greetingService = StatelessBehavior.of(greetingServiceModel);
    
    greetingService.reactTo(new SayHelloRequest("Jack"));
    
    verify(mockConsolePrinter).accept("Hello, Jack.");
  }
}
