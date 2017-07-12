package hexagon;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HexagonApplication {
  private FeelStuffUseCaseRealization feelStuffUseCaseRealization;
  private UseCaseModel useCaseModel;
  private UseCaseModelRunner runner;

  public static void main(String[] args) {
    new HexagonApplication().start();
  }

  private void start() {
    feelStuffUseCaseRealization = new FeelStuffUseCaseRealization(new WriterAdapter(), new RepositoryAdapter());
    useCaseModel = 
      new HexagonUseCaseModel(feelStuffUseCaseRealization).buildWith(UseCaseModelBuilder.newBuilder());
    
    runner = new UseCaseModelRunner();
    runner.run(useCaseModel);
    runner.reactTo(new AskForPoem(), new AskForPoem(), new AskForPoem());
  }
}
