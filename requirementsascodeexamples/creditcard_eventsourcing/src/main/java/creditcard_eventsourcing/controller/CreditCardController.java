package creditcard_eventsourcing.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import creditcard_eventsourcing.model.CreditCardAggregateRoot;
import creditcard_eventsourcing.persistence.CreditCardRepository;

/**
 * Based on code by Jakub Pilimon: 
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 *
 */
@RestController
class CreditCardController {
	@Autowired
	CreditCardRepository repository;

	@GetMapping("/cards")
	List<CreditCardAggregateRoot> creditCardList() {
		List<CreditCardAggregateRoot> creditCards = new ArrayList<>();
		Set<UUID> uuids = repository.uuids();
		for (UUID uuid : uuids) {
			CreditCardAggregateRoot creditCard = new CreditCardAggregateRoot(uuid, repository);
			creditCards.add(creditCard);
		}
		return creditCards;
	}
}