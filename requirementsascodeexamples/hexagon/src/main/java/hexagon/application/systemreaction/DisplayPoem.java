package hexagon.application.systemreaction;

import hexagon.domain.RandomPoemPicker;
import hexagon.port.IObtainPoems;
import hexagon.port.IWriteLines;

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
