package hexagon.application;

import org.requirementsascode.Model;

class UseCaseModel {	
	private static final Class<AskForGermanPoem> askForGermanPoem = AskForGermanPoem.class;
	private static final Class<AskForEnglishPoem> askForEnglishPoem = AskForEnglishPoem.class;

	public static Model build(Runnable displayEnglishPoem, Runnable displayGermanPoem) {
		Model model = Model.builder()
			.useCase("Read a poem")
				.on(askForEnglishPoem).system(displayEnglishPoem)
				.on(askForGermanPoem).system(displayGermanPoem)
		.build();

		return model;
	}
}
