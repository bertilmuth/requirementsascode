package hexagon;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HexagonApplication {
  private ConsolePort consolePort;
  private UseCaseModel useCaseModel;
  private UseCaseModelRunner runner;

  public static void main(String[] args) {
    new HexagonApplication().start();
  }

  private void start() {
    consolePort = new ConsoleAdapter(new WriterAdapter(), new RepositoryAdapter());
    useCaseModel = 
      new HexagonUseCaseModel(consolePort).buildWith(UseCaseModelBuilder.newBuilder());
    
    runner = new UseCaseModelRunner();
    runner.run(useCaseModel);
    runner.reactTo(new AskForPoem(), new AskForPoem(), new AskForPoem());
  }
}
