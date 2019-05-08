package hexagon_example.hexagon.application.system_reaction;

import hexagon_example.hexagon.application.domain.RandomPoemPicker;
import hexagon_example.hexagon.driven_port.IObtainPoems;
import hexagon_example.hexagon.driven_port.IWriteLines;

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

	public DisplayPoem(IObtainPoems poetryLibrary, RandomPoemPicker poemPicker, IWriteLines publishingDevice) {
		this.poetryLibrary = poetryLibrary;
		this.poemPicker = poemPicker;
		this.publishingDevice = publishingDevice;
	}

	@Override
	public void run() {
		String[] poems = poetryLibrary.getMePoems();
		String verses = poemPicker.pickPoem(poems);
		publishingDevice.writeLine(verses);
	}
}
