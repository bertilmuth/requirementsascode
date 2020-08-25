package helloworld.commandhandler;

import helloworld.domain.Greeting;
import helloworld.infrastructure.OutputAdapter;

public class GreetWithDefaultName implements Runnable{
  private OutputAdapter outputAdapter;

  public GreetWithDefaultName() {
    this.outputAdapter = new OutputAdapter();
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
