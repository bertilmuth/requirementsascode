package helloworld;

import java.util.Scanner;

public class AbstractHelloWorldExample {
	private Scanner scanner;

	public AbstractHelloWorldExample() {
		this.scanner = new Scanner(System.in);
	}
	
	public String enterText(String prompt) {
		System.out.print(prompt);
		String firstName = scanner.next(); 
		return firstName;
	}
	
	public void theEnd(){
		scanner.close();
		System.exit(0);
	}
}
