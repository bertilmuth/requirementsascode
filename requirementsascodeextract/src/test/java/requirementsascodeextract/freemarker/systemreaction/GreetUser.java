package requirementsascodeextract.freemarker.systemreaction;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModelRunner;

public class GreetUser implements Consumer<UseCaseModelRunner> {
  @Override
  public void accept(UseCaseModelRunner runner) {
    System.out.println("Hello, user.");
  }
}
