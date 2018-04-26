package creditcard_eventsourcing.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.exception.UnhandledException;

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
    public void assigningLimitTwiceThrowsException() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.TEN);

	boolean illegalStateExceptionThrown = false;
	try {
	    card.assignLimit(BigDecimal.TEN);
	} catch (UnhandledException e) {
	    illegalStateExceptionThrown = e.getCause() instanceof IllegalStateException;
	}
	assertTrue(illegalStateExceptionThrown);
    }
    
    @Test
    public void withdrawingOnceWorksCorrectly() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(BigDecimal.TEN);
	card.withdraw(BigDecimal.ONE);
	assertEquals(new BigDecimal(9), card.availableLimit());
    }

    @Test
    public void withdrawingTooOftenThrowsException() {
	CreditCard card = new CreditCard(UUID.randomUUID());
	card.assignLimit(new BigDecimal(100));

	boolean illegalStateExceptionThrown = false;
	try {
	    for (int i = 1; i <= 90; i++) {
		card.withdraw(BigDecimal.ONE);
	    }
	} catch (UnhandledException e) {
	    illegalStateExceptionThrown = e.getCause() instanceof IllegalStateException;
	}
	assertTrue(illegalStateExceptionThrown);
    }
}
