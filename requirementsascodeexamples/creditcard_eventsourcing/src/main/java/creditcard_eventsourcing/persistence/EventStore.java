package creditcard_eventsourcing.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import creditcard_eventsourcing.model.event.DomainEvent;

/**
 * Based on code by Jakub Pilimon:
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 * 
 * @author b_muth
 *
 */
@Repository
public class EventStore {
	private final Map<UUID, List<DomainEvent>> eventStream = new HashMap<>();

	public void save(UUID uuid, List<DomainEvent> currentStream) {
		eventStream.put(uuid, currentStream);
	}

	public List<DomainEvent> loadEvents(UUID uuid) {
		return eventStream.getOrDefault(uuid, new ArrayList<>());
	}

	public Set<UUID> uuids() {
		return eventStream.keySet();
	}
}
