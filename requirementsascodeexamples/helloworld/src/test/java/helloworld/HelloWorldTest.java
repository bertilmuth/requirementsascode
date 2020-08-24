package helloworld;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.requirementsascode.AbstractActor;

import helloworld.actor.AnonymousUser;
import helloworld.actor.InvalidUser;
import helloworld.actor.NormalUser;
import helloworld.actor.ValidUser;
import helloworld.command.EnterText;

public class HelloWorldTest {
	@Test
	public void testHelloWorld01() {
		HelloWorldActor01 actor = new HelloWorldActor01(() -> {});
		
		recordStepNamesOf(actor);
		actor.run();
		assertRecordedStepNames(actor, "S1");
	}

	@Test
	public void testHelloWorld02() {
	  HelloWorldActor02 actor = new HelloWorldActor02(() -> {});
	  
    recordStepNamesOf(actor);
    actor.run();
		assertRecordedStepNames(actor, "S1", "S1", "S1");
	}

	@Test
	public void testHelloWorld03() {
		HelloWorldActor03 actor = new HelloWorldActor03(et -> {});
		 
    recordStepNamesOf(actor);
		actor.reactTo(new EnterText("John Q. Public"));
		assertRecordedStepNames(actor, "S1");
	}

  @Test
  public void testHelloWorld03a_validUser() {
    HelloWorldActor03a helloWorldActor = new HelloWorldActor03a(et -> {});
    ValidUser validUser03a = new ValidUser(helloWorldActor);
    helloWorldActor.setValidUser(validUser03a);
    
    recordStepNamesOf(helloWorldActor);
    validUser03a.run();
    assertRecordedStepNames(helloWorldActor, "S1b");
  }

  @Test
  public void testHelloWorld03a_invalidUser() {
    HelloWorldActor03a helloWorldActor = new HelloWorldActor03a(et -> {});
    ValidUser validUser03a = new ValidUser(helloWorldActor);
    InvalidUser invalidUser03a = new InvalidUser(helloWorldActor);
    helloWorldActor.setValidUser(validUser03a);

    recordStepNamesOf(helloWorldActor);
    invalidUser03a.run();
    assertRecordedStepNames(helloWorldActor);
  }

