package poem.simple.drivenadapter;

import java.util.Objects;

import poem.hexagon.boundary.drivenport.IWriteLines;

/**
 * Right-side, driven adapter for writing text to the console.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class ConsoleWriter implements IWriteLines {
	public void writeLines(String[] lines) {
		Objects.requireNonNull(lines);
		for (String line : lines) {
			System.out.println(line);
		}
	}
}