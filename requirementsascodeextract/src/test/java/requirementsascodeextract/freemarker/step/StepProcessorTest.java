package requirementsascodeextract.freemarker.step;

import static org.junit.Assert.assertEquals;
import static requirementsascodeextract.freemarker.step.StepProcessor.as;
import static requirementsascodeextract.freemarker.step.StepProcessor.system;
import static requirementsascodeextract.freemarker.step.StepProcessor.user;

import org.junit.Test;
import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModelBuilder;

import requirementsascodeextract.freemarker.systemreaction.GreetUser;
import requirementsascodeextract.freemarker.userevent.EnterName;

public class StepProcessorTest {
  @Test
  public void extractsWordsOfSingleNamelessActor() {
    UseCaseModelBuilder builder = UseCaseModelBuilder.newBuilder();
    Actor[] actors = new Actor[] {builder.actor("")};
    assertEquals("", as(actors, " & "));
  }

  @Test
  public void extractsWordsOfSingleNamedActor() {
    UseCaseModelBuilder builder = UseCaseModelBuilder.newBuilder();
    Actor[] actors = new Actor[] {builder.actor("admin")};
    assertEquals("admin", as(actors, " & "));
  }

  @Test
  public void extractsWordsOfTwoActors() {
    UseCaseModelBuilder builder = UseCaseModelBuilder.newBuilder();
    Actor[] actors = new Actor[] {builder.actor("EndCustomer"), builder.actor("admin")};
    assertEquals("end customer & admin", as(actors, " & "));
  }

  @Test
  public void extractsWordsOfSystemReaction() {
    assertEquals("greet user", system(new GreetUser()));
  }

  @Test
  public void extractsWordsOfUserEvent() {
    assertEquals("enter name", user(EnterName.class));
  }
}
