package helloworld;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.TestUseCaseModelRunner;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;

import helloworld.userevent.EntersText;

public class HelloWorldTest {
	private TestUseCaseModelRunner modelRunner;
	private UseCaseModel useCaseModel;
	private UseCaseModelBuilder modelBuilder;
	
	@Before
	public void setUp() throws Exception {
		modelRunner = new TestUseCaseModelRunner();
		modelBuilder = UseCaseModel.builder();
	}

	@Test
	public void testHelloWorld01() {
		HelloWorld01 example = new HelloWorld01();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.run(useCaseModel);

		assertEquals("S1;", modelRunner.getRunStepNames());
	}
	
	@Test
	public void testHelloWorld02() {
		HelloWorld02 example = new HelloWorld02();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.run(useCaseModel);

		assertEquals("S1;S2;S2;S2;", modelRunner.getRunStepNames());
	}
	
	@Test
	public void testHelloWorld03() {
		HelloWorld03 example = new HelloWorld03();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(new EntersText("John Q. Public"));

		assertEquals("S1;S2;", modelRunner.getRunStepNames());
	}
	
	@Test
	public void testHelloWorld04() {
		HelloWorld04 example = new HelloWorld04();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(new EntersText("John"), new EntersText("39"));

		assertEquals("S1;S2;S3;S4;S5;", modelRunner.getRunStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithCorrectNameAndAge() {
		HelloWorld05 example = new HelloWorld05();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(new EntersText("John"), new EntersText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;", modelRunner.getRunStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithOutOfBoundsAge() {
		HelloWorld05 example = new HelloWorld05();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(new EntersText("John"), new EntersText("1000"));

		assertEquals("S1;S2;S3;S4;S5a_1;S5a_2;S3;", modelRunner.getRunStepNames());
	}
	
	@Test
	public void testHelloWorld05_WithNonNumericalAge() {
		HelloWorld05 example = new HelloWorld05();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.run(useCaseModel);
		modelRunner.reactTo(new EntersText("John"), new EntersText("NON-NUMERICAL-AGE"));

		assertEquals("S1;S2;S3;S4;S5b_1;S5b_2;S3;", modelRunner.getRunStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsNormalUser() {
		HelloWorld06 example = new HelloWorld06();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.as(example.normalUser()).run(useCaseModel);
		modelRunner.reactTo(new EntersText("John"), new EntersText("39"));

		assertEquals("S1;S2;S3;S4;S5;S6;S7;", modelRunner.getRunStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsAnonymousUserAgeIsOk() {
		HelloWorld06 example = new HelloWorld06();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.as(example.anonymousUser()).run(useCaseModel);
		modelRunner.reactTo(new EntersText("39"));

		assertEquals("S1a_1;S3;S4;S5c_1;S6;S7;", modelRunner.getRunStepNames());
	}
	
	@Test
	public void testHelloWorld06_AsAnonymousUserHandleNonNumericalAge() {
		HelloWorld06 example = new HelloWorld06();
		useCaseModel = example.buildWith(modelBuilder);
		
		modelRunner.as(example.anonymousUser()).run(useCaseModel);
		modelRunner.reactTo(new EntersText("NotANumber"));

		assertEquals("S1a_1;S3;S4;S5b_1;S5b_2;S3;", modelRunner.getRunStepNames());
	}
}
