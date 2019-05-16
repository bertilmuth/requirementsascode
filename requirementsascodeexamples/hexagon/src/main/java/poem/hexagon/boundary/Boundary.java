package poem.hexagon.boundary;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import poem.hexagon.boundary.drivenport.IObtainPoems;
import poem.hexagon.boundary.drivenport.IWriteLines;
import poem.hexagon.boundary.driverport.IReactToCommands;
import poem.hexagon.internal.commandhandler.DisplayRandomPoem;

/**
 * The boundary class is the only point of communication with left-side driver
 * adapters. It accepts commands, and calls the appropriate command handler.
 * 
 * On creation, this class wires up the dependencies between command types and
 * command handlers, by injecting the command handlers into a use case model.
 * 
 * After creation, this class sends each command it receives to the runner
 * of the use case model. The model runner then dispatches the command to the
 * appropriate command handler, which in turn calls the driven adapters.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class Boundary implements IReactToCommands {

	private ModelRunner modelRunner;

	public Boundary(IObtainPoems poetryLibrary, IWriteLines publishingDevice) {
		// Create a use case model
		Model model = buildModel(poetryLibrary, publishingDevice);
		
		// Run the use case model
		modelRunner = new ModelRunner().run(model);
	}

	private Model buildModel(IObtainPoems poetryLibrary, IWriteLines publishingDevice) {
		// Create the command handler(s)
		DisplayRandomPoem displayRandomPoem = new DisplayRandomPoem(poetryLibrary, publishingDevice);
		
		// Inject command handler(s) into use case model, to wire them up with command types.
		Model model = UseCaseModel.build(displayRandomPoem); 
		return model;
	}

	public void reactTo(Object commandObject) {
		modelRunner.reactTo(commandObject);
	}
}