package creditcard_eventsourcing;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import creditcard_eventsourcing.model.CreditCardAggregateRoot;
import creditcard_eventsourcing.model.command.RequestToAssignLimit;
import creditcard_eventsourcing.model.command.RequestWithdrawal;
import creditcard_eventsourcing.persistence.EventStore;

/**
 * Based on code by Jakub Pilimon:
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 *
 */
@SpringBootApplication
@EnableScheduling
public class EventsourcingApplication {

	private final EventStore eventStore;

	public EventsourcingApplication(EventStore eventStore) {
		this.eventStore = eventStore;
	}

	public static void main(String[] args) {
		SpringApplication.run(EventsourcingApplication.class, args);
	}

	@Scheduled(fixedRate = 2000)
	public void randomCards() {
		CreditCardAggregateRoot cardModelRunner = new CreditCardAggregateRoot(UUID.randomUUID(), eventStore);
		cardModelRunner.accept(new RequestToAssignLimit(BigDecimal.TEN));
		cardModelRunner.accept(new RequestWithdrawal(BigDecimal.ONE));
	}
}
