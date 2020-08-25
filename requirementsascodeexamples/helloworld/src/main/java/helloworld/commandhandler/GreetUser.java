package helloworld.commandhandler;

import java.util.function.Consumer;

import helloworld.command.EnterText;
import helloworld.domain.Greeting;
import helloworld.infrastructure.OutputAdapter;

public class GreetUser implements Runnable, Consumer<EnterText> {
  private OutputAdapter outputAdapter;

  public GreetUser() {
    this.outputAdapter = new OutputAdapter();
  }

  @Override
  public void accept(EnterText t) {
    String greeting = Greeting.forUserWithName(t.text);
    outputAdapter.showMessage(greeting);    
  }

  @Override
  public void run() {
    accept(new EnterText("User"));
  }
}
