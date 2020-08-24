package helloworld;

import java.util.Scanner;

import org.requirementsascode.AbstractActor;

import helloworld.command.EnterText;

public abstract class AbstractHelloWorldExample extends AbstractActor{
	private Scanner scanner;
	private boolean isSystemStopped;

	public AbstractHelloWorldExample() {
		this.scanner = new Scanner(System.in);
		isSystemStopped = false;
	}

	protected EnterText entersText() {
		String text = scanner.next();
		return new EnterText(text);
	}

	protected void stop() {
		isSystemStopped = true;
	}

	protected boolean systemStopped() {
		return isSystemStopped;
	}

	protected void exitSystem() {
		System.out.println("Exiting system!");
		scanner.close();
		System.exit(0);
	}
}
