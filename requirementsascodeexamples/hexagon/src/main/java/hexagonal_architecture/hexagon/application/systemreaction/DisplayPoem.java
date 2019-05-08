package hexagonal_architecture.hexagon.application.systemreaction;

import hexagonal_architecture.hexagon.domain.RandomPoemPicker;
import hexagonal_architecture.hexagon.port.driven.IObtainPoems;
import hexagonal_architecture.hexagon.port.driven.IWriteLines;

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
