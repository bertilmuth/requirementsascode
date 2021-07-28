package actor;

import java.util.Optional;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import actor.message.EnterName;

public class PublishingActorExample {
  public static void main(String[] args) {
    AbstractActor actor = new PublishingActor();

    Optional<Object> userName = actor.reactTo(new EnterName("Joe"));
    System.out.println("Your name is: " + userName.get() + ".");
  }
}

class PublishingActor extends AbstractActor {
  @Override
  protected Model behavior() {
    Model model = Model.builder()
      .user(EnterName.class).systemPublish(this::publishNameAsString)
      .on(String.class).system(this::displayNameString)
    .build();
    return model;
  }

  private String publishNameAsString(EnterName enterName) {
    return enterName.getUserName();
  }

  public void displayNameString(String nameString) {
    System.out.println("Welcome, " + nameString + ".");
  }
}

