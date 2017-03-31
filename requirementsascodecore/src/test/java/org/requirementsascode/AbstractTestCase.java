package org.requirementsascode;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.builder.UseCaseModelBuilder;

public abstract class AbstractTestCase {
	
	protected static final String CUSTOMER = "Customer";
	
	protected static final String USE_CASE = "Use Case";
	protected static final String USE_CASE_2 = "Use Case 2";

	protected static final String BASIC_FLOW = "Basic Flow";
	protected static final String ALTERNATIVE_FLOW = "Alternative Flow";
	protected static final String ALTERNATIVE_FLOW_2 = "Alternative Flow 2";
	
	protected static final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	protected static final String SYSTEM_DISPLAYS_TEXT_AGAIN = "System displays text again";
	protected static final String SYSTEM_DISPLAYS_NUMBER = "System displays number";
	protected static final String SYSTEM_THROWS_EXCEPTION = "System throws Exception";
	protected static final String SYSTEM_HANDLES_EXCEPTION = "System handles exception";

	protected static final String CUSTOMER_ENTERS_TEXT = "Customer enters text";
	protected static final String CUSTOMER_ENTERS_TEXT_AGAIN = "Customer enters text again";
	protected static final String CUSTOMER_ENTERS_ALTERNATIVE_TEXT = "Customer enters alternative text";
	protected static final String CUSTOMER_ENTERS_NUMBER = "Customer enters number";
	protected static final String CUSTOMER_ENTERS_NUMBER_AGAIN = "Customer enters number again";
		
	protected static final String THIS_STEP_SHOULD_BE_SKIPPED = "This step should be skipped";
	protected static final String THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL = "This step should be skipped as well";

	protected static final String CONTINUE = "Continue";
	protected static final String CONTINUE_2 = "Continue 2";
	
	protected Actor customer;
	protected UseCaseModelBuilder useCaseModelBuilder;
	protected TestUseCaseRunner useCaseRunner;
	protected String displayedText;

	protected void setupWith(TestUseCaseRunner useCaseRunner){
		this.useCaseRunner = useCaseRunner;
		this.useCaseModelBuilder = UseCaseModelBuilder.ofNewModel();
		this.customer = useCaseModelBuilder.build().newActor(CUSTOMER);
		this.displayedText = null;
	}
	
	protected Predicate<UseCaseRunner> textIsAvailable() {
		return r -> displayedText != null;
	}
	
	protected Predicate<UseCaseRunner> textIsNotAvailable() {
		return r -> displayedText == null;
	}
	
	protected EnterText enterText(){
		return new EnterText("Hello, Basic Flow!");
	}
	
	protected EnterText enterAlternativeText(){
		return new EnterText("Hello, I am an Alternative Flow!");
	}
	
	protected EnterNumber enterNumber(){
		EnterNumber enterNumber = new EnterNumber(42);
		return enterNumber;
	}
	
	protected Consumer<UseCaseRunner> displayConstantText() {
		return r -> {
			displayedText = "Hello, Basic Flow!";
		};
	}
	
	protected Consumer<EnterText> displayEnteredText() {
		return enterText -> {
			displayedText = enterText.toString();
		};
	}
	
	protected Consumer<EnterNumber> displayEnteredNumber() {
		return enterNumber -> {
			displayedText = enterNumber.toString();
		};
	}
	
	protected Supplier<EnterNumber> raiseEnterNumber() {
		return () -> {
			EnterNumber enterNumberEvent = new EnterNumber(42);
			return enterNumberEvent;
		};
	}
	
	protected Consumer<UseCaseRunner> throwArrayIndexOutOfBoundsException() {
		return r -> {
			throw new ArrayIndexOutOfBoundsException(42);
		};
	}
	
	protected Consumer<EnterText> throwRuntimeException() {
		return (object) -> {throw new RuntimeException("Test failed!");};
	}
	
	protected String runStepNames() {
		return useCaseRunner.runStepNames();
	}

	protected String latestStepName() {
		String latestStepName = 
			useCaseRunner.latestStep()
				.map(step -> step.name()).orElse(null);
		return latestStepName;
	}
	
	public class EnterText{
		private String value;
		
		public EnterText(String text) {
			this.value = text;
		}
		
		public String value(){
			return value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	protected class EnterNumber{
		private Integer value;
		
		public EnterNumber(int value) {
			this.value = value;
		}
		
		public Integer value(){
			return value;
		}
		
		@Override
		public String toString() {
			return value.toString();
		}
	}
}
