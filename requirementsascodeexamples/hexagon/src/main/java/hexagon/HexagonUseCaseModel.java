package hexagon;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;

public class HexagonUseCaseModel {
  private DisplayPort displayPort;
  private final Class<AskForPoem> askForPoem;
  
  public HexagonUseCaseModel(DisplayPort displayPort) {
    this.displayPort = displayPort;
    askForPoem = AskForPoem.class;
  }
  
  public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
    UseCaseModel useCaseModel = UseCaseModelBuilder.newBuilder()
      .useCase("Feel Stuff")
        .basicFlow()
          .step("1").user(askForPoem).system(displayPort::displaySadPoem)
          .step("2").user(askForPoem).system(displayPort::displayHappyPoem)
          .step("3").user(askForPoem).system(displayPort::displayFunnyPoem)
      .build();
    
    return useCaseModel;
  }  
}
