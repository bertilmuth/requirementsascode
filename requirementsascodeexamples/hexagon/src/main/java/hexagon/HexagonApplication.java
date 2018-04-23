package hexagon;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import hexagon.adapter.RepositoryAdapter;
import hexagon.adapter.WriterAdapter;
import hexagon.usecase.AsksForPoem;
import hexagon.usecase.HexagonUseCaseModel;
import hexagon.usecaserealization.FeelStuffUseCaseRealization;

public class HexagonApplication {
  private FeelStuffUseCaseRealization feelStuffUseCaseRealization;
  private Model useCaseModel;
  private ModelRunner runner;

  public static void main(String[] args) {
    new HexagonApplication().start();
  }

  private void start() {
    feelStuffUseCaseRealization = new FeelStuffUseCaseRealization(new WriterAdapter(), new RepositoryAdapter());
    useCaseModel = 
      new HexagonUseCaseModel(feelStuffUseCaseRealization).buildWith(Model.builder());
    
    runner = new ModelRunner();
    runner.run(useCaseModel);
    runner.reactTo(new AsksForPoem(), new AsksForPoem(), new AsksForPoem());
  }
}
