package org.requirementsascode.extract.freemarker.systemreaction;

import java.util.function.Function;

import org.requirementsascode.extract.freemarker.usercommand.EntersName;

public class NameEntered implements Function<EntersName, String> {

  @Override
  public String apply(EntersName entersName) {
    return entersName.getName();
  }

}
