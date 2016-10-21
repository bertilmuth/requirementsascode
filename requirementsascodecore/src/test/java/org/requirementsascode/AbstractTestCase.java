package org.requirementsascode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.Before;
import org.requirementsascode.event.EnterNumberEvent;
import org.requirementsascode.event.EnterTextEvent;

public abstract class AbstractTestCase {
	protected Actor customer;
	protected UseCaseRunner useCaseRunner;
	protected UseCaseModel useCaseModel;
	
	private String displayedText;
	private ArrayList<String> runStepNames;

	@Before
	public void setup(){
		this.useCaseRunner = new UseCaseRunner();
		this.useCaseModel = useCaseRunner.getUseCaseModel();
		this.customer = useCaseModel.newActor("Customer");
		
		runStepNames = new ArrayList<>();
		displayedText = null;
	}
	
	protected Predicate<UseCaseRunner> textIsAvailablePredicate() {
		return r -> displayedText != null;
	}
	
	protected Predicate<UseCaseRunner> textIsNotAvailablePredicate() {
		return r -> displayedText == null;
	}
	
	protected EnterTextEvent enterTextEvent(){
		return new EnterTextEvent("Hello, Basic Flow!");
	}
	
	protected EnterTextEvent enterDifferentTextEvent(){
		return new EnterTextEvent("Hello, I am an Alternative Flow!");
	}
	
	protected EnterNumberEvent enterNumberEvent(){
		EnterNumberEvent enterNumber = new EnterNumberEvent();
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
	
	protected Consumer<EnterTextEvent> displayEnteredText() {
		return enterTextEvent -> {
			runStepNames.add(getLatestStepName());
			displayedText = enterTextEvent.toString();
			System.out.println(displayedText);
		};
	}
	
	protected Consumer<EnterNumberEvent> displayEnteredNumber() {
		return enterNumberEvent -> {
			runStepNames.add(getLatestStepName());
			displayedText = enterNumberEvent.value.toString();
			System.out.println(displayedText);
		};
	}
	
	protected Supplier<EnterNumberEvent> raiseEnterNumber() {
		return () -> {
			EnterNumberEvent enterNumberEvent = new EnterNumberEvent();
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
	
	protected Consumer<ArrayIndexOutOfBoundsException> reactToArrayIndexOutOfBoundsException() {
		return e -> {
			runStepNames.add(getLatestStepName());
		};
	}
	
	protected Consumer<EnterTextEvent> throwRuntimeException() {
		return (object) -> {throw new RuntimeException("Test failed!");};
	}
	
	protected List<String> getRunStepNames() {
		return runStepNames;
	}

	protected String getLatestStepName() {
		String latestStepName = 
			useCaseRunner.getLatestStep()
				.map(step -> step.getName()).orElse(null);
		return latestStepName;
	}
}
