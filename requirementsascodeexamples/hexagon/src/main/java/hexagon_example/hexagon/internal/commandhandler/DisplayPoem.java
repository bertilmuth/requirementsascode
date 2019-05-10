package hexagon_example.hexagon.internal.commandhandler;

import hexagon_example.hexagon.boundary.driven_port.IObtainPoems;
import hexagon_example.hexagon.boundary.driven_port.IWriteLines;
import hexagon_example.hexagon.internal.domain.RandomPoemPicker;

/**
 * The command handler for displaying a poem.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal
 * architecture: https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class DisplayPoem implements Runnable {
	private IObtainPoems poetryLibrary;
	private RandomPoemPicker poemPicker;
	private IWriteLines publishingDevice;

	public DisplayPoem(IObtainPoems poetryLibrary, IWriteLines publishingDevice) {
		this.poetryLibrary = poetryLibrary;
		this.poemPicker = new RandomPoemPicker();
		this.publishingDevice = publishingDevice;
	}

	@Override
	public void run() {
		String[] poems = poetryLibrary.getMePoems();
		String verses = poemPicker.pickPoem(poems);
		publishingDevice.writeLine(verses);
	}
}
