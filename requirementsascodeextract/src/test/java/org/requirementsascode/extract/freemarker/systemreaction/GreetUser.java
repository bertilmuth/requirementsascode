package org.requirementsascode.extract.freemarker.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.extract.freemarker.userevent.EnterName;

public class GreetUser implements Consumer<EnterName> {
  @Override
  public void accept(EnterName enterName) {
    System.out.println("Hello, " + enterName.name);
  }
}
