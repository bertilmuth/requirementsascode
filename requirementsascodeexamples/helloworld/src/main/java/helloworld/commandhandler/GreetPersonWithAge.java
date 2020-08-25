package helloworld.commandhandler;

import helloworld.domain.Greeting;
import helloworld.domain.Person;
import helloworld.infrastructure.OutputAdapter;

public class GreetPersonWithAge implements Runnable{
  private Person person;
  private OutputAdapter outputAdapter;

  public GreetPersonWithAge(Person person) {
    this.person = person;
    this.outputAdapter = new OutputAdapter();
  }

  @Override
  public void run() {
    greetWithAge(person.getAge());
  }
  
  private void greetWithAge(int age) {
    String greeting = Greeting.forUserWithAge(age);
    outputAdapter.showMessage(greeting);
  }
}
