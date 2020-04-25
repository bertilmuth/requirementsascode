package creditcard_eventsourcing.model.command;

import java.math.BigDecimal;

public class RequestsToAssignLimit {
    private BigDecimal amount;
    public RequestsToAssignLimit(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getAmount() {
        return amount;
    }
}