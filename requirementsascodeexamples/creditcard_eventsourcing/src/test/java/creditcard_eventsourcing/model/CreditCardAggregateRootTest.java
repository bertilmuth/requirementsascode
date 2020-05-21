package creditcard_eventsourcing.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import creditcard_eventsourcing.model.command.RequestToCloseCycle;
import creditcard_eventsourcing.model.command.RequestRepay;
import creditcard_eventsourcing.model.command.RequestToAssignLimit;
import creditcard_eventsourcing.model.command.RequestWithdrawal;
import creditcard_eventsourcing.persistence.EventStore;

public class CreditCardAggregateRootTest {
	private EventStore eventStore;
	private UUID uuid;

	@Before
	public void setUp() throws Exception {
		this.eventStore = new EventStore();
		this.uuid = uuid();
	}
	private CreditCardAggregateRoot aggregateRoot() {
		CreditCardAggregateRoot aggregateRoot = new CreditCardAggregateRoot(uuid, eventStore);
		return aggregateRoot;
	}
	
	private UUID uuid() {
		return UUID.randomUUID();
	}

	@Test
	public void assigningLimitOnceWorksCorrectly() {
		CreditCardAggregateRoot aggregateRoot = requestToAssignLimit(BigDecimal.TEN);
		assertEquals(BigDecimal.TEN, aggregateRoot.loadCreditCard().getAvailableLimit());
	}

	@Test
	public void assigningLimitOnceWorks() {
		CreditCardAggregateRoot aggregateRoot = requestToAssignLimit(BigDecimal.TEN);
		assertEquals(BigDecimal.TEN, aggregateRoot.loadCreditCard().getAvailableLimit());
	}

	@Test(expected = IllegalStateException.class)
	public void assigningLimitTwiceThrowsException() {
		requestToAssignLimit(BigDecimal.TEN);
		requestToAssignLimit(BigDecimal.TEN);
	}

	@Test
	public void withdrawingOnceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.ONE);
		CreditCardAggregateRoot aggregateRoot = requestWithdrawal(BigDecimal.ONE);
		assertEquals(BigDecimal.ZERO, aggregateRoot.loadCreditCard().getAvailableLimit());
	}

	@Test
	public void assigningAndWithdrawingTheSameEqualsZero() {
		requestToAssignLimit(BigDecimal.ONE);
		CreditCardAggregateRoot aggregateRoot = requestWithdrawal(BigDecimal.ONE);
		assertEquals(BigDecimal.ZERO, aggregateRoot.loadCreditCard().getAvailableLimit());
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
		CreditCardAggregateRoot aggregateRoot = requestToAssignLimit(new BigDecimal(50));

		for (int i = 1; i <= 46; i++) {
			try {
				requestWithdrawal(BigDecimal.ONE);
			} catch (IllegalStateException e) {
				assertEquals(new BigDecimal(5), aggregateRoot.loadCreditCard().getAvailableLimit());
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

		aggregateRoot().accept(new RequestToCloseCycle());

		for (int i = 1; i <= 44; i++) {
			requestWithdrawal(BigDecimal.ONE);
		}
	}

	@Test
	public void repayingOnceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		CreditCardAggregateRoot aggregateRoot = requestRepay(BigDecimal.ONE);
		assertEquals(BigDecimal.TEN, aggregateRoot.loadCreditCard().getAvailableLimit());
	}

	@Test
	public void repayingTwiceWorksCorrectly() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		requestRepay(BigDecimal.ONE);
		CreditCardAggregateRoot aggregateRoot = requestRepay(BigDecimal.ONE);
		assertEquals(new BigDecimal(11), aggregateRoot.loadCreditCard().getAvailableLimit());
	}

	@Test
	public void assigningWithdrawingAndRepayingWorks() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		CreditCardAggregateRoot aggregateRoot = requestRepay(BigDecimal.TEN);
		assertEquals(new BigDecimal(19), aggregateRoot.loadCreditCard().getAvailableLimit());
	}

	@Test
	public void withdrawingWorksAfterRepaying() {
		requestToAssignLimit(BigDecimal.TEN);
		requestWithdrawal(BigDecimal.ONE);
		requestRepay(BigDecimal.ONE);
		requestWithdrawal(BigDecimal.ONE);
		CreditCardAggregateRoot aggregateRoot = requestWithdrawal(BigDecimal.ONE);
		assertEquals(new BigDecimal(8), aggregateRoot.loadCreditCard().getAvailableLimit());
	}

	private CreditCardAggregateRoot requestToAssignLimit(BigDecimal amount) {
		CreditCardAggregateRoot aggregateRoot = aggregateRoot();
		aggregateRoot.accept(new RequestToAssignLimit(amount));
		return aggregateRoot;
	}

	public CreditCardAggregateRoot requestWithdrawal(BigDecimal amount) {
		CreditCardAggregateRoot aggregateRoot = aggregateRoot();
		aggregateRoot.accept(new RequestWithdrawal(amount));
		return aggregateRoot;
	}

	public CreditCardAggregateRoot requestRepay(BigDecimal amount) {
		CreditCardAggregateRoot aggregateRoot = aggregateRoot();
		aggregateRoot.accept(new RequestRepay(amount));
		return aggregateRoot;
	}
}
