package creditcard_eventsourcing.model.command;

import java.math.BigDecimal;

public class RequestWithdrawal {
	private BigDecimal amount;

	public RequestWithdrawal(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}