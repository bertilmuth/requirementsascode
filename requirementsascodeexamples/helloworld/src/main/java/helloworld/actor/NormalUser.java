package helloworld.actor;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import helloworld.command.EnterText;

public class NormalUser extends AbstractActor{
  private final AbstractActor helloWorldActor;

  public NormalUser(AbstractActor helloWorldActor) {
    this.helloWorldActor = helloWorldActor;
  }
  
  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").systemPublish(() -> new EnterText("John Q. Public")).to(helloWorldActor)
          .step("S2").systemPublish(() -> new EnterText("43")).to(helloWorldActor)
       .build();
    
    return model;
  }
}