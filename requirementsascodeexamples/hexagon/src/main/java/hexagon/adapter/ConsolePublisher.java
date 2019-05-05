package hexagon.adapter;

import hexagon.port.IWriteLines;

public class ConsolePublisher implements IWriteLines {
	public void writeLine(String text) {
		System.out.println(text);
		System.out.println();
		System.out.println();
	}
}