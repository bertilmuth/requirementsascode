package creditcard_eventsourcing.model.command;

import java.math.BigDecimal;

public class RequestRepay {
	private BigDecimal amount;

	public RequestRepay(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}