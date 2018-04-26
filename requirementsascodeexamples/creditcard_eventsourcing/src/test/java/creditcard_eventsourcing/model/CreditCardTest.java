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
}
