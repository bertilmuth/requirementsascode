package creditcard_eventsourcing.model;

import static creditcard_eventsourcing.model.CreditCardModelRunner.ASSIGN;
import static creditcard_eventsourcing.model.CreditCardModelRunner.REPAY;
import static creditcard_eventsourcing.model.CreditCardModelRunner.REPEAT;
import static creditcard_eventsourcing.model.CreditCardModelRunner.WITHDRAW;
import static creditcard_eventsourcing.model.CreditCardModelRunner.WITHDRAW_AGAIN;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.ModelRunner;

import creditcard_eventsourcing.model.command.RequestToCloseCycle;
import creditcard_eventsourcing.model.command.RequestsRepay;
import creditcard_eventsourcing.model.command.RequestsToAssignLimit;
import creditcard_eventsourcing.model.command.RequestsWithdrawal;
import creditcard_eventsourcing.persistence.CreditCardRepository;

public class CreditCardModelRunnerTest {
	private ModelRunner modelRunner;
	private CreditCardModelRunner cardModelRunner;
	private CreditCardRepository repository;

	@Before
	public void setUp() throws Exception {
		modelRunner = new ModelRunner().startRecording();
		repository = new CreditCardRepository();
		cardModelRunner = new CreditCardModelRunner(UUID.randomUUID(), modelRunner, repository);
	}

	@Test
	public void assigningLimitOnceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.TEN);
		assertRecordedStepNames(ASSIGN);
		assertEquals(BigDecimal.TEN, creditCard().availableLimit());
	}

	@Test
	public void assigningLimitOnceWorks() {
		requestToAssignLimit(BigDecimal.TEN);
		assertEquals(BigDecimal.TEN, creditCard().availableLimit());
	}

	@Test(expected = IllegalStateException.class)
	public void assigningLimitTwiceThrowsException() {
		requestToAssignLimit(BigDecimal.TEN);
		requestToAssignLimit(BigDecimal.TEN);
	}

	@Test
	public void withdrawingOnceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.ONE);
		requestWithdrawal(BigDecimal.ONE);
		assertRecordedStepNames(ASSIGN, WITHDRAW);
		assertEquals(BigDecimal.ZERO, creditCard().availableLimit());
	}

	@Test
	public void assigningAndWithdrawingTheSameEqualsZero() {
		requestToAssignLimit(BigDecimal.ONE);
		requestWithdrawal(BigDecimal.ONE);
		assertEquals(BigDecimal.ZERO, creditCard().availableLimit());
	}

	@Test(expected = IllegalStateException.class)
	public void assigningAndWithdrawingAndAssigningThrowsException() {
		requestToAssignLimit(BigDecimal.ONE);
		requestWithdrawal(BigDecimal.ONE);
		requestToAssignLimit(BigDecimal.ONE);
	}

	@Test(expected = IllegalStateException.class)
	public void withdrawingTooMuchThrowsException() {
		requestToAssignLimit(BigDecimal.ONE);
		requestWithdrawal(new BigDecimal(2));
	}

	@Test(expected = IllegalStateException.class)
	public void withdrawingTooOftenThrowsException() {
		requestToAssignLimit(new BigDecimal(100));

		for (int i = 1; i <= 90; i++) {
			requestWithdrawal(BigDecimal.ONE);
		}
	}

	@Test
	public void withdrawingTooOftenOnceProducesCorrectResult() {
		requestToAssignLimit(new BigDecimal(50));

		for (int i = 1; i <= 46; i++) {
			try {
				requestWithdrawal(BigDecimal.ONE);
			} catch (IllegalStateException e) {
				assertEquals(new BigDecimal(5), creditCard().availableLimit());
				return;
			}
		}
		fail();
	}

	@Test
	public void withdrawingOftenWorksWhenCycleIsClosed() {
		requestToAssignLimit(new BigDecimal(100));

		for (int i = 1; i <= 44; i++) {
			requestWithdrawal(BigDecimal.ONE);
		}

		cardModelRunner.handleCommand(new RequestToCloseCycle());

		for (int i = 1; i <= 44; i++) {
			requestWithdrawal(BigDecimal.ONE);
		}
	}

	@Test
	public void repayingOnceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		requestRepay(BigDecimal.ONE);
		assertRecordedStepNames(ASSIGN, WITHDRAW, REPAY);
		assertEquals(BigDecimal.TEN, creditCard().availableLimit());
	}

	@Test
	public void repayingTwiceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		requestRepay(BigDecimal.ONE);
		requestRepay(BigDecimal.ONE);
		assertRecordedStepNames(ASSIGN, WITHDRAW, REPAY, REPAY);
		assertEquals(new BigDecimal(11), creditCard().availableLimit());
	}

	@Test
	public void assigningWithdrawingAndRepayingWorks() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		requestRepay(BigDecimal.TEN);
		assertEquals(new BigDecimal(19), creditCard().availableLimit());
	}

	@Test
	public void withdrawingWorksAfterRepaying() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		requestRepay(BigDecimal.ONE);
		requestWithdrawal(BigDecimal.ONE);
		requestWithdrawal(BigDecimal.ONE);
		assertRecordedStepNames(ASSIGN, WITHDRAW, REPAY, WITHDRAW_AGAIN, REPEAT, WITHDRAW);
		assertEquals(new BigDecimal(8), creditCard().availableLimit());
	}

	protected void assertRecordedStepNames(String... expectedStepNames) {
		String[] actualStepNames = modelRunner.getRecordedStepNames();
		assertArrayEquals(expectedStepNames, actualStepNames);
	}

	private void requestToAssignLimit(BigDecimal amount) {
		cardModelRunner.handleCommand(new RequestsToAssignLimit(amount));
	}

	public void requestWithdrawal(BigDecimal amount) {
		cardModelRunner.handleCommand(new RequestsWithdrawal(amount));
	}

	public void requestRepay(BigDecimal amount) {
		cardModelRunner.handleCommand(new RequestsRepay(amount));
	}
	
	private CreditCard creditCard() {
		return cardModelRunner.creditCard();
	}
}
