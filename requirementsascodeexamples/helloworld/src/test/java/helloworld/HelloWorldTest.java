package helloworld;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.requirementsascode.RecordingActor;

import helloworld.actor.AnonymousUser;
import helloworld.actor.InvalidUser;
import helloworld.actor.NormalUser;
import helloworld.actor.ValidUser;
import helloworld.command.EnterText;
import helloworld.commandhandler.SaveAge;
import helloworld.commandhandler.SaveName;
import helloworld.domain.Color;
import helloworld.domain.Person;

public class HelloWorldTest {
  @Test
  public void testHelloWorld01() {
    RecordingActor actor = 
      RecordingActor.basedOn(new HelloWorldActor01(() -> {}));

    actor.run();
    assertRecordedStepNames(actor, "S1");
  }

  @Test
  public void testHelloWorld02() {
    RecordingActor actor = 
      RecordingActor.basedOn(new HelloWorldActor02(() -> {}));

    actor.run();
    assertRecordedStepNames(actor, "S1", "S1", "S1");
  }

  @Test
  public void testHelloWorld03() {
    RecordingActor actor = 
      RecordingActor.basedOn(new HelloWorldActor03(et -> {}));

    actor.reactTo(new EnterText("John Q. Public"));
    assertRecordedStepNames(actor, "S1");
  }

  @Test
  public void testHelloWorld03a_validUser() {
    HelloWorldActor03a helloWorldActor = new HelloWorldActor03a(et -> {});

    RecordingActor actor = 
      RecordingActor.basedOn(helloWorldActor);
    ValidUser validUser03a = new ValidUser(actor);
    helloWorldActor.setValidUser(validUser03a);
    
    validUser03a.run();
    assertRecordedStepNames(actor, "S1");
  }

  @Test
  public void testHelloWorld03a_invalidUser() {
    HelloWorldActor03a helloWorldActor = new HelloWorldActor03a(et -> {});
    
    RecordingActor actor = 
      RecordingActor.basedOn(helloWorldActor);
    ValidUser validUser03a = new ValidUser(actor);
    helloWorldActor.setValidUser(validUser03a);
    
    InvalidUser invalidUser03a = new InvalidUser(actor);
    invalidUser03a.run();
    assertRecordedStepNames(actor);
  }

