package org.requirementsascode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.event.EnterNumber;
import org.requirementsascode.event.EnterText;

public abstract class AbstractTestCase {
	protected Actor customer;
	protected UseCaseRunner useCaseRunner;
	protected UseCaseModel useCaseModel;
	
	private String displayedText;
	private ArrayList<String> runStepNames;

	protected void setupWith(UseCaseRunner useCaseRunner){
		this.useCaseRunner = useCaseRunner;
		this.useCaseModel = useCaseRunner.useCaseModel();
		this.customer = useCaseModel.actor("Customer");
		
		runStepNames = new ArrayList<>();
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
			runStepNames.add(getLatestStepName());
			displayedText = "Hello, Basic Flow!";
			System.out.println(displayedText);
		};
	}
	
	protected Consumer<EnterText> displayEnteredText() {
		return enterText -> {
			runStepNames.add(getLatestStepName());
			displayedText = enterText.toString();
			System.out.println(displayedText);
		};
	}
	
	protected Consumer<EnterNumber> displayEnteredNumber() {
		return enterNumber -> {
			runStepNames.add(getLatestStepName());
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
			runStepNames.add(getLatestStepName());
			throw new ArrayIndexOutOfBoundsException(42);
		};
	}
	
	protected Consumer<ArrayIndexOutOfBoundsException> trackArrayIndexOutOfBoundsException() {
		return e -> {
			runStepNames.add(getLatestStepName());
		};
	}
	
	protected Consumer<EnterText> throwRuntimeException() {
		return (object) -> {throw new RuntimeException("Test failed!");};
	}
	
	protected List<String> getRunStepNames() {
		return runStepNames;
	}

	protected String getLatestStepName() {
		String latestStepName = 
			useCaseRunner.latestStep()
				.map(step -> step.name()).orElse(null);
		return latestStepName;
	}
}
