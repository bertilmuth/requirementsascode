package hexagonexample.hexagon.boundary;

import org.requirementsascode.Model;

/**
 * The use case model ties each type of command to its appropriate command handler interface.
 * 
 * In business terms, this example model means:
 * 
 * Either
 * 	Alternative A. The user asks for an English poem. The system displays the English poem.
 * Or
 * 	Alternative B. The user asks for a German poem. The system displays the German poem.
 * 
 * @author b_muth
 *
 */
class UseCaseModel {	
	private static final Class<AskForGermanPoem> askForGermanPoem = AskForGermanPoem.class;
	private static final Class<AskForEnglishPoem> askForEnglishPoem = AskForEnglishPoem.class;

	public static Model build(Runnable displayEnglishPoem, Runnable displayGermanPoem) {
		Model model = Model.builder()
			.useCase("Read a poem")
				.user(askForEnglishPoem).system(displayEnglishPoem)
				.user(askForGermanPoem).system(displayGermanPoem)
		.build();

		return model;
	}
}
