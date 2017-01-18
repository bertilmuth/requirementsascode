package helloworld;

import java.util.Scanner;

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
	
	protected Runnable stopSystem(){
		return () -> isSystemStopped = true;
	}
	
	protected boolean isSystemStopped() {
		return isSystemStopped;
	}
	
	protected void exitSystem(){
		System.out.println("Exiting system!");
		scanner.close();
		System.exit(0);
	}
}
