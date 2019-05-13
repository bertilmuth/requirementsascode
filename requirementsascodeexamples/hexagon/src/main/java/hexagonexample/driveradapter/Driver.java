package hexagonexample.driveradapter;

import hexagonexample.hexagon.boundary.AskForEnglishPoem;
import hexagonexample.hexagon.boundary.AskForGermanPoem;
import hexagonexample.hexagon.boundary.driverport.IReactToCommands;

/**
 * The driver adapter. It's on the left side of the hexagon. It sends user
 * requests as command objects to the hexagon boundary. (For simplicitly,
 * sending is done autonomously without user interaction.)
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class Driver {
	private IReactToCommands hexagonBoundary;

	public Driver(IReactToCommands hexagonBoundary) {
		this.hexagonBoundary = hexagonBoundary;
	}

	public void run() {
		hexagonBoundary.reactTo(new AskForEnglishPoem());
		hexagonBoundary.reactTo(new AskForGermanPoem());
	}
}
