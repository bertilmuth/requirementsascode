package creditcard_eventsourcing.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import creditcard_eventsourcing.model.command.RequestToCloseCycle;
import creditcard_eventsourcing.model.command.RequestsRepay;
import creditcard_eventsourcing.model.command.RequestsToAssignLimit;
import creditcard_eventsourcing.model.command.RequestsWithdrawal;
import creditcard_eventsourcing.persistence.CreditCardRepository;

public class CreditCardModelRunnerTest {
	private CreditCardRepository repository;
	private UUID uuid;

	@Before
	public void setUp() throws Exception {
		this.repository = new CreditCardRepository();
		this.uuid = uuid();
	}
	private CreditCardModelRunner cardModelRunner() {
		CreditCardModelRunner cardModelRunner = new CreditCardModelRunner(uuid, repository);
		return cardModelRunner;
	}
	
	private UUID uuid() {
		return UUID.randomUUID();
	}

	@Test
	public void assigningLimitOnceWorksCorrectly() {
		CreditCardModelRunner cardModelRunner = requestToAssignLimit(BigDecimal.TEN);
		assertEquals(BigDecimal.TEN, cardModelRunner.creditCard().availableLimit());
	}

	@Test
	public void assigningLimitOnceWorks() {
		CreditCardModelRunner cardModelRunner = requestToAssignLimit(BigDecimal.TEN);
		assertEquals(BigDecimal.TEN, cardModelRunner.creditCard().availableLimit());
	}

	@Test(expected = IllegalStateException.class)
	public void assigningLimitTwiceThrowsException() {
		requestToAssignLimit(BigDecimal.TEN);
		requestToAssignLimit(BigDecimal.TEN);
	}

	@Test
	public void withdrawingOnceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.ONE);
		CreditCardModelRunner cardModelRunner = requestWithdrawal(BigDecimal.ONE);
		assertEquals(BigDecimal.ZERO, cardModelRunner.creditCard().availableLimit());
	}

	@Test
	public void assigningAndWithdrawingTheSameEqualsZero() {
		requestToAssignLimit(BigDecimal.ONE);
		CreditCardModelRunner cardModelRunner = requestWithdrawal(BigDecimal.ONE);
		assertEquals(BigDecimal.ZERO, cardModelRunner.creditCard().availableLimit());
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
		CreditCardModelRunner cardModelRunner = requestToAssignLimit(new BigDecimal(50));

		for (int i = 1; i <= 46; i++) {
			try {
				requestWithdrawal(BigDecimal.ONE);
			} catch (IllegalStateException e) {
				assertEquals(new BigDecimal(5), cardModelRunner.creditCard().availableLimit());
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

		cardModelRunner().accept(new RequestToCloseCycle());

		for (int i = 1; i <= 44; i++) {
			requestWithdrawal(BigDecimal.ONE);
		}
	}

	@Test
	public void repayingOnceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		CreditCardModelRunner cardModelRunner = requestRepay(BigDecimal.ONE);
		assertEquals(BigDecimal.TEN, cardModelRunner.creditCard().availableLimit());
	}

	@Test
	public void repayingTwiceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		requestRepay(BigDecimal.ONE);
		CreditCardModelRunner cardModelRunner = requestRepay(BigDecimal.ONE);
		assertEquals(new BigDecimal(11), cardModelRunner.creditCard().availableLimit());
	}

	@Test
	public void assigningWithdrawingAndRepayingWorks() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		CreditCardModelRunner cardModelRunner = requestRepay(BigDecimal.TEN);
		assertEquals(new BigDecimal(19), cardModelRunner.creditCard().availableLimit());
	}

	@Test
	public void withdrawingWorksAfterRepaying() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		requestRepay(BigDecimal.ONE);
		requestWithdrawal(BigDecimal.ONE);
		CreditCardModelRunner cardModelRunner = requestWithdrawal(BigDecimal.ONE);
		assertEquals(new BigDecimal(8), cardModelRunner.creditCard().availableLimit());
	}

	private CreditCardModelRunner requestToAssignLimit(BigDecimal amount) {
		CreditCardModelRunner cardModelRunner = cardModelRunner();
		cardModelRunner.accept(new RequestsToAssignLimit(amount));
		return cardModelRunner;
	}

	public CreditCardModelRunner requestWithdrawal(BigDecimal amount) {
		CreditCardModelRunner cardModelRunner = cardModelRunner();
		cardModelRunner.accept(new RequestsWithdrawal(amount));
		return cardModelRunner;
	}

	public CreditCardModelRunner requestRepay(BigDecimal amount) {
		CreditCardModelRunner cardModelRunner = cardModelRunner();
		cardModelRunner.accept(new RequestsRepay(amount));
		return cardModelRunner;
	}
}
