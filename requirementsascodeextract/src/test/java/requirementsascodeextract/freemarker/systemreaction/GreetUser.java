package requirementsascodeextract.freemarker.systemreaction;

import java.util.function.Consumer;

import requirementsascodeextract.freemarker.userevent.EnterName;

public class GreetUser implements Consumer<EnterName> {
  @Override
  public void accept(EnterName enterName) {
    System.out.println("Hello, " + enterName.name);
  }
}
