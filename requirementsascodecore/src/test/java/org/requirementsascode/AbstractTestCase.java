package org.requirementsascode;

import static org.requirementsascode.testutil.Names.CUSTOMER;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.requirementsascode.builder.UseCaseModelBuilder;

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
