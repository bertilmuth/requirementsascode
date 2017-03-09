package org.requirementsascode;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.event.EnterNumber;
import org.requirementsascode.event.EnterText;

public abstract class AbstractTestCase {
	protected Actor customer;
	protected TestUseCaseRunner useCaseRunner;
	protected UseCaseModel useCaseModel;
	
	private String displayedText;

	protected void setupWith(TestUseCaseRunner useCaseRunner){
		this.useCaseRunner = useCaseRunner;
		this.useCaseModel = useCaseRunner.useCaseModel();
		this.customer = useCaseModel.actor("Customer");
		
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
	
	protected EnterText enterDifferentText(){
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
			System.out.println(displayedText);
		};
	}
	
	protected Consumer<EnterText> displayEnteredText() {
		return enterText -> {
			displayedText = enterText.toString();
			System.out.println(displayedText);
		};
	}
	
	protected Consumer<EnterNumber> displayEnteredNumber() {
		return enterNumber -> {
			displayedText = enterNumber.value.toString();
			System.out.println(displayedText);
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
