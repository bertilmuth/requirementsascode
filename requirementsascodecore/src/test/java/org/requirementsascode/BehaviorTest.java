package org.requirementsascode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.requirementsascode.testbehavior.EmptyBehavior;
import org.requirementsascode.testbehavior.TestAddTaskRequest;
import org.requirementsascode.testbehavior.TestBehaviorModel;
import org.requirementsascode.testbehavior.TestCreateListRequest;
import org.requirementsascode.testbehavior.TestExceptionThrower;
import org.requirementsascode.testbehavior.TestAddTaskResponse;
import org.requirementsascode.testbehavior.TestCreateListResponse;

public class BehaviorTest {
	private String receivedMessage;

	@BeforeEach
	void setup() {
		receivedMessage = "n/a";
	}

	@Test
	void emptyBehaviorDoesntReactToMessage() {
		StatelessBehavior statelessBehavior = new StatelessBehavior(new EmptyBehavior());
		Optional<Object> response = statelessBehavior.reactTo("DummyStringMessage");
		assertFalse(response.isPresent());
	}

	@Test
	void reactsToStringMessage() {
		StatelessBehavior statelessBehavior = new StatelessBehavior(new MessageFieldMutator());

		final String expectedMessage = "ExpectedTestMessage";
		Optional<Object> response = statelessBehavior.reactTo(expectedMessage);

		assertEquals(expectedMessage, receivedMessage);
		assertFalse(response.isPresent());
	}

	@Test
	void reactsToCreateList() {
		StatelessBehavior statelessBehavior = new StatelessBehavior(new TestBehaviorModel());

		final TestCreateListRequest createList = new TestCreateListRequest();
		final Optional<TestCreateListResponse> optionalResponse = statelessBehavior.reactTo(createList);
		TestCreateListResponse todoListId = optionalResponse.get();

		assertNotNull(todoListId);
	}

	@Test
	void reactsToAddTask() {
		StatelessBehavior statelessBehavior = new StatelessBehavior(new TestBehaviorModel());

		final TestAddTaskRequest addTask = new TestAddTaskRequest();
		final Optional<TestAddTaskResponse> optionalResponse = statelessBehavior.reactTo(addTask);
		TestAddTaskResponse taskId = optionalResponse.get();

		assertNotNull(taskId);
	}

	@Test
	void throwsExceptionIfPublishedTypeMatchesRequestType() {
		StatelessBehavior statelessBehavior = new StatelessBehavior(new TestExceptionThrower());

		BehaviorException actualException = assertThrows(BehaviorException.class,
			() -> statelessBehavior.reactTo("StringMessageThatWillTriggerException"));

		// Assert that exception message contains request type name
		final String exceptionMessage = actualException.getMessage();
		assertTrue(exceptionMessage.contains(String.class.getName()));
	}

	private class MessageFieldMutator implements BehaviorModel {
		@Override
		public Model model() {
			return Model.builder().user(String.class).system(msg -> receivedMessage = msg).build();
		}
	}
}
