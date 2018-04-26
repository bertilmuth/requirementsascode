package org.requirementsascode.creditcard_eventsourcing.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.requirementsascode.creditcard_eventsourcing.model.CreditCard;
import org.requirementsascode.creditcard_eventsourcing.model.DomainEvent;
import org.springframework.stereotype.Repository;

/**
 * Based on code by Jakub Pilimon: 
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 * 
 * @author b_muth
 *
 */
@Repository
public class CreditCardRepository {

    private final Map<UUID, List<DomainEvent>> eventStream = new HashMap<>();

    public CreditCardRepository() {
    }


    public void save(CreditCard creditCard) {
        List<DomainEvent> currentStream = eventStream.getOrDefault(creditCard.getUuid(), new ArrayList<>());
        currentStream.addAll(creditCard.getPendingEvents());
        eventStream.put(creditCard.getUuid(), currentStream);
        creditCard.flushEvents();
    }

    public CreditCard load(UUID uuid) {
        return CreditCard.recreateFrom(uuid, eventStream.getOrDefault(uuid, new ArrayList<>()));
    }
    
    public Set<UUID> getUuids(){
    	return eventStream.keySet();
    }
}
