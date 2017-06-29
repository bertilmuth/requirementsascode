package hexagon;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;

public class HexagonUseCaseModel {
  private ConsolePort consolePort;
  private final Class<AskForPoem> askForPoem;
  
  public HexagonUseCaseModel(ConsolePort consolePort) {
    this.consolePort = consolePort;
    askForPoem = AskForPoem.class;
  }
  
  public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
    UseCaseModel useCaseModel = modelBuilder.useCase("Feel Stuff")
      .basicFlow()
        .step("1").user(askForPoem).system(consolePort::writeSadPoem)
        .step("2").user(askForPoem).system(consolePort::writeHappyPoem)
        .step("3").user(askForPoem).system(consolePort::writeFunnyPoem)
    .build();
    
    return useCaseModel;
  }  
}
