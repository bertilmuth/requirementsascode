package org.requirementsascode.extract.freemarker;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.extract.freemarker.predicate.SomeConditionIsFulfilled;
import org.requirementsascode.extract.freemarker.predicate.ThereIsNoAlternative;
import org.requirementsascode.extract.freemarker.systemreaction.BlowsUp;
import org.requirementsascode.extract.freemarker.systemreaction.GreetsUser;
import org.requirementsascode.extract.freemarker.systemreaction.PromptsUserToEnterName;
import org.requirementsascode.extract.freemarker.systemreaction.Quits;
import org.requirementsascode.extract.freemarker.userevent.DecidesToQuit;
import org.requirementsascode.extract.freemarker.userevent.EntersName;
import org.requirementsascode.systemreaction.IgnoreIt;

public class FreemarkerEngineTest {
  private FreeMarkerEngine engine;

  @Before
  public void setUp() throws Exception {
    engine = new FreeMarkerEngine("org/requirementsascode/extract/freemarker");
  }
  
  @Test
  public void extractsEmptyStringFromEmptyModel() throws Exception {
    UseCaseModel useCaseModel = UseCaseModelBuilder.newBuilder().build();
    String templateFileName = "testextract.ftl";
    StringWriter outputWriter = new StringWriter();
    
    engine.extract(useCaseModel, templateFileName, outputWriter);
    String output = outputWriter.toString();
    
    assertEquals("", output);
  }
  
  @Test
  public void extractsUseCaseModel() throws Exception {
    UseCaseModel useCaseModel = 
        UseCaseModelBuilder.newBuilder()
        .useCase("Included Use Case")
          .basicFlow()
            .step("Included Step").system(new IgnoreIt<>())
        .useCase("Get greeted")
          .basicFlow()
            .step("S1").system(promptsUserToEnterName())
            .step("S2").user(entersName()).system(greetsUser()).reactWhile(someConditionIsFulfilled())
            .step("S3").user(decidesToQuit())
            .step("S4").system(quits())
          .flow("Alternative Flow A").insteadOf("S4")
            .step("S4a_1").system(blowsUp())
            .step("S4a_2").continuesAt("S1")
          .flow("Alternative Flow B").after("S3")
            .step("S4b_1").continuesAfter("S2")
          .flow("Alternative Flow C").when(thereIsNoAlternative())
            .step("S5").continuesWithoutAlternativeAt("S4")
          .flow("Alternative Flow D").insteadOf("S4").when(thereIsNoAlternative())
            .step("S4c_1").includesUseCase("Included Use Case")
            .step("S4c_2").continuesAt("S1")
        .build();    
    
    String templateFileName = "testextract.ftl";
    Writer outputWriter = new StringWriter();
    
    engine.extract(useCaseModel, templateFileName, outputWriter);
    String output = outputWriter.toString();

    assertEquals(
        "use case: Included Use Case. flow: basic flow"
            + " step: Included Step."
        + " use case: Get greeted. flow: basic flow"
            + " step: S1. System prompts user to enter name."
            + " step: S2. As long as some condition is fulfilled: User enters name. System greets user."
            + " step: S3. User decides to quit."
            + " step: S4. System quits."
            + " flow: Alternative Flow A Instead of S4:"
            + " step: S4a_1. System blows up." 
            + " step: S4a_2. System continues at S1."
            + " flow: Alternative Flow B After S3:"
            + " step: S4b_1. System continues after S2."
            + " flow: Alternative Flow C Anytime, when there is no alternative:"
            + " step: S5. System continues without alternative at S4."
            + " flow: Alternative Flow D Instead of S4, when there is no alternative:"
            + " step: S4c_1. System includes use case Included Use Case."
            + " step: S4c_2. System continues at S1.",
        output);
  }

  private Predicate<UseCaseModelRunner> thereIsNoAlternative() {
    return new ThereIsNoAlternative();
  }

  private Consumer<UseCaseModelRunner> promptsUserToEnterName() {
    return new PromptsUserToEnterName();
  }

  private Class<EntersName> entersName() {
    return EntersName.class;
  }

  private Consumer<EntersName> greetsUser() {
    return new GreetsUser();
  }
  
  private Predicate<UseCaseModelRunner> someConditionIsFulfilled() {
    return new SomeConditionIsFulfilled();
  }

  private Class<DecidesToQuit> decidesToQuit() {
    return DecidesToQuit.class;
  }

  private Consumer<UseCaseModelRunner> quits() {
    return new Quits();
  }

  private Consumer<UseCaseModelRunner> blowsUp() { 
	  return new BlowsUp();
  }
}
