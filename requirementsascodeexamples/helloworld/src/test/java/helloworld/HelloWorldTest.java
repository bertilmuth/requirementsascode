package helloworld;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.requirementsascode.AbstractActor;
import org.requirementsascode.ModelRunner;

import helloworld.usercommand.EnterText;

public class HelloWorldTest {
	@Test
	public void testHelloWorld01() {
		HelloWorld01 actor = new HelloWorld01();
		actor.getModelRunner().startRecording();
		actor.run();

		assertRecordedStepNames(actor, "S1");
	}

	@Test
	public void testHelloWorld02() {
		HelloWorld02 actor = new HelloWorld02();
    actor.getModelRunner().startRecording();
    actor.run();

		assertRecordedStepNames(actor, "S1", "S1", "S1");
	}

	@Test
	public void testHelloWorld03() {
		HelloWorld03 actor = new HelloWorld03();
    actor.getModelRunner().startRecording();
    
		actor.reactTo(new EnterText("John Q. Public"));

		assertRecordedStepNames(actor, "S1", "S2");
	}

	@Test
	public void testHelloWorld03a() {
		HelloWorld03a actor = new HelloWorld03a();
    ModelRunner modelRunner = actor.getModelRunner();
    modelRunner.startRecording();

		actor.run();
		
		modelRunner.as(actor.invalidUser()).reactTo(new EnterText("Ignored"));
		assertRecordedStepNames(actor, "S1");
		
		modelRunner.as(actor.validUser()).reactTo(new EnterText("John Q. Public"));
		assertRecordedStepNames(actor, "S1", "S2");
	}

	@Test
	public void testHelloWorld04() {
		HelloWorld04 actor = new HelloWorld04();
    ModelRunner modelRunner = actor.getModelRunner();
    modelRunner.startRecording();

    actor.run();
    
		actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("39"));

		assertRecordedStepNames(actor, "S1", "S2", "S3", "S4", "S5");
	}

	@Test
	public void testHelloWorld05_WithCorrectNameAndAge() {
		HelloWorld05 actor = new HelloWorld05();
    ModelRunner modelRunner = actor.getModelRunner();
    modelRunner.startRecording();
    
    actor.run();
    
    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("39"));
    
		assertRecordedStepNames(actor, "S1", "S2", "S3", "S4", "S5", "S6");
	}

	@Test
	public void testHelloWorld05_WithOutOfBoundsAge() {
    HelloWorld05 actor = new HelloWorld05();
    ModelRunner modelRunner = actor.getModelRunner();
    modelRunner.startRecording();
    
    actor.run();
    
    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("1000"));

		assertRecordedStepNames(actor, "S1", "S2", "S3", "S4", "S5a_1", "S5a_2", "S3");
	}

	@Test
	public void testHelloWorld05_WithNonNumericalAge() {
    HelloWorld05 actor = new HelloWorld05();
    ModelRunner modelRunner = actor.getModelRunner();
    modelRunner.startRecording();
    
    actor.run();

    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("NON-NUMERICAL-AGE"));

		assertRecordedStepNames(actor, "S1", "S2", "S3", "S4", "S5b_1", "S5b_2", "S3");
	}

	@Test
	public void testHelloWorld06_AsNormalUser() {
	  HelloWorld06 actor = new HelloWorld06();
    ModelRunner modelRunner = actor.getModelRunner();
    modelRunner.startRecording();
        
    actor.getModelRunner().as(actor.normalUser()).run(actor.behavior());

    actor.reactTo(new EnterText("John"));
    actor.reactTo(new EnterText("39"));

		assertRecordedStepNames(actor, "S1", "S2", "S3", "S4", "S5", "S6", "S7");
	}

	@Test
	public void testHelloWorld06_AsAnonymousUserAgeIsOk() {
    HelloWorld06 actor = new HelloWorld06();
    ModelRunner modelRunner = actor.getModelRunner();
    modelRunner.startRecording();
        
    actor.getModelRunner().as(actor.anonymousUser()).run(actor.behavior());

    actor.reactTo(new EnterText("39"));

		assertRecordedStepNames(actor, "S1a_1", "S3", "S4", "S5c_1", "S6", "S7");
	}

	@Test
	public void testHelloWorld06_AsAnonymousUserHandleNonNumericalAge() {
    HelloWorld06 actor = new HelloWorld06();
    ModelRunner modelRunner = actor.getModelRunner();
    modelRunner.startRecording();
        
    actor.getModelRunner().as(actor.anonymousUser()).run(actor.behavior());

    actor.reactTo(new EnterText("NotANumber"));

		assertRecordedStepNames(actor, "S1a_1", "S3", "S4", "S5b_1", "S5b_2", "S3");
	}
	
  @Test
  public void testHelloWorld07() {
    HelloWorld07 actor = new HelloWorld07();

    actor.inputColor = "yellow";
    actor.run();

    assertEquals("yellow", actor.outputColor);
  }

	protected void assertRecordedStepNames(AbstractActor actor, String... expectedStepNames) {
		String[] actualStepNames = actor.getModelRunner().getRecordedStepNames();
		assertArrayEquals(expectedStepNames, actualStepNames);
	}
}
