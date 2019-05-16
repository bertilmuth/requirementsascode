package poem.hexagon.boundary.command;

/**
 * Command object representing the user request for a poem in a certain
 * language. Supported languages are: "de" for German, "en" for English.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class AskForPoem {
	private String language;

	public AskForPoem(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}
}
