package helloworld;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.TestUseCaseRunner;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.builder.UseCaseModelBuilder;

public class HelloWorldTest {
	private TestUseCaseRunner useCaseRunner;
	private UseCaseModel useCaseModel;
	
	@Before
	public void setUp() throws Exception {
		useCaseRunner = new TestUseCaseRunner();
	}

	@Test
	public void testHelloWorld01() {
		HelloWorld01_PrintHelloUserExample example = new HelloWorld01_PrintHelloUserExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.run(useCaseModel);

		assertEquals("S1;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld02() {
		HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample example = new HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.run(useCaseModel);

		assertEquals("S1;S2;S2;S2;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld03() {
		HelloWorld03_EnterNameExample example = new HelloWorld03_EnterNameExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.run(useCaseModel);
		useCaseRunner.reactTo(new EnterText("John Q. Public"));

		assertEquals("S1;S2;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld04() {
		HelloWorld04_EnterNameAndAgeExample example = new HelloWorld04_EnterNameAndAgeExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.run(useCaseModel);
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithCorrectNameAndAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.run(useCaseModel);
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithOutOfBoundsAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.run(useCaseModel);
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("1000"));

		assertEquals("S1;S2;S3;S4;S5a_1;S5a_2;S3;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithNonNumericalAge() {
		HelloWorld05_EnterNameAndAgeWithValidationExample example = new HelloWorld05_EnterNameAndAgeWithValidationExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.run(useCaseModel);
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("NON-NUMERICAL-AGE"));

		assertEquals("S1;S2;S3;S4;S5b_1;S5b_2;S3;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsNormalUser() {
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.as(example.normalUser()).run(useCaseModel);
		useCaseRunner.reactTo(new EnterText("John"), new EnterText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;S7;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsAnonymousUserAgeIsOk() {
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.as(example.anonymousUser()).run(useCaseModel);
		useCaseRunner.reactTo(new EnterText("39"));

		assertEquals("S1a_1;S3;S4;S5c_1;S6;S7;", useCaseRunner.runStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsAnonymousUserHandleNonNumericalAge() {
		HelloWorld06_EnterNameAndAgeWithAnonymousUserExample example = new HelloWorld06_EnterNameAndAgeWithAnonymousUserExample();
		useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.as(example.anonymousUser()).run(useCaseModel);
		useCaseRunner.reactTo(new EnterText("NotANumber"));

		assertEquals("S1a_1;S3;S4;S5b_1;S5b_2;S3;", useCaseRunner.runStepNames());
	}
}