  @Test
  public void testHelloWorld04() {
    RecordingActor actor = 
      RecordingActor.basedOn(new HelloWorldActor04(et -> {}, et -> {}, () -> {}));

    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("39"));
    assertRecordedStepNames(actor, "S1", "S2", "S3");
  }

  @Test
  public void testHelloWorld05_WithCorrectNameAndAge() {
    Person person = new Person();
    HelloWorldActor05 helloWorldActor05 = 
      new HelloWorldActor05(new SaveName(person), new SaveAge(person), () -> {}, person::ageIsOutOfBounds);
    RecordingActor actor = RecordingActor.basedOn(helloWorldActor05);

    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("39"));
    assertRecordedStepNames(actor, "S1", "S2", "S3");
  }

  @Test
  public void testHelloWorld05_WithOutOfBoundsAge() {
    Person person = new Person();
    HelloWorldActor05 helloWorldActor05 =
      new HelloWorldActor05(new SaveName(person), new SaveAge(person), () -> {}, person::ageIsOutOfBounds);
    RecordingActor actor = RecordingActor.basedOn(helloWorldActor05);

    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("1000"));
    actor.reactTo(new EnterText("43"));
    assertRecordedStepNames(actor, "S1", "S2", "S3a_1", "S2", "S3");
  }

  @Test
  public void testHelloWorld05_WithNonNumericalAge() {
    Person person = new Person();
    HelloWorldActor05 helloWorldActor05 = 
      new HelloWorldActor05(new SaveName(person), new SaveAge(person), () -> {}, person::ageIsOutOfBounds);
    RecordingActor actor = RecordingActor.basedOn(helloWorldActor05);

    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("NON-NUMERICAL-AGE"));
    actor.reactTo(new EnterText("43"));
    assertRecordedStepNames(actor, "S1", "S2", "S3b_1", "S2", "S3");
  }

  @Test
  public void testHelloWorld06_AsNormalUser() {
    Person person = new Person();
    HelloWorldActor06 helloWorldActor06 = 
      new HelloWorldActor06(new SaveName(person), new SaveAge(person), () -> {}, () -> {}, 
        person::ageIsOk, person::ageIsOutOfBounds);
    RecordingActor actor = RecordingActor.basedOn(helloWorldActor06);

    NormalUser normalUser = new NormalUser(actor, "Jane", "21");
    AnonymousUser anonymousUser = new AnonymousUser(actor, "43");
    helloWorldActor06.setNormalUser(normalUser);
    helloWorldActor06.setAnonymousUser(anonymousUser);
    normalUser.run();
    assertRecordedStepNames(actor, "S1", "S2", "S3", "S4");
  }

  @Test
  public void testHelloWorld06_AsNormalUserHandleOutOfBoundsAgeAndThenOkAge() {
    Person person = new Person();
    HelloWorldActor06 helloWorldActor06 = 
      new HelloWorldActor06(new SaveName(person), new SaveAge(person), () -> {}, () -> {}, 
        person::ageIsOk, person::ageIsOutOfBounds);
    RecordingActor actor = RecordingActor.basedOn(helloWorldActor06);

    NormalUser normalUser = new NormalUser(actor, "Methusalem", "1000");
    AnonymousUser anonymousUser = new AnonymousUser(actor, "43");
    helloWorldActor06.setNormalUser(normalUser);
    helloWorldActor06.setAnonymousUser(anonymousUser);
    normalUser.run();
    assertRecordedStepNames(actor, "S1", "S2", "S3a_1", "S2", "S3", "S4");
  }

  @Test
  public void testHelloWorld06_AsAnonymousUserHandleNonNumericalAgeAndThenOkAge() {
    Person person = new Person();
    HelloWorldActor06 helloWorldActor06 = 
      new HelloWorldActor06(new SaveName(person), new SaveAge(person), () -> {}, () -> {}, 
        person::ageIsOk, person::ageIsOutOfBounds);
    RecordingActor actor = RecordingActor.basedOn(helloWorldActor06);

    NormalUser normalUser = new NormalUser(actor, "Jane", "21");
    AnonymousUser anonymousUser = new AnonymousUser(actor, "NON-NUMERICAL-AGE");
    helloWorldActor06.setNormalUser(normalUser);
    helloWorldActor06.setAnonymousUser(anonymousUser);
    anonymousUser.run();
    assertRecordedStepNames(actor, "S1a_1", "S3b_1", "S2", "S3c_1", "S4");
  }

  @Test
  public void testHelloWorld06_AsAnonymousUserAgeIsOk() {
    Person person = new Person();
    HelloWorldActor06 helloWorldActor06 = 
      new HelloWorldActor06(new SaveName(person), new SaveAge(person), () -> {}, () -> {}, 
        person::ageIsOk, person::ageIsOutOfBounds);
    RecordingActor actor = RecordingActor.basedOn(helloWorldActor06);

    NormalUser normalUser = new NormalUser(actor, "Jane", "21");
    AnonymousUser anonymousUser = new AnonymousUser(actor, "43");
    helloWorldActor06.setNormalUser(normalUser);
    helloWorldActor06.setAnonymousUser(anonymousUser);
    helloWorldActor06.run();
    anonymousUser.run();
    assertRecordedStepNames(actor, "S1a_1", "S1a_2", "S3c_1", "S4");
  }

  @Test
  public void testHelloWorld07() {
    Color color = new Color();
    HelloWorldActor07 helloWorldActor07 = 
      new HelloWorldActor07(color::isInputColorRed, color::isInputColorYellow,
      color::isInputColorGreen, color::setOutputColorToRed, color::setOutputColorToYellow, 
      color::setOutputColorToGreen, () -> {});

    color.setInputColor("yellow");
    RecordingActor actor = RecordingActor.basedOn(helloWorldActor07);
    actor.run();

    assertEquals("yellow", color.getOutputColor());
  }

  protected void assertRecordedStepNames(RecordingActor actor, String... expectedStepNames) {
    String[] actualStepNames = actor.getRecordedStepNames();
    assertArrayEquals(expectedStepNames, actualStepNames);
  }
}
