package akka;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Consumer;

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
		
		ModelRunner modelRunner = runnerOf(model());

		ActorRef sayHelloActor = spawn("sayHelloActor", actorSystem, SayHelloActor.class, modelRunner);
		
		sayHelloActor.tell(new AsksForHelloWorld(), ActorRef.noSender());
		sayHelloActor.tell(new AsksForHelloToUser("Sandra"), ActorRef.noSender());

		waitForReturnKeyPressed(); 

		actorSystem.terminate();
	}

	static ModelRunner runnerOf(Model model) {
		ModelRunner modelRunner = new ModelRunner();
		modelRunner.run(model);
		return modelRunner;
	}
	
	static <T> ActorRef spawn(String actorName, ActorSystem system, Class<? extends AbstractActor> actorClass,
			Object... constructorParams) {
		Props props = Props.create(actorClass, constructorParams);
		return system.actorOf(props, actorName);
	}

	static Model model() {
		SaysHelloWorld saysHelloWorld = new SaysHelloWorld();
		SaysHelloToUser saysHelloToUser = new SaysHelloToUser();
		
		Model model = Model.builder().useCase("Say hello to world, then user")
			.basicFlow()
				.step("S1").user(ASKS_FOR_HELLO_WORLD).system(saysHelloWorld)
				.step("S2").user(ASKS_FOR_HELLO_TO_USER).system(saysHelloToUser)
			.build();
		return model;
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
	
	static class SaysHelloWorld implements Consumer<AsksForHelloWorld>, Serializable{
		private static final long serialVersionUID = 5717637483302189546L;

		@Override
		public void accept(AsksForHelloWorld t) {
			System.out.println("Hello, World.");
		}
	}
	
	static class SaysHelloToUser implements Consumer<AsksForHelloToUser>, Serializable{
		private static final long serialVersionUID = 5090421774929433206L;

		@Override
		public void accept(AsksForHelloToUser t) {
			System.out.println("Hello, " + t.getName() + ".");
		}
	}

	static class SayHelloActor extends UntypedAbstractActor {
		private ModelRunner modelRunner;

		public SayHelloActor(ModelRunner modelRunner) {
			this.modelRunner = modelRunner;
		}

		@Override
		public void onReceive(Object msg) throws Exception {
		    modelRunner.reactTo(msg);
		}
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
