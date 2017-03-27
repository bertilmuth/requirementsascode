package org.requirementsascode.builder;

import static org.requirementsascode.testutil.Names.CUSTOMER;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.Actor;
import org.requirementsascode.TestUseCaseRunner;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.testutil.EnterNumber;
import org.requirementsascode.testutil.EnterText;


public abstract class AbstractTestCase {
	protected Actor customer;
	protected UseCaseModelBuilder useCaseModelBuilder;
	protected TestUseCaseRunner useCaseRunner;
	private String displayedText;


	protected void setupWith(TestUseCaseRunner useCaseRunner){
		this.useCaseRunner = useCaseRunner;
		this.useCaseModelBuilder = new UseCaseModelBuilder();
		this.customer = useCaseModelBuilder.build().actor(CUSTOMER);
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
		EnterNumber enterNumber = new EnterNumber();
		enterNumber.value = 42;
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
