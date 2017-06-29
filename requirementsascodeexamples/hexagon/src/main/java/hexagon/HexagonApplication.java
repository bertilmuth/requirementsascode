package hexagon;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HexagonApplication {
  private DisplayPort displayPort;
  private UseCaseModel useCaseModel;
  private UseCaseModelRunner runner;

  public static void main(String[] args) {
    new HexagonApplication().start();
  }

  private void start() {
    displayPort = new DisplayAdapter();
    useCaseModel = 
      new HexagonUseCaseModel(displayPort).buildWith(UseCaseModelBuilder.newBuilder());
    
    runner = new UseCaseModelRunner();
    runner.run(useCaseModel);
    runner.reactTo(new AskForPoem(), new AskForPoem(), new AskForPoem());
  }
}
