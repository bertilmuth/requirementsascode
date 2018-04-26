package org.requirementsascode.creditcard_eventsourcing.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.requirementsascode.creditcard_eventsourcing.model.CreditCard;
import org.requirementsascode.creditcard_eventsourcing.persistence.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Based on code by Jakub Pilimon: 
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 * 
 * @author b_muth
 *
 */
@RestController
class CreditCardController {
	@Autowired
	CreditCardRepository repository;

	@GetMapping("/cards")
	List<CreditCard> creditCardList() {
		List<CreditCard> creditCards = new ArrayList<>();
		Set<UUID> uuids = repository.getUuids();
		for (UUID uuid : uuids) {
			CreditCard creditCard = repository.load(uuid);
			creditCards.add(creditCard);
		}
		return creditCards;
	}
}