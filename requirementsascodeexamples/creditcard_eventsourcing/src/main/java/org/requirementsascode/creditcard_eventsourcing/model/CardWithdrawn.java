package org.requirementsascode.creditcard_eventsourcing.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Based on code by Jakub Pilimon: 
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 * 
 * @author b_muth
 *
 */
public class CardWithdrawn implements DomainEvent {

    private final UUID cardNo;
    private final BigDecimal amount;
    private final Instant timestamp;

    public CardWithdrawn(UUID cardNo, BigDecimal amount, Instant timestamp) {
        this.cardNo = cardNo;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public UUID getCardNo() {
        return cardNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }


    @Override
    public String getType() {
        return "card-withdrawn";
    }
}
