package creditcard_eventsourcing;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import creditcard_eventsourcing.model.CreditCard;
import creditcard_eventsourcing.persistence.CreditCardRepository;

/**
 * Based on code by Jakub Pilimon: 
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 * 
 * @author b_muth
 *
 */
@SpringBootApplication
@EnableScheduling
public class EventsourcingApplication {

    private final CreditCardRepository creditCardRepository;

    public EventsourcingApplication(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(EventsourcingApplication.class, args);
	}


    @Scheduled(fixedRate = 2000)
    public void randomCards() {
        CreditCard card = new CreditCard(UUID.randomUUID());
        card.assignLimit(BigDecimal.TEN);
        card.withdraw(BigDecimal.ONE);
        creditCardRepository.save(card);
    }
}
