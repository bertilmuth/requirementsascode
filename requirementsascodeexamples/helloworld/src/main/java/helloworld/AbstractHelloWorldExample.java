package helloworld;

import java.util.Scanner;

public class AbstractHelloWorldExample {
	private Scanner scanner;

	public AbstractHelloWorldExample() {
		this.scanner = new Scanner(System.in);
	}
	
	public EnterTextEvent enterTextEvent() {
		String text = scanner.next(); 
		return new EnterTextEvent(text);
	}
	
	public Runnable terminateApplication(){
		return () -> {
			scanner.close();
			System.exit(0);
		};
	}
}
