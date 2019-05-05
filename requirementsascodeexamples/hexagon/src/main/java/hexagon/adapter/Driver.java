package hexagon.adapter;

import java.util.function.Consumer;

import hexagon.application.AskForEnglishPoem;
import hexagon.application.AskForGermanPoem;

public class Driver {
	private Consumer<Object> application;

	public Driver(Consumer<Object> application) {
		this.application = application;
	}

	public void run() {
		application.accept(new AskForEnglishPoem());
		application.accept(new AskForGermanPoem());
	}
}
