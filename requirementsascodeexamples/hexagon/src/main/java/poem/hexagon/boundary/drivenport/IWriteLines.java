package poem.hexagon.boundary.drivenport;

/**
 * Driven, right side port for writing the lines of a poem to an output device
 * outside the hexagon, e.g. the console.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public interface IWriteLines {
	void writeLine(String text);
}