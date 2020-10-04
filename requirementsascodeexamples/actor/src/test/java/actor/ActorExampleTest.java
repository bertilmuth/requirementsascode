package actor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.AbstractActor;

public class ActorExampleTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void greetsJoe() {
    final TestOutputAdapter outputAdapter = new TestOutputAdapter();
    final SayHello sayHello = new SayHello(outputAdapter);
    AbstractActor greetingService = new GreetingService(sayHello);
    new MessageSender(greetingService).sendMessages();
    
    assertEquals("Hello, Joe.", outputAdapter.message());
  }
  
  private class TestOutputAdapter extends OutputAdapter{
    private String message;

    @Override
    public void showMessage(String message) {
      this.message = message;
    }
    
    public String message() {
      return message;
    }
  }
}
