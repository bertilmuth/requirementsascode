package org.requirementsascode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.Before;
import org.requirementsascode.event.EnterNumber;
import org.requirementsascode.event.EnterText;

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
	
	protected EnterText enterTextEvent(){
		return new EnterText("Hello, Basic Flow!");
	}
	
	protected EnterText enterDifferentTextEvent(){
		return new EnterText("Hello, I am an Alternative Flow!");
	}
	
	protected EnterNumber enterNumberEvent(){
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
		return (text) -> {
			runStepNames.add(getLatestStepName());
			displayedText = text.toString();
			System.out.println(displayedText);
		};
	}
	
	protected Consumer<EnterNumber> displayEnteredNumber() {
		return (integerContainer) -> {
			runStepNames.add(getLatestStepName());
			displayedText = integerContainer.value.toString();
			System.out.println(displayedText);
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
	
	protected Consumer<EnterText> throwRuntimeException() {
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
