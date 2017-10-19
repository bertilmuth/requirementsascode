package akka;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class Akka {
	private static final Class<AskForHelloToUser> ASK_FOR_HELLO_TO_USER = AskForHelloToUser.class;
	private static final Class<AskForHelloWorld> ASK_FOR_HELLO_WORLD = AskForHelloWorld.class;
	private static final String SAY_HELLO_ACTOR = "sayHelloActor";

	public static void main(String[] args) {
		ActorSystem actorSystem = ActorSystem.create("modelBasedActorSystem");
		
		UseCaseModelRunner useCaseModelRunner = runnerOf(useCaseModel());

		ActorRef sayHelloActor = spawn(SAY_HELLO_ACTOR, actorSystem, SayHelloActor.class, useCaseModelRunner);
		
		sayHelloActor.tell(new AskForHelloWorld(), ActorRef.noSender());
		sayHelloActor.tell(new AskForHelloToUser("Sandra"), ActorRef.noSender());

		waitForReturnKeyPressed(); 

		actorSystem.terminate();
	}

	static UseCaseModelRunner runnerOf(UseCaseModel useCaseModel) {
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
		useCaseModelRunner.run(useCaseModel);
		return useCaseModelRunner;
	}
	
	static <T> ActorRef spawn(String actorName, ActorSystem system, Class<? extends AbstractActor> actorClass,
			Object... constructorParams) {
		Props props = Props.create(actorClass, constructorParams);
		return system.actorOf(props, actorName);
	}

	static UseCaseModel useCaseModel() {
		SayHelloWorld sayHelloWorld = new SayHelloWorld();
		SayHelloToUser sayHelloToUser = new SayHelloToUser();
		
		UseCaseModel useCaseModel = UseCaseModelBuilder.newBuilder().useCase("Say hello to world, then user")
			.basicFlow()
				.step("S1").user(ASK_FOR_HELLO_WORLD).system(sayHelloWorld)
				.step("S2").user(ASK_FOR_HELLO_TO_USER).system(sayHelloToUser)
			.build();
		return useCaseModel;
	}
	
	static class AskForHelloWorld implements Serializable{
		private static final long serialVersionUID = -8546529101337178227L;
	}

	static class AskForHelloToUser implements Serializable {
		private static final long serialVersionUID = -133206036145556906L;
		private String name;
		public AskForHelloToUser(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
	
	static class SayHelloWorld implements Consumer<AskForHelloWorld>, Serializable{
		private static final long serialVersionUID = 5717637483302189546L;

		@Override
		public void accept(AskForHelloWorld t) {
			System.out.println("Hello, World.");
		}
	}
	
	static class SayHelloToUser implements Consumer<AskForHelloToUser>, Serializable{
		private static final long serialVersionUID = 5090421774929433206L;

		@Override
		public void accept(AskForHelloToUser t) {
			System.out.println("Hello, " + t.getName() + ".");
		}
	}

	static class SayHelloActor extends UntypedAbstractActor {
		private UseCaseModelRunner useCaseModelRunner;

		public SayHelloActor(UseCaseModelRunner useCaseModelRunner) {
			this.useCaseModelRunner = useCaseModelRunner;
		}

		@Override
		public void onReceive(Object msg) throws Exception {
			useCaseModelRunner.reactTo(msg);
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
