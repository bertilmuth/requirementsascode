package hexagon;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;

public class HexagonUseCaseModel {
  private FeelStuffUseCaseRealization feelStuffUseCaseRealization;
  private final Class<AskForPoem> askForPoem;
  
  public HexagonUseCaseModel(FeelStuffUseCaseRealization feelStuffUseCaseRealization) {
    this.feelStuffUseCaseRealization = feelStuffUseCaseRealization;
    askForPoem = AskForPoem.class;
  }
  
  public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
    UseCaseModel useCaseModel = modelBuilder.useCase("Feel Stuff")
      .basicFlow()
        .step("1").user(askForPoem).system(feelStuffUseCaseRealization::writeSadPoem)
        .step("2").user(askForPoem).system(feelStuffUseCaseRealization::writeHappyPoem)
        .step("3").user(askForPoem).system(feelStuffUseCaseRealization::writeFunnyPoem)
    .build();
    
    return useCaseModel;
  }  
}
