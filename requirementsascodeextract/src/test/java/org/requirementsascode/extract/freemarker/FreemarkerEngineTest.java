package org.requirementsascode.extract.freemarker;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.Actor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.extract.freemarker.predicate.SomeConditionIsFulfilled;
import org.requirementsascode.extract.freemarker.predicate.ThereIsNoAlternative;
import org.requirementsascode.extract.freemarker.systemreaction.BlowsUp;
import org.requirementsascode.extract.freemarker.systemreaction.GreetsUser;
import org.requirementsascode.extract.freemarker.systemreaction.LogsException;
import org.requirementsascode.extract.freemarker.systemreaction.PromptsUserToEnterName;
import org.requirementsascode.extract.freemarker.systemreaction.Quits;
import org.requirementsascode.extract.freemarker.userevent.DecidesToQuit;
import org.requirementsascode.extract.freemarker.userevent.EntersName;

public class FreemarkerEngineTest {
  private FreeMarkerEngine engine;

  @Before
  public void setUp() throws Exception {
    engine = new FreeMarkerEngine("org/requirementsascode/extract/freemarker");
  }
  
  @Test
  public void extractsEmptyStringFromEmptyModel() throws Exception {
    Model model = Model.builder().build();
    String templateFileName = "testextract.ftl";
    StringWriter outputWriter = new StringWriter();
    
    engine.extract(model, templateFileName, outputWriter);
    String output = outputWriter.toString();
    
    assertEquals("", output);
  }
  
  @Test
  public void extractsUseCaseModel() throws Exception {
    
    ModelBuilder modelBuilder = Model.builder();
    Actor firstActor = modelBuilder.actor("First actor");
    Actor secondActor = modelBuilder.actor("Second actor");
    
    Model model = 
      modelBuilder
        .useCase("Included use case")
          .basicFlow()
            .step("Included step").system(promptsUserToEnterName())
        .useCase("Get greeted")
          .basicFlow()
            .step("S1").system(promptsUserToEnterName())
            .step("S2").user(entersName()).system(greetsUser())
            .step("S3").as(firstActor).user(entersName()).system(greetsUser()).reactWhile(someConditionIsFulfilled())
            .step("S4").as(firstActor, secondActor).user(decidesToQuit())
            .step("S5").as(firstActor, secondActor).system(promptsUserToEnterName())
            .step("S6").system(quits())
          .flow("Alternative flow A").insteadOf("S4")
            .step("S4a_1").system(blowsUp())
            .step("S4a_2").continuesAt("S1")
          .flow("Alternative flow B").after("S3")
            .step("S4b_1").continuesAfter("S2")
          .flow("Alternative flow C").condition(thereIsNoAlternative())
            .step("S5a").continuesWithoutAlternativeAt("S4")
          .flow("Alternative flow D").insteadOf("S4").condition(thereIsNoAlternative())
            .step("S4c_2").continuesAt("S1")
          .flow("EX").anytime()
          	.step("EX1").on(Exception.class).system(logsException())
        .build();    
    
    String templateFileName = "testextract.ftl";
    Writer outputWriter = new StringWriter();
    
    engine.extract(model, templateFileName, outputWriter);
    String output = outputWriter.toString();

    assertEquals(
        "Use case: Included use case. Flow: Basic flow"
            + " Step: Included step. System prompts user to enter name."
        + " Use case: Get greeted. Flow: Basic flow"
              + " Step: S1. System prompts user to enter name."
              + " Step: S2. User enters name.System greets user."
              + " Step: S3. As long as some condition is fulfilled: As First actor: User enters name.System greets user."
              + " Step: S4. As First actor/Second actor: User decides to quit."
              + " Step: S5. As First actor/Second actor: System prompts user to enter name."
              + " Step: S6. System quits."
            + " Flow: Alternative flow A Instead of S4:"
              + " Step: S4a_1. System blows up." 
              + " Step: S4a_2. System continues at S1."
            + " Flow: Alternative flow B After S3:"
              + " Step: S4b_1. System continues after S2."
            + " Flow: Alternative flow C Anytime, when there is no alternative:"
              + " Step: S5a. System continues without alternative at S4."
            + " Flow: Alternative flow D Instead of S4, when there is no alternative:"
              + " Step: S4c_2. System continues at S1."
            + " Flow: EX Anytime:"
              + " Step: EX1. On Exception: System logs exception.",
        output);
  }
  
    @Test
    public void extractsFlowlessModel() throws Exception {
	Model model = Model.builder()
		.on(entersName()).system(greetsUser())
		.on(Exception.class).system(logsException())
	.build();
  
	String templateFileName = "testextract_flowless.ftl";
	Writer outputWriter = new StringWriter();

	engine.extract(model, templateFileName, outputWriter);
	String output = outputWriter.toString();

          assertEquals(
              "Use case: Handles events."
                    + " Step: S1. On EntersName: System greets user."                 
                    + " Step: S2. On Exception: System logs exception.",
              output);
  }

  private Condition thereIsNoAlternative() {
    return new ThereIsNoAlternative();
  }

  private Runnable promptsUserToEnterName() {
    return new PromptsUserToEnterName();
  }

  private Class<EntersName> entersName() {
    return EntersName.class;
  }

  private Consumer<EntersName> greetsUser() {
    return new GreetsUser();
  }
  
  private Condition someConditionIsFulfilled() {
    return new SomeConditionIsFulfilled();
  }

  private Class<DecidesToQuit> decidesToQuit() {
    return DecidesToQuit.class;
  }

  private Runnable quits() {
    return new Quits();
  }

  private Runnable blowsUp() { 
	  return new BlowsUp();
  }
  
  private Consumer<Exception> logsException() {
    return new LogsException();
  }
}
