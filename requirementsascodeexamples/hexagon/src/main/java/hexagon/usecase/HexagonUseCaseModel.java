package hexagon.usecase;

import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;

import hexagon.usecaserealization.FeelStuffUseCaseRealization;

public class HexagonUseCaseModel {
  private FeelStuffUseCaseRealization feelStuffUseCaseRealization;
  private final Class<AsksForPoem> asksForPoem;
  
  public HexagonUseCaseModel(FeelStuffUseCaseRealization feelStuffUseCaseRealization) {
    this.feelStuffUseCaseRealization = feelStuffUseCaseRealization;
    asksForPoem = AsksForPoem.class;
  }
  
  public Model buildWith(ModelBuilder modelBuilder) {
    Model useCaseModel = modelBuilder.useCase("Feel Stuff")
      .basicFlow()
        .step("1").user(asksForPoem).system(feelStuffUseCaseRealization::writesSadPoem)
        .step("2").user(asksForPoem).system(feelStuffUseCaseRealization::writesHappyPoem)
        .step("3").user(asksForPoem).system(feelStuffUseCaseRealization::writesFunnyPoem)
    .build();
    
    return useCaseModel;
  }  
}
