package creditcard_eventsourcing.model.request;

import java.math.BigDecimal;

public class RequestsWithdrawal {
    private BigDecimal amount;
    public RequestsWithdrawal(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getAmount() {
        return amount;
    }
}