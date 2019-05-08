package hexagonal_architecture.port.driven;

/**
 * Driven, right side port for obtaining poems, e.g. from a repository outside
 * the hexagon.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public interface IObtainPoems {
	String[] getMePoems();
}