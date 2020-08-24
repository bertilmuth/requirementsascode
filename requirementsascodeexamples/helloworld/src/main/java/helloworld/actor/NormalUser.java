package helloworld.actor;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import helloworld.command.EnterText;

public class NormalUser extends AbstractActor{
  private final AbstractActor helloWorldActor;
  private final String name;
  private final String ageString;

  public NormalUser(AbstractActor helloWorldActor, String name, String ageString) {
    this.helloWorldActor = helloWorldActor;
    this.name = name;
    this.ageString = ageString;
  }
  
  @Override
  public Model behavior() {
    Model model = Model.builder() 
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").systemPublish(() -> new EnterText(name)).to(helloWorldActor)
          .step("S2").systemPublish(() -> new EnterText(ageString)).to(helloWorldActor)
          .step("S3").systemPublish(() -> new EnterText("43")).to(helloWorldActor)
       .build();
    
    return model;
  }
}