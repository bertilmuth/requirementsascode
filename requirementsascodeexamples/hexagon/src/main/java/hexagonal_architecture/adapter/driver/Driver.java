package hexagonal_architecture.adapter.driver;

import java.util.function.Consumer;

import hexagonal_architecture.hexagon.application.AskForEnglishPoem;
import hexagonal_architecture.hexagon.application.AskForGermanPoem;

/**
 * The driver adapter of the application. It's on the left side of the hexagon.
 * It sends user requests as command objects to the hexagon. (For simplicitly,
 * sending is done autonomously without user interaction.)
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
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
