package akka;

import java.io.IOException;
import java.io.Serializable;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class Akka {
	private static final Class<AsksForHelloToUser> ASKS_FOR_HELLO_TO_USER = AsksForHelloToUser.class;
	private static final Class<AsksForHelloWorld> ASKS_FOR_HELLO_WORLD = AsksForHelloWorld.class;

	public static void main(String[] args) {
		ActorSystem actorSystem = ActorSystem.create("modelBasedActorSystem");
		
		ActorRef sayHelloActor = spawn("sayHelloActor", actorSystem, SayHelloActor.class);
		
		sayHelloActor.tell(new AsksForHelloWorld(), ActorRef.noSender());
		sayHelloActor.tell(new AsksForHelloToUser("Sandra"), ActorRef.noSender());

		waitForReturnKeyPressed(); 

		actorSystem.terminate();
	}
	
	static <T> ActorRef spawn(String actorName, ActorSystem system, Class<? extends AbstractActor> actorClass) {
		Props props = Props.create(actorClass);
		return system.actorOf(props, actorName);
	}
	
	static class AsksForHelloWorld implements Serializable{
		private static final long serialVersionUID = -8546529101337178227L;
	}

	static class AsksForHelloToUser implements Serializable {
		private static final long serialVersionUID = -133206036145556906L;
		private String name;
		public AsksForHelloToUser(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}

	static class SayHelloActor extends UntypedAbstractActor {
		private ModelRunner modelRunner;

		public SayHelloActor() {
			this.modelRunner = new ModelRunner().run(model());
		}

		@Override
		public void onReceive(Object msg) throws Exception {
		    modelRunner.reactTo(msg);
		}
	}
	static Model model() {
		Model model = Model.builder().useCase("Say hello to world, then user")
			.basicFlow()
				.step("S1").user(ASKS_FOR_HELLO_WORLD).system(() -> System.out.println("Hello, World!"))
				.step("S2").user(ASKS_FOR_HELLO_TO_USER).system(message -> System.out.println("Hello, " + message.getName() + "."))
			.build();
		return model;
	}

	static void waitForReturnKeyPressed() {
		System.out.println("Please press return key to terminate.");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
