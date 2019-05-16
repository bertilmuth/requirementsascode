package poem.simple.driveradapter;

import poem.hexagon.boundary.command.AskForPoem;
import poem.hexagon.boundary.driverport.IReactToCommands;

/**
 * The driver adapter. It's on the left side of the hexagon. It sends user
 * requests as command objects to a driver port on the hexagon boundary. (For simplicitly,
 * sending is done autonomously without user interaction.)
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class Driver {
	private IReactToCommands driverPort;

	public Driver(IReactToCommands driverPort) {
		this.driverPort = driverPort;
	}

	public void run() {
		driverPort.reactTo(new AskForPoem("en"));
		driverPort.reactTo(new AskForPoem("de"));
	}
}
