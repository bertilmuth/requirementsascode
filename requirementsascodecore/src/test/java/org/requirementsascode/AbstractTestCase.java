package org.requirementsascode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.Before;
import org.requirementsascode.event.EnteredNumber;
import org.requirementsascode.event.EnteredText;

public abstract class AbstractTestCase {
	protected Actor customer;
	protected UseCaseModelRun useCaseModelRun;
	protected UseCaseModel useCaseModel;
	
	private String displayedText;
	private ArrayList<String> runStepNames;

	@Before
	public void setup(){
		this.useCaseModelRun = new UseCaseModelRun();
		this.useCaseModel = useCaseModelRun.getModel();
		this.customer = useCaseModel.newActor("Customer");
		
		runStepNames = new ArrayList<>();
		displayedText = null;
	}
	
	protected Class<EnteredNumber> enteredNumberEventClass() {
		return EnteredNumber.class;
	}
	
	protected Predicate<UseCaseModelRun> textIsAvailablePredicate() {
		return r -> displayedText != null;
	}

	protected Supplier<Boolean> textIsAvailable() {
		return () -> displayedText != null;
	}

	protected Supplier<Boolean> textIsNotAvailable() {
		return () -> displayedText == null;
	}
	
	protected Predicate<UseCaseModelRun> textIsNotAvailablePredicate() {
		return r -> displayedText == null;
	}
	
	protected EnteredText enteredTextEvent(){
		return new EnteredText("Hello, Basic Flow!");
	}
	
	protected EnteredText enteredDifferentTextEvent(){
		return new EnteredText("Hello, I am an Alternative Flow!");
	}
	
	protected EnteredNumber enteredNumberEvent(){
		EnteredNumber enteredNumber = new EnteredNumber();
		enteredNumber.value = 42;
		return enteredNumber;
	}
	
	protected Runnable displaysConstantText() {
		return () -> {
			runStepNames.add(getLatestStepName());
			displayedText = "Hello, Basic Flow!";
			System.out.println(displayedText);
		};
	}
	
	protected Consumer<EnteredText> displaysEnteredText() {
		return (text) -> {
			runStepNames.add(getLatestStepName());
			displayedText = text.toString();
			System.out.println(displayedText);
		};
	}
	
	protected Consumer<EnteredNumber> displaysEnteredNumber() {
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
	
	protected Consumer<ArrayIndexOutOfBoundsException> reactsToArrayIndexOutOfBoundsException() {
		return e -> {
			runStepNames.add(getLatestStepName());
		};
	}
	
	protected Consumer<EnteredText> throwRuntimeException() {
		return (object) -> {throw new RuntimeException("Test failed!");};
	}
	
	protected List<String> getRunStepNames() {
		return runStepNames;
	}

	protected String getLatestStepName() {
		UseCaseStep latestStep = useCaseModelRun.getLatestStep();
		return latestStep != null ? latestStep.getName() : null;
	}
}
