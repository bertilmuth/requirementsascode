package actor;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

import actor.message.EnterName;
import actor.message.NameEntered;

public class InteractingActorsExample {
	public static void main(String[] args) {
    AbstractActor consumer = new MessageConsumer(); 
		AbstractActor producer = new MessageProducer(consumer); 
		
		producer.reactTo(new EnterName("Joe"));	
	}
}

class MessageProducer extends AbstractActor {
  private AbstractActor messageConsumer;

  public MessageProducer(AbstractActor messageConsumer) {
    this.messageConsumer = messageConsumer;
  }
  
  @Override
  public Model behavior() {
    Model model = Model.builder()
      .user(EnterName.class).systemPublish(this::nameEntered).to(messageConsumer)
    .build();
    return model;
  }

  private NameEntered nameEntered(EnterName enterName) {
    return new NameEntered(enterName.getUserName());
  }
}

class MessageConsumer extends AbstractActor {
  @Override
  public Model behavior() {
    Model model = Model.builder()
      .on(NameEntered.class).system(this::displayName)
    .build();
    return model;
  }

  public void displayName(NameEntered nameEntered) {
    System.out.println("Welcome, " + nameEntered.getUserName() + ".");
  }
}