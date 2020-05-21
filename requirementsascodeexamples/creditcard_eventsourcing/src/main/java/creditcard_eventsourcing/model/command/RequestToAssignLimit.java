package creditcard_eventsourcing.model.command;

import java.math.BigDecimal;

public class RequestToAssignLimit {
    private BigDecimal amount;
    public RequestToAssignLimit(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getAmount() {
        return amount;
    }
}