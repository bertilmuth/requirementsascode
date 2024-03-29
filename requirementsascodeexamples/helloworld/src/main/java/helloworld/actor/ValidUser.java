package helloworld.actor;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import helloworld.command.EnterText;

public class ValidUser extends AbstractActor{
  private final AbstractActor helloWorldActor;

  public ValidUser(AbstractActor helloWorldActor) {
    this.helloWorldActor = helloWorldActor;
  }
  
  @Override
  protected Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").systemPublish(() -> new EnterText("John Q. Public")).to(helloWorldActor)
          .step("S2").systemPublish(() -> new EnterText("43")).to(helloWorldActor)
       .build();
    
    return model;
  }
}