package org.requirementsascode.extract.freemarker.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.extract.freemarker.userevent.EntersName;

public class GreetsUser implements Consumer<EntersName> {
  @Override
  public void accept(EntersName enterName) {
    System.out.println("Hello, " + enterName.name);
  }
}
