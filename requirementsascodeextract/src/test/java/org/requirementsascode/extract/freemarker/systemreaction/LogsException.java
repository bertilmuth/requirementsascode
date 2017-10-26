package org.requirementsascode.extract.freemarker.systemreaction;

import java.util.function.Consumer;

public class LogsException implements Consumer<Exception> {

  @Override
  public void accept(Exception t) {
    t.printStackTrace();
  }

}
