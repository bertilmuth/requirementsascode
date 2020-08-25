package helloworld.commandhandler;

import java.util.function.Consumer;

import helloworld.command.EnterText;
import helloworld.domain.Person;

public class SaveName implements Consumer<EnterText> {
  private Person person;

  public SaveName(Person person) {
    this.person = person;
  }

  @Override
  public void accept(EnterText t) {
    person.saveName(t.getText());
  }
}
