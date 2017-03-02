package helloworld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.test.TestUseCaseRunner;

public class HelloWorldTest {
	private TestUseCaseRunner runner;
	
	@Before
	public void setUp() throws Exception {
		runner = new TestUseCaseRunner();
	}

	@Test
	public void testHelloWorld01() {
		HelloWorld01_PrintHelloUserExample example = new HelloWorld01_PrintHelloUserExample();
		example.createModelFor(runner);
		runner.run();

		assertEquals("S1;", runner.runStepNames());
	}
	
	@Test
	public void testHelloWorld02() {
		HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample example = new HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample();
		example.createModelFor(runner);
		runner.run();

		assertTrue(runner.runStepNames().startsWith("S1;S2;"));
	}
	
	@Test
	public void testHelloWorld03() {
		HelloWorld03_EnterNameExample example = new HelloWorld03_EnterNameExample();
		example.createModelFor(runner);
		runner.run();
		runner.reactTo(new EnterText("John Q. Public"));

		assertEquals("S1;S2;", runner.runStepNames());
	}
	
	@Test
	public void testHelloWorld04() {
		HelloWorld04_EnterNameAndAgeExample example = new HelloWorld04_EnterNameAndAgeExample();
		example.createModelFor(runner);
		runner.run();
		runner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;", runner.runStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithCorrectNameAndAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.createModelFor(runner);
		runner.run();
		runner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;", runner.runStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithOutOfBoundsAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.createModelFor(runner);
		runner.run();
		runner.reactTo(new EnterText("John"), new EnterText("1000"));

		assertEquals("S1;S2;S3;S4;S4a_1;S3;", runner.runStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithNonNumericalAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.createModelFor(runner);
		runner.run();
		runner.reactTo(new EnterText("John"), new EnterText("NON-NUMERICAL-AGE"));

		assertEquals("S1;S2;S3;S4;S4b_1;S3;", runner.runStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsNormalUser() {
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		example.createModelFor(runner);
		runner.runAs(example.normalUser());
		runner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;S7;", runner.runStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsAnonymousUser() {
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		example.createModelFor(runner);
		runner.runAs(example.anonymousUser());
		runner.reactTo(new EnterText("39"));

		assertEquals("S0a_1;S3;S4;S4c_1;S6;S7;", runner.runStepNames());
	}
}
