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
import org.requirementsascode.TestModelRunner;

public class CreditCardModelRunnerTest {
    private TestModelRunner testModelRunner;
    private CreditCardModelRunner cardModelRunner;
    private CreditCard creditCard;

    @Before
    public void setUp() throws Exception {
	creditCard = new CreditCard(UUID.randomUUID());
	testModelRunner = new TestModelRunner();
	cardModelRunner = new CreditCardModelRunner(creditCard, testModelRunner);
    }

    @Test
    public void assigningLimitOnceWorksCorrectly() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	assertRecordedStepNames(ASSIGN);
	assertEquals(BigDecimal.TEN, creditCard.availableLimit());
    }

    @Test
    public void assigningLimitOnceCreatesOneEvent() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	assertEquals(1, creditCard.getPendingEvents().size());
    }

    @Test(expected = IllegalStateException.class)
    public void assigningLimitTwiceThrowsException() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
    }

    @Test
    public void withdrawingOnceWorksCorrectly() {
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	assertRecordedStepNames(ASSIGN, WITHDRAW);
	assertEquals(BigDecimal.ZERO, creditCard.availableLimit());
    }

    @Test
    public void assigningAndWithdrawingCreatesTwoEvents() {
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	assertEquals(2, creditCard.getPendingEvents().size());
    }

    @Test(expected = IllegalStateException.class)
    public void assigningAndWithdrawingAndAssigningThrowsException() {
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void withdrawingTooMuchThrowsException() {
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(new BigDecimal(2));
    }

    @Test(expected = IllegalStateException.class)
    public void withdrawingTooOftenThrowsException() {
	cardModelRunner.requestToAssignLimit(new BigDecimal(100));

	for (int i = 1; i <= 90; i++) {
	    cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	}
    }

    @Test
    public void withdrawingTooOftenOnceProducesCorrectResult() {
	cardModelRunner.requestToAssignLimit(new BigDecimal(50));

	for (int i = 1; i <= 46; i++) {
	    try {
		cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	    } catch (IllegalStateException e) {
		assertEquals(new BigDecimal(5), creditCard.availableLimit());
		return;
	    }
	}
	fail();
    }

    @Test
    public void withdrawingOftenWorksWhenCycleIsClosed() {
	cardModelRunner.requestToAssignLimit(new BigDecimal(100));

	for (int i = 1; i <= 44; i++) {
	    cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	}

	cardModelRunner.requestToCloseCycle();

	for (int i = 1; i <= 44; i++) {
	    cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	}
    }

    @Test
    public void repayingOnceWorksCorrectly() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	assertRecordedStepNames(ASSIGN, WITHDRAW, REPAY);
	assertEquals(BigDecimal.TEN, creditCard.availableLimit());
    }

    @Test
    public void repayingTwiceWorksCorrectly() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	assertRecordedStepNames(ASSIGN, WITHDRAW, REPAY, REPAY);
	assertEquals(new BigDecimal(11), creditCard.availableLimit());
    }

    @Test
    public void assigningWithdrawingAndRepayingCreatesThreeEvents() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	assertEquals(3, creditCard.getPendingEvents().size());
    }

    @Test
    public void withdrawingWorksAfterRepaying() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	assertRecordedStepNames(ASSIGN, WITHDRAW, REPAY, WITHDRAW_AGAIN, REPEAT, WITHDRAW);
	assertEquals(new BigDecimal(8), creditCard.availableLimit());
    }

    protected void assertRecordedStepNames(String... expectedStepNames) {
	String[] actualStepNames = testModelRunner.getRecordedStepNames();
	assertArrayEquals(expectedStepNames, actualStepNames);
    }
}
