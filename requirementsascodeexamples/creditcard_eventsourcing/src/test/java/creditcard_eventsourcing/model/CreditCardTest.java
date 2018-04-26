package creditcard_eventsourcing.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class CreditCardTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void assigningLimitOnceWorksCorrectly() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.TEN);
	assertEquals(BigDecimal.TEN, card.availableLimit());
    }
    
    @Test
    public void assigningLimitOnceCreatesOneEvent() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.TEN);
	assertEquals(1, card.getPendingEvents().size());
    }

    @Test(expected = IllegalStateException.class)
    public void assigningLimitTwiceThrowsException() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.TEN);
	card.assignLimit(BigDecimal.TEN);
    }

    @Test
    public void withdrawingOnceWorksCorrectly() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.ONE);
	card.withdraw(BigDecimal.ONE);
	assertEquals(BigDecimal.ZERO, card.availableLimit());
    }
    
    @Test
    public void assigningAndWithdrawingCreatesTwoEvents() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.ONE);
	card.withdraw(BigDecimal.ONE);
	assertEquals(2, card.getPendingEvents().size());
    }

    @Test(expected = IllegalStateException.class)
    public void withdrawingTooMuchThrowsException() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.ONE);
	card.withdraw(new BigDecimal(2));
    }

    @Test(expected = IllegalStateException.class)
    public void withdrawingTooOftenThrowsException() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(new BigDecimal(100));

	for (int i = 1; i <= 90; i++) {
	    card.withdraw(BigDecimal.ONE);
	}
    }

    @Test
    public void repayingOnceWorksCorrectly() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.TEN);
	card.withdraw(BigDecimal.ONE);
	card.repay(BigDecimal.ONE);
	assertEquals(BigDecimal.TEN, card.availableLimit());
    }
    
    @Test
    public void assigningWithdrawingAndRepayingCreatesThreeEvents() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.TEN);
	card.withdraw(BigDecimal.ONE);
	card.repay(BigDecimal.ONE);
	assertEquals(3, card.getPendingEvents().size());
    }
}