  @Test
	public void testHelloWorld04() {
		HelloWorldActor04 actor = new HelloWorldActor04(et -> {}, et -> {}, () -> {});
    
		recordStepNamesOf(actor);
		actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("39"));
		assertRecordedStepNames(actor, "S1", "S2", "S3");
	}

	@Test
	public void testHelloWorld05_WithCorrectNameAndAge() {
    HelloWorldActor05 actor = new HelloWorldActor05(HelloWorld05.saveName, HelloWorld05.saveAge, HelloWorld05.greetUser,
      HelloWorld05.ageIsOutOfBounds);
    
    recordStepNamesOf(actor);
    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("39"));
		assertRecordedStepNames(actor, "S1", "S2", "S3");
	}

  @Test
  public void testHelloWorld05_WithOutOfBoundsAge() {
    HelloWorldActor05 actor = new HelloWorldActor05(HelloWorld05.saveName, HelloWorld05.saveAge, HelloWorld05.greetUser,
      HelloWorld05.ageIsOutOfBounds);

    recordStepNamesOf(actor);
    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("1000"));
    actor.reactTo(new EnterText("43"));
    assertRecordedStepNames(actor, "S1", "S2", "S3a_1", "S2", "S3");
  }

  @Test
  public void testHelloWorld05_WithNonNumericalAge() {
    HelloWorldActor05 actor = new HelloWorldActor05(HelloWorld05.saveName, HelloWorld05.saveAge,
      HelloWorld05.greetUser, HelloWorld05.ageIsOutOfBounds);

    recordStepNamesOf(actor);
    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("NON-NUMERICAL-AGE"));
    actor.reactTo(new EnterText("43"));
    assertRecordedStepNames(actor, "S1", "S2", "S3b_1", "S2", "S3");
  }

	@Test
  public void testHelloWorld06_AsNormalUser() {
    HelloWorldActor06 helloWorldActor = new HelloWorldActor06(HelloWorld06.saveName, HelloWorld06.saveAge,
      HelloWorld06.greetUserWithName, HelloWorld06.greetUserWithAge, HelloWorld06.ageIsOk, HelloWorld06.ageIsOutOfBounds);

    recordStepNamesOf(helloWorldActor);
    NormalUser normalUser = new NormalUser(helloWorldActor, "Jane", "21");
    AnonymousUser anonymousUser = new AnonymousUser(helloWorldActor, "43");
    helloWorldActor.setNormalUser(normalUser);
    helloWorldActor.setAnonymousUser(anonymousUser);
    normalUser.run(); 
		assertRecordedStepNames(helloWorldActor, "S1", "S2", "S3", "S4");
	}
	
  @Test
  public void testHelloWorld06_AsNormalUserHandleOutOfBoundsAgeAndThenOkAge() {
    HelloWorldActor06 helloWorldActor = new HelloWorldActor06(HelloWorld06.saveName, HelloWorld06.saveAge,
      HelloWorld06.greetUserWithName, HelloWorld06.greetUserWithAge, HelloWorld06.ageIsOk,
      HelloWorld06.ageIsOutOfBounds);

    recordStepNamesOf(helloWorldActor);
    NormalUser normalUser = new NormalUser(helloWorldActor, "Methusalem", "1000");
    AnonymousUser anonymousUser = new AnonymousUser(helloWorldActor, "43");
    helloWorldActor.setNormalUser(normalUser);
    helloWorldActor.setAnonymousUser(anonymousUser);
    normalUser.run();
    assertRecordedStepNames(helloWorldActor, "S1", "S2", "S3a_1", "S2", "S3", "S4");
  }

  @Test
  public void testHelloWorld06_AsAnonymousUserHandleNonNumericalAgeAndThenOkAge() {
    HelloWorldActor06 helloWorldActor = new HelloWorldActor06(HelloWorld06.saveName, HelloWorld06.saveAge,
      HelloWorld06.greetUserWithName, HelloWorld06.greetUserWithAge, HelloWorld06.ageIsOk,
      HelloWorld06.ageIsOutOfBounds);

    recordStepNamesOf(helloWorldActor);
    NormalUser normalUser = new NormalUser(helloWorldActor, "Jane", "21");
    AnonymousUser anonymousUser = new AnonymousUser(helloWorldActor, "NON-NUMERICAL-AGE");
    helloWorldActor.setNormalUser(normalUser);
    helloWorldActor.setAnonymousUser(anonymousUser);
    anonymousUser.run();
    assertRecordedStepNames(helloWorldActor, "S1a_1", "S3b_1", "S2", "S3c_1", "S4");
  }

  @Test
  public void testHelloWorld06_AsAnonymousUserAgeIsOk() {
    HelloWorldActor06 helloWorldActor = new HelloWorldActor06(HelloWorld06.saveName, HelloWorld06.saveAge,
      HelloWorld06.greetUserWithName, HelloWorld06.greetUserWithAge, HelloWorld06.ageIsOk,
      HelloWorld06.ageIsOutOfBounds);

    recordStepNamesOf(helloWorldActor);
    NormalUser normalUser = new NormalUser(helloWorldActor, "Jane", "21");
    AnonymousUser anonymousUser = new AnonymousUser(helloWorldActor, "43");
    helloWorldActor.setNormalUser(normalUser);
    helloWorldActor.setAnonymousUser(anonymousUser);
    helloWorldActor.run();
    anonymousUser.run();
    assertRecordedStepNames(helloWorldActor, "S1a_1", "S1a_2", "S3c_1", "S4");
  }
	
  @Test
  public void testHelloWorld07() {
    HelloWorld07 actor = new HelloWorld07();

    actor.inputColor = "yellow";
    actor.run();

    assertEquals("yellow", actor.outputColor);
  }
  
  private void recordStepNamesOf(AbstractActor actor) {
    actor.getModelRunner().startRecording();
  }

	protected void assertRecordedStepNames(AbstractActor actor, String... expectedStepNames) {
		String[] actualStepNames = actor.getModelRunner().getRecordedStepNames();
		assertArrayEquals(expectedStepNames, actualStepNames);
	}
}
