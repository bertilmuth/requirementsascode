package helloworld;

import java.util.Scanner;
import java.util.function.Consumer;

import org.requirementsascode.UseCaseRunner;

public class AbstractHelloWorldExample {
	private Scanner scanner;
	private boolean isSystemStopped;

	public AbstractHelloWorldExample() {
		this.scanner = new Scanner(System.in);
		isSystemStopped = false;

	}
	
	protected EnterText enterText() {
		String text = scanner.next(); 
		return new EnterText(text);
	}
	
	protected Consumer<UseCaseRunner> stopSystem(){
		return r -> isSystemStopped = true;
	}
	
	protected boolean systemStopped() {
		return isSystemStopped;
	}
	
	protected void exitSystem(){
		System.out.println("Exiting system!");
		scanner.close();
		System.exit(0);
	}
}
