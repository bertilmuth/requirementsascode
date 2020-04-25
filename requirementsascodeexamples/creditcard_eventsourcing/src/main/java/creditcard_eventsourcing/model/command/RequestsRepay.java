package creditcard_eventsourcing.model.command;

import java.math.BigDecimal;

public class RequestsRepay {
    private BigDecimal amount;
    public RequestsRepay(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getAmount() {
        return amount;
    }
}