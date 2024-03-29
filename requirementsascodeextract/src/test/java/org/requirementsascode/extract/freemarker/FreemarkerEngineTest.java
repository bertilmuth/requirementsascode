package org.requirementsascode.extract.freemarker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.requirementsascode.Actor;
import org.requirementsascode.Behavior;
import org.requirementsascode.BehaviorModel;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;
import org.requirementsascode.extract.freemarker.predicate.SomeConditionIsFulfilled;
import org.requirementsascode.extract.freemarker.predicate.ThereIsNoAlternative;
import org.requirementsascode.extract.freemarker.systemreaction.BlowsUp;
import org.requirementsascode.extract.freemarker.systemreaction.GreetsUser;
import org.requirementsascode.extract.freemarker.systemreaction.LogsException;
import org.requirementsascode.extract.freemarker.systemreaction.NameEntered;
import org.requirementsascode.extract.freemarker.systemreaction.PromptsUserToEnterName;
import org.requirementsascode.extract.freemarker.systemreaction.Quits;
import org.requirementsascode.extract.freemarker.usercommand.DecidesToQuit;
import org.requirementsascode.extract.freemarker.usercommand.EntersName;

public class FreemarkerEngineTest {
  private FreeMarkerEngine engine;

  @BeforeEach
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

    Actor firstActor = new Actor("First actor");
    Actor secondActor = new Actor("Second actor");

    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").system(promptsUserToEnterName())
          .step("S2").user(entersName()).system(greetsUser())
          .step("S3").as(firstActor).user(entersName()).system(greetsUser())
            .reactWhile(someConditionIsFulfilled())
          .step("S4").as(firstActor, secondActor).user(decidesToQuit())
          .step("S5").as(firstActor, secondActor).system(promptsUserToEnterName())
          .step("S6").inCase(thereIsNoAlternative()).system(quits())
        .flow("Alternative flow A").insteadOf("S4")
          .step("S4a_1").system(blowsUp())
          .step("S4a_2").continuesAt("S1")
        .flow("Alternative flow B").after("S3", "S4")
          .step("S4b_1").continuesAfter("S2")
        .flow("Alternative flow C").condition(thereIsNoAlternative())
          .step("S5a").continuesAt("S4")
        .flow("Alternative flow D").insteadOf("S4").condition(thereIsNoAlternative())
          .step("S4c_2").continuesAt("S1")
        .flow("EX").anytime()
          .step("EX1").on(Exception.class).system(logsException()).build();

    String templateFileName = "testextract.ftl";
    Writer outputWriter = new StringWriter();

    engine.extract(model, templateFileName, outputWriter);
    String output = outputWriter.toString();

    assertEquals("Use case: Get greeted. Flow: Basic flow" 
      + " Step: S1. System prompts user to enter name."
      + " Step: S2. User enters name.System greets user."
      + " Step: S3. As long as some condition is fulfilled: As First actor: User enters name.System greets user."
      + " Step: S4. As First actor/Second actor: User decides to quit."
      + " Step: S5. As First actor/Second actor: System prompts user to enter name." 
      + " Step: S6. In case there is no alternative, System quits."
      + " Flow: Alternative flow A Instead of S4:" 
      + " Step: S4a_1. System blows up."
      + " Step: S4a_2. System continues at S1." 
      + " Flow: Alternative flow B After S3,S4:"
      + " Step: S4b_1. System continues after S2." 
      + " Flow: Alternative flow C When there is no alternative:"
      + " Step: S5a. System continues at S4."
      + " Flow: Alternative flow D Instead of S4, when there is no alternative:"
      + " Step: S4c_2. System continues at S1." 
      + " Flow: EX"
      + " Step: EX1. On Exception: System logs exception.", output);
  }

  @Test
  public void extractsFlowlessModel() throws Exception {
    Actor anotherActor = new Actor("another actor");
    DummyBehavior dummyBehavior = new DummyBehavior();

    Model model = Model.builder()
      .on(entersName()).system(greetsUser())
      .on(entersName()).systemPublish(nameEntered())
      .condition(thereIsNoAlternative()).step("Three")
        .on(entersName()).systemPublish(nameEntered()).to(anotherActor)
      .on(entersName()).systemPublish(nameEntered()).to(dummyBehavior)
      .on(Exception.class).system(logsException())
      .build();

    String templateFileName = "testextract_flowless.ftl";
    Writer outputWriter = new StringWriter();

    engine.extract(model, templateFileName, outputWriter);
    String output = outputWriter.toString();

    assertEquals("Use case: Handles messages." + " Step: S1. On EntersName: System greets user."
      + " Step: S2. On EntersName: System publishes name entered."
      + " Step: Three. When there is no alternative: On EntersName: System publishes name entered to another actor."
      + " Step: S4. On EntersName: System publishes name entered to DummyBehavior."
      + " Step: S5. On Exception: System logs exception.", 
      output);
  }
  
  private class DummyBehavior implements Behavior{

    @Override
    public <T> Optional<T> reactTo(Object message) {
      return null;
    }

    @Override
    public BehaviorModel behaviorModel() {
      return null;
    }
    
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

  private Function<EntersName, String> nameEntered() {
    return new NameEntered();
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
