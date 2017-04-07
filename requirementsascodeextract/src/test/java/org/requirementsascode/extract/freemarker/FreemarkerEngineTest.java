package org.requirementsascode.extract.freemarker;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.extract.freemarker.systemreaction.GreetUser;
import org.requirementsascode.extract.freemarker.systemreaction.PromptUserToEnterName;
import org.requirementsascode.extract.freemarker.systemreaction.Quit;
import org.requirementsascode.extract.freemarker.userevent.DecideToQuit;
import org.requirementsascode.extract.freemarker.userevent.DontQuit;
import org.requirementsascode.extract.freemarker.userevent.EnterName;

public class FreeMarkerEngineTest {
  private FreeMarkerEngine engine;

  @Before
  public void setUp() throws Exception {
    engine = new FreeMarkerEngine();
  }
  
  @Test
  public void extractsEmptyStringFromEmptyModel() throws Exception {
    UseCaseModel useCaseModel = UseCaseModelBuilder.newBuilder().build();

    engine.put("useCaseModel", useCaseModel);
    File templateFile = new File("src/test/resources/testextract.ftl");
    StringWriter outputWriter = new StringWriter();
    engine.process(templateFile, outputWriter);
    String output = outputWriter.toString();
    
    assertEquals("", output);
  }
  
  @Test
  public void extractsUseCaseModel() throws Exception {
    UseCaseModel useCaseModel = 
    	UseCaseModelBuilder.newBuilder()
			.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(promptUserToEnterName())
					.step("S2").user(enterName()).system(greetUser())
					.step("S3").user(decideToQuit())
					.step("S4").system(quit())
				.flow("alternative flow").insteadOf("S4")
					.step("S4a_1").system(dontQuit())
    	.build();

    engine.put("useCaseModel", useCaseModel);
    File templateFile = new File("src/test/resources/testextract.ftl");
    StringWriter outputWriter = new StringWriter();
    engine.process(templateFile, outputWriter);
    String output = outputWriter.toString();

    assertEquals(
        "use case: Get greeted. flow: basic flow." 
        	+ " step: S1. System prompts user to enter name."
            + " step: S2. User enters name. System greets user."
            + " step: S3. User decides to quit."
            + " step: S4. System quits."
            + " flow: alternative flow."
        	+ " step: S4a_1. System donts quit.",
        output);
  }

@Test
  @Ignore
  public void printsUseCaseModelToConsole() throws Exception {
	UseCaseModel useCaseModel = UseCaseModelBuilder.newBuilder()
		.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(promptUserToEnterName())
				.step("S2").user(enterName()).system(greetUser())
				.step("S3").user(decideToQuit())
				.step("S4").system(quit())
	.build();

    engine.put("useCaseModel", useCaseModel);
    File templateFile = new File("src/test/resources/example.ftlh");
    File outputFile = File.createTempFile( "requirementsascodeextract_test", ".html");
    engine.process(templateFile, new FileWriter(outputFile));
    
    System.out.println("Wrote file to: " + outputFile);
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

  private Consumer<UseCaseModelRunner> dontQuit() { 
	  return new DontQuit();
  }
}