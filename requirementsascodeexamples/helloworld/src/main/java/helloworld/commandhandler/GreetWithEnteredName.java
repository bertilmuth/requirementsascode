package helloworld.commandhandler;

import java.util.function.Consumer;

import helloworld.command.EnterText;
import helloworld.domain.Greeting;
import helloworld.infrastructure.OutputAdapter;

public class GreetWithEnteredName implements Runnable, Consumer<EnterText> {
  private OutputAdapter outputAdapter;

  public GreetWithEnteredName() {
    this.outputAdapter = new OutputAdapter();
  }

  @Override
  public void accept(EnterText t) {
    greetWithName(t.text);    
  }

  @Override
  public void run() {
    greetWithName("User");    
  }
  
  private void greetWithName(String name) {
    String greeting = Greeting.forUserWithName(name);
    outputAdapter.showMessage(greeting);
  }
}
