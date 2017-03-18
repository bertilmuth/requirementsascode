package helloworld;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.TestUseCaseRunner;
import org.requirementsascode.UseCaseModel;

public class HelloWorldTest {
	private TestUseCaseRunner useCaseRunner;
	private UseCaseModel useCaseModel;
	
	@Before
	public void setUp() throws Exception {
		useCaseRunner = new TestUseCaseRunner();
		useCaseModel = useCaseRunner.useCaseModel();
	}

	@Test
	public void testHelloWorld01() {
		HelloWorld01_PrintHelloUserExample example = new HelloWorld01_PrintHelloUserExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();

		assertEquals("S1;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld02() {
		HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample example = new HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();

		assertEquals("S1;S2;S2;S2;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld03() {
		HelloWorld03_EnterNameExample example = new HelloWorld03_EnterNameExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John Q. Public"));

		assertEquals("S1;S2;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld04() {
		HelloWorld04_EnterNameAndAgeExample example = new HelloWorld04_EnterNameAndAgeExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithCorrectNameAndAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithOutOfBoundsAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("1000"));

		assertEquals("S1;S2;S3;S4;S5a_1;S5a_2;S3;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithNonNumericalAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("NON-NUMERICAL-AGE"));

		assertEquals("S1;S2;S3;S4;S5b_1;S5b_2;S3;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsNormalUser() {
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		example.create(useCaseModel);
		
		useCaseRunner.runAs(example.normalUser());
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;S7;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsAnonymousUser() {
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		example.create(useCaseModel);
		
		useCaseRunner.runAs(example.anonymousUser());
		useCaseRunner.reactTo(new EnterText("39"));

		assertEquals("S1a_1;S3;S4;S5c_1;S6;S7;", useCaseRunner.runStepNames());
	}
}
