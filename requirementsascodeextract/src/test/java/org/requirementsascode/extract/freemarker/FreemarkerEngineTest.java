package org.requirementsascode.extract.freemarker;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.StringWriter;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.extract.freemarker.predicate.ThereIsNoAlternative;
import org.requirementsascode.extract.freemarker.systemreaction.GreetUser;
import org.requirementsascode.extract.freemarker.systemreaction.PromptUserToEnterName;
import org.requirementsascode.extract.freemarker.systemreaction.Quit;
import org.requirementsascode.extract.freemarker.userevent.BlowUp;
import org.requirementsascode.extract.freemarker.userevent.DecideToQuit;
import org.requirementsascode.extract.freemarker.userevent.EnterName;

public class FreeMarkerEngineTest {
  private FreeMarkerEngine engine;
  private UseCaseModel useCaseModel;

  @Before
  public void setUp() throws Exception {
    engine = new FreeMarkerEngine();
    useCaseModel = buildWithNewBuilder();
  }
  
  private UseCaseModel buildWithNewBuilder(){
    UseCaseModel useCaseModel = 
    	UseCaseModelBuilder.newBuilder()
			.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(promptUserToEnterName())
					.step("S2").user(enterName()).system(greetUser())
					.step("S3").user(decideToQuit())
					.step("S4").system(quit())
				.flow("Alternative Flow A").insteadOf("S4")
					.step("S4a_1").system(blowUp())
					.step("S4a_2").continueAt("S1")
				.flow("Alternative Flow B").after("S3")
					.step("S4b_1").continueAfter("S2")
				.flow("Alternative Flow C").when(thereIsNoAlternative())
					.step("S5").continueWithoutAlternativeAt("S4")
				.flow("Alternative Flow D").insteadOf("S4").when(thereIsNoAlternative())
					.step("S6").continueAt("S1")
    	.build();
    return useCaseModel;
  }
  
  @Test
  public void extractsEmptyStringFromEmptyModel() throws Exception {
    UseCaseModel useCaseModel = UseCaseModelBuilder.newBuilder().build();
    File templateFile = new File("src/test/resources/testextract.ftl");
    StringWriter outputWriter = new StringWriter();
    engine.extract(useCaseModel, templateFile, outputWriter);
    String output = outputWriter.toString();
    
    assertEquals("", output);
  }
  
  @Test
  public void extractsUseCaseModel() throws Exception {
    File templateFile = new File("src/test/resources/testextract.ftl");
    StringWriter outputWriter = new StringWriter();
    engine.extract(useCaseModel, templateFile, outputWriter);
    String output = outputWriter.toString();

    assertEquals(
        "use case: Get greeted. flow: basic flow"
            + " step: S1. System prompts user to enter name."
            + " step: S2. User enters name. System greets user."
            + " step: S3. User decides to quit."
            + " step: S4. System quits."
            + " flow: Alternative Flow A Instead of S4:"
            + " step: S4a_1. System blows up."
            + " step: S4a_2. System continues at S1."
            + " flow: Alternative Flow B After S3:"
            + " step: S4b_1. System continues after S2."
            + " flow: Alternative Flow C When there is no alternative:"
            + " step: S5. System continues without alternative at S4."
            + " flow: Alternative Flow D Instead of S4, when there is no alternative:"
            + " step: S6. System continues at S1.", 
        output);
  }

  private Predicate<UseCaseModelRunner> thereIsNoAlternative() {
    return new ThereIsNoAlternative();
  }

  private Consumer<UseCaseModelRunner> promptUserToEnterName() {
    return new PromptUserToEnterName();
  }

  private Class<EnterName> enterName() {
    return EnterName.class;
  }

  private Consumer<EnterName> greetUser() {
    return new GreetUser();
  }

  private Class<DecideToQuit> decideToQuit() {
    return DecideToQuit.class;
  }

  private Consumer<UseCaseModelRunner> quit() {
    return new Quit();
  }

  private Consumer<UseCaseModelRunner> blowUp() { 
	  return new BlowUp();
  }
}
