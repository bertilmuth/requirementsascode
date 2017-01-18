package helloworld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.SystemReactionTrigger;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;

public class HelloWorldTest {
	private String runStepNames;
	private UseCaseRunner useCaseRunner;
	
	@Before
	public void setUp() throws Exception {
		runStepNames = "";
		useCaseRunner = new UseCaseRunner(trackRunStepNames());
	}
	private Consumer<SystemReactionTrigger> trackRunStepNames() {
		return systemReactionTrigger -> {
			runStepNames += trackStepName(systemReactionTrigger.useCaseStep());
			systemReactionTrigger.trigger();
		};
	}
	private String trackStepName(UseCaseStep useCaseStep) {
		String trackedStepName = "";
		if(useCaseStep.name().startsWith("S")){
			trackedStepName = useCaseStep.name() + ";";
		}
		return trackedStepName;
	}

	@Test
	public void testHelloWorld01() {
		HelloWorld01_PrintHelloUserExample example = new HelloWorld01_PrintHelloUserExample();
		example.createModelFor(useCaseRunner);
		useCaseRunner.run();

		assertEquals("S1;", runStepNames);
	}
	
	@Test
	public void testHelloWorld02() {
		HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample example = new HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample();
		example.createModelFor(useCaseRunner);
		useCaseRunner.run();

		assertTrue(runStepNames.startsWith("S1;S2;"));
	}
	
	@Test
	public void testHelloWorld03() {
		HelloWorld03_EnterNameExample example = new HelloWorld03_EnterNameExample();
		example.createModelFor(useCaseRunner);
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John Q. Public"));

		assertEquals("S1;S2;", runStepNames);
	}
	
	@Test
	public void testHelloWorld04() {
		HelloWorld04_EnterNameAndAgeExample example = new HelloWorld04_EnterNameAndAgeExample();
		example.createModelFor(useCaseRunner);
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;", runStepNames);
	}
	
	@Test
	public void testHelloWorld05_WithCorrectNameAndAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.createModelFor(useCaseRunner);
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;", runStepNames);
	}
	
	@Test
	public void testHelloWorld05_WithOutOfBoundsAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.createModelFor(useCaseRunner);
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("1000"));

		assertEquals("S1;S2;S3;S4;S4a_1;S3;", runStepNames);
	}
	
	@Test
	public void testHelloWorld05_WithNonNumericalAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.createModelFor(useCaseRunner);
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("NON-NUMERICAL-AGE"));

		assertEquals("S1;S2;S3;S4;S4b_1;S3;", runStepNames);
	}
	
	@Test
	public void testHelloWorld06_AsNormalUser() {
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		example.createModelFor(useCaseRunner);
		useCaseRunner.runAs(example.normalUser());
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;S7;", runStepNames);
	}
	
	@Test
	public void testHelloWorld06_AsAnonymousUser() {
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		example.createModelFor(useCaseRunner);
		useCaseRunner.runAs(example.anonymousUser());
		useCaseRunner.reactTo(new EnterText("39"));

		assertEquals("S0a_1;S3;S4;S4c_1;S6;S7;", runStepNames);
	}
}
