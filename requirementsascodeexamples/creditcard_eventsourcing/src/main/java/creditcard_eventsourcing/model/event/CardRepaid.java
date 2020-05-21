package creditcard_eventsourcing.model.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Based on code by Jakub Pilimon: 
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 *
 */
public class CardRepaid implements DomainEvent {

    private final UUID cardNo;
    private final BigDecimal amount;
    private final Instant timestamp;

    public CardRepaid(UUID cardNo, BigDecimal amount, Instant timestamp) {
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
        return "card-repaid";
    }
}
