package hexagon_example.driver_adapter;

import hexagon_example.hexagon.application.AskForEnglishPoem;
import hexagon_example.hexagon.application.AskForGermanPoem;
import hexagon_example.hexagon.driver_port.IReactToCommands;

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
	private IReactToCommands application;

	public Driver(IReactToCommands application) {
		this.application = application;
	}

	public void run() {
		application.reactTo(new AskForEnglishPoem());
		application.reactTo(new AskForGermanPoem());
	}
}
