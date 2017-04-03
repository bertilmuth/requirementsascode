package requirementsascodeextract.freemarker;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModelRunner;

public class GreetTheUser implements Consumer<UseCaseModelRunner> {
	@Override
	public void accept(UseCaseModelRunner runner) {
		System.out.println("Hello, user.");
	}
}
