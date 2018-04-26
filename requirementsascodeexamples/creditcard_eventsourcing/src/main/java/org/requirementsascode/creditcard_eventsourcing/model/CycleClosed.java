package org.requirementsascode.creditcard_eventsourcing.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Based on code by Jakub Pilimon: 
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 * 
 * @author b_muth
 *
 */
public class CycleClosed implements DomainEvent {

    private final UUID cardNo;
    private final Instant timestamp;

    public CycleClosed(UUID cardNo, Instant timestamp) {
        this.cardNo = cardNo;
        this.timestamp = timestamp;
    }

    public UUID getCardNo() {
        return cardNo;
    }

    public Instant getTimestamp() {
        return timestamp;
    }


    @Override
    public String getType() {
        return "cycle-closed";
    }
}
