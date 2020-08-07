package org.requirementsascode.exception;

public class NestedCallOfReactTo extends RuntimeException{
  private static final long serialVersionUID = 4979548355692063295L;
  
  public NestedCallOfReactTo() {
    super(exceptionMessage());
  }

  private static String exceptionMessage() {
    String message = "Don't call modelRunner.reactTo(x) from a message handler (i.e. from a method specified in system(..)). Use systemPublish() and return x from the specified message.";
    return message;
  }
}