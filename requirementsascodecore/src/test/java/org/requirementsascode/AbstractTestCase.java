package org.requirementsascode;

import static org.junit.Assert.assertArrayEquals;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.requirementsascode.builder.ModelBuilder;

public abstract class AbstractTestCase {
  public static final String TEXT = "Hello, Basic Flow!";
  public static final int NUMBER = 42;

	protected static final String CUSTOMER = "Customer";
	protected static final String PARTNER = "Partner";
	protected static final String PARTNER2 = "Partner2";

	protected static final String USE_CASE = "Use case";
	protected static final String USE_CASE_2 = "Use case 2";

	protected static final String ALTERNATIVE_FLOW = "Alternative flow";
	protected static final String ALTERNATIVE_FLOW_2 = "Alternative flow 2";

	protected static final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	protected static final String SYSTEM_DISPLAYS_TEXT_AGAIN = "System displays text again";
	protected static final String SYSTEM_DISPLAYS_NUMBER = "System displays number";
	protected static final String SYSTEM_DISPLAYS_NUMBER_AGAIN = "System displays number again";

	protected static final String SYSTEM_THROWS_EXCEPTION = "System throws Exception";
	protected static final String SYSTEM_HANDLES_EXCEPTION = "System handles exception";

	protected static final String CUSTOMER_ENTERS_TEXT = "Customer enters text";
	protected static final String CUSTOMER_ENTERS_TEXT_AGAIN = "Customer enters text again";
	protected static final String CUSTOMER_ENTERS_ALTERNATIVE_TEXT = "Customer enters alternative text";
	protected static final String CUSTOMER_ENTERS_NUMBER = "Customer enters number";
	protected static final String CUSTOMER_ENTERS_ALTERNATIVE_NUMBER = "Customer enters number again";

	protected static final String THIS_STEP_SHOULD_BE_SKIPPED = "This step should be skipped";
	protected static final String THIS_STEP_SHOULD_BE_SKIPPED_AS_WELL = "This step should be skipped as well";

	protected static final String CONTINUE = "Continue";
	protected static final String CONTINUE_2 = "Continue 2";

	protected Actor customer;
	protected Actor sourceActor;
	protected Actor targetActor;
	protected ModelBuilder modelBuilder;
	protected ModelRunner modelRunner;
	protected String displayedText;

	protected void setupWithRecordingModelRunner() {
		this.modelRunner = new ModelRunner().startRecording();
		this.modelBuilder = Model.builder();
		this.customer = new Actor(CUSTOMER);
		this.sourceActor = new Actor(PARTNER);
		this.targetActor = new Actor(PARTNER2);
		this.displayedText = null;
	}

	protected void assertRecordedStepNames(String... expectedStepNames) {
		String[] actualStepNames = modelRunner.getRecordedStepNames();
		assertArrayEquals(expectedStepNames, actualStepNames);
	}

	protected void reactToAndAssertEvents(Object... events) {
		modelRunner.reactTo(events);
		assertArrayEquals(events, modelRunner.getRecordedMessages());
	}

	protected boolean textIsAvailable() {
		return displayedText != null;
	}

	protected boolean textIsNotAvailable() {
		return displayedText == null;
	}

	protected EntersText entersText() {
		return new EntersText(TEXT);
	}

	protected EntersText entersAlternativeText() {
		return new EntersText("Hello, I am an Alternative Flow!");
	}

	protected EntersNumber entersNumber() {
		EntersNumber enterNumber = new EntersNumber(NUMBER);
		return enterNumber;
	}

	protected Runnable displaysConstantText() {
		return () -> {
			displayedText = TEXT;
		};
	}
	
	protected Consumer<EntersText> displaysEnteredText() {
		return enteredText -> {
			displayedText = enteredText.toString();
		};
	}
	
	protected Function<Object, String> publishAnyMessageAsString() {
		return message -> message.toString();
	}
	
	protected Function<EntersText, String> publishEnteredTextAsString() {
		return enteredText -> enteredText.value();
  }
	
	protected Supplier<String> publishConstantTextAsString() {
		return () -> {
			return TEXT;
		};
	}
	
	protected Function<Object, Object> publishAnyMessageAsEvent() {
		return message -> message;
	}
	
	protected Function<EntersText, EntersText> publishesEnteredTextAsEvent() {
		return message -> message;
	}

	protected Consumer<EntersNumber> displaysEnteredNumber() {
		return enteredNumber -> {
			displayedText = enteredNumber.toString();
		};
	}

	protected Runnable throwsArrayIndexOutOfBoundsException() {
		return () -> {
			throw new ArrayIndexOutOfBoundsException(42);
		};
	}

	protected Consumer<EntersText> throwsRuntimeException() {
		return (object) -> {
			throw new RuntimeException("Test failed!");
		};
	}

	protected String latestStepName() {
		String latestStepName = modelRunner.getLatestStep().map(step -> step.getName()).orElse(null);
		return latestStepName;
	}

	public class EntersText {
		private String value;

		public EntersText(String text) {
			this.value = text;
		}

		public String value() {
			return value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	protected class EntersNumber {
		private Integer value;

		public EntersNumber(int value) {
			this.value = value;
		}

		public Integer value() {
			return value;
		}

		@Override
		public String toString() {
			return value.toString();
		}
	}
}
