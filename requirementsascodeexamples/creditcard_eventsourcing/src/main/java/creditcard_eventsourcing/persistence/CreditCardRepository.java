package creditcard_eventsourcing.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import creditcard_eventsourcing.model.CreditCard;
import creditcard_eventsourcing.model.DomainEvent;

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

    public void save(CreditCard creditCard) {
        List<DomainEvent> currentStream = uniqueEvents(creditCard.uuid());
        currentStream.addAll(creditCard.pendingEvents());
        eventStream.put(creditCard.uuid(), currentStream);
        creditCard.flushEvents();
    }

    public CreditCard load(UUID uuid) {
        return new CreditCard(uuid, uniqueEvents(uuid));
    }

		private List<DomainEvent> uniqueEvents(UUID uuid) {
			return eventStream.getOrDefault(uuid, new ArrayList<>());
		}
    
    public Set<UUID> uuids(){
    	return eventStream.keySet();
    }
}
