package helloworld.commandhandler;

import java.util.function.Consumer;

import helloworld.command.EnterText;
import helloworld.domain.Person;

public class SaveAge implements Consumer<EnterText> {
  private Person person;

  public SaveAge(Person person) {
    this.person = person;
  }

  @Override
  public void accept(EnterText t) {
    int age = Integer.parseInt(t.getText());
    person.saveAge(age);
  }
}
