package helloworld;

import java.util.Scanner;

import org.requirementsascode.UseCaseModelRunner;

import helloworld.userevent.EntersText;

public class AbstractHelloWorldExample {
	private Scanner scanner;
	private boolean isSystemStopped;

	public AbstractHelloWorldExample() {
		this.scanner = new Scanner(System.in);
		isSystemStopped = false;

	}
	
	protected EntersText entersText() {
		String text = scanner.next(); 
		return new EntersText(text);
	}
	
	protected void stops(UseCaseModelRunner runner){
		isSystemStopped = true;
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
