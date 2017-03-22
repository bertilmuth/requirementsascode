package org.requirementsascode;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.event.EnterNumber;
import org.requirementsascode.event.EnterText;

public abstract class AbstractTestCase {
	protected final String CUSTOMER = "Customer";
	
	protected final String USE_CASE = "Use Case";
	protected final String ANOTHER_USE_CASE = "Another Use Case";

	protected final String BASIC_FLOW = "Basic Flow";
	protected final String ALTERNATIVE_FLOW = "Alternative Flow";
	protected final String ALTERNATIVE_FLOW_2 = "Alternative Flow 2";
	
	protected final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	protected final String SYSTEM_DISPLAYS_TEXT_AGAIN = "System displays text again";
	protected final String SYSTEM_DISPLAYS_NUMBER = "System displays number";

	protected final String CUSTOMER_ENTERS_TEXT = "Customer enters text";
	protected final String CUSTOMER_ENTERS_TEXT_AGAIN = "Customer enters text again";
	protected final String CUSTOMER_ENTERS_ALTERNATIVE_TEXT = "Customer enters alternative text";
	protected final String CUSTOMER_ENTERS_NUMBER = "Customer enters number";
	protected final String CUSTOMER_ENTERS_NUMBER_AGAIN = "Customer enters number again";
		
	protected final String THIS_STEP_SHOULD_BE_SKIPPED = "This step should be skipped";
	protected final String THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL = "This step should be skipped as well";

	protected final String CONTINUE = "Continue";
	protected final String CONTINUE_2 = "Continue 2";
	
	protected Actor customer;
	protected TestUseCaseRunner useCaseRunner;
	protected UseCaseModel useCaseModel;
	
	private String displayedText;

	protected void setupWith(TestUseCaseRunner useCaseRunner){
		this.useCaseRunner = useCaseRunner;
		this.useCaseModel = useCaseRunner.useCaseModel();
		this.customer = useCaseModel.actor(CUSTOMER);
		
		displayedText = null;
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
		EnterNumber enterNumber = new EnterNumber();
		enterNumber.value = 42;
		return enterNumber;
	}
	
	protected Runnable displayConstantText() {
		return () -> {
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
			displayedText = enterNumber.value.toString();
		};
	}
	
	protected Supplier<EnterNumber> raiseEnterNumber() {
		return () -> {
			EnterNumber enterNumberEvent = new EnterNumber();
			enterNumberEvent.value = 42;
			return enterNumberEvent;
		};
	}
	
	protected Runnable throwArrayIndexOutOfBoundsException() {
		return () -> {
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
}
