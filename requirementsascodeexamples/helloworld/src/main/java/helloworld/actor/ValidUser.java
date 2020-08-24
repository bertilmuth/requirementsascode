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
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1a").systemPublish(() -> new EnterText("John Q. Public")).to(helloWorldActor)
       .build();
    
    return model;
  }
}