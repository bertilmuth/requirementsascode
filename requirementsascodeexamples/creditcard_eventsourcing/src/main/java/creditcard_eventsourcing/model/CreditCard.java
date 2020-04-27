package creditcard_eventsourcing.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

/**
 * Based on code by Jakub Pilimon:
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 *
 */
public class CreditCard {

	private UUID uuid;
	private BigDecimal initialLimit;
	private BigDecimal usedLimit = BigDecimal.ZERO;
	private int withdrawals;
	private List<DomainEvent> pendingEvents = new ArrayList<>();
	private Model eventHandlingModel;
	private ModelRunner modelRunner;
	private Object latestEvent;

	public CreditCard(UUID uuid) {
		this.uuid = uuid;
		this.eventHandlingModel = model();
		this.modelRunner = new ModelRunner().run(eventHandlingModel);
	}

	private Model model() {
		return Model.builder()
			.on(LimitAssigned.class).system(this::limitAssigned)
			.on(CardWithdrawn.class).system(this::cardWithdrawn)
			.on(CardRepaid.class).system(this::cardRepaid)
			.on(CycleClosed.class).system(this::cycleWasClosed)
		.build();
	}

	public List<DomainEvent> getPendingEvents() {
		return pendingEvents;
	}

	/*
	 * State changing methods
	 */

	private void limitAssigned(LimitAssigned event) {
		this.initialLimit = event.getAmount();
	}

	private void cardWithdrawn(CardWithdrawn event) {
		this.usedLimit = usedLimit.add(event.getAmount());
		withdrawals++;
	}

	private void cardRepaid(CardRepaid event) {
		usedLimit = usedLimit.subtract(event.getAmount());
	}

	private void cycleWasClosed(CycleClosed event) {
		withdrawals = 0;
	}

	/*
	 * Getters
	 */
	public UUID uuid() {
		return uuid;
	}

	/*
	 * Validation methods
	 */
	boolean notEnoughMoneyToWithdraw(BigDecimal amount) {
		return availableLimit().compareTo(amount) < 0;
	}

	public BigDecimal availableLimit() {
		return initialLimit.subtract(usedLimit);
	}

	/*
	 * Conditions
	 */

	boolean tooManyWithdrawalsInCycle() {
		return withdrawals >= 45;
	}

	boolean limitAlreadyAssigned() {
		return initialLimit != null;
	}

	boolean accountOpen() {
		return true;
	}

	/*
	 * Event sourcing methods
	 */
	public static CreditCard recreateFrom(UUID uuid, List<DomainEvent> events) {
		CreditCard creditCard = new CreditCard(uuid);
		events.forEach(ev -> creditCard.mutate(ev));
		setLatestEvent(creditCard, events);
		return creditCard;
	}
	
	private static void setLatestEvent(CreditCard creditCard, List<DomainEvent> events) {
		creditCard.latestEvent = null;
		if(events.size() > 0) {
			creditCard.latestEvent = events.get(events.size()-1);
		}
	}
	public Optional<Object> latestEvent() {
		return Optional.ofNullable(latestEvent);
	}
	
	private void mutate(DomainEvent event) {
		modelRunner.reactTo(event);
	}
	
	void apply(DomainEvent event) {
		modelRunner.reactTo(event);
		pendingEvents.add(event);
	}

	public void flushEvents() {
		pendingEvents.clear();
	}
}