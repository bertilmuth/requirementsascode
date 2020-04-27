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

	public CreditCard(UUID uuid, List<DomainEvent> events) {
		this.uuid = uuid;
		this.eventHandlingModel = model();
		this.modelRunner = new ModelRunner().run(eventHandlingModel);
		replay(uuid, events);
	}

	/**
	 * Builds a model that maps received events to method calls
	 * 
	 * @return the event to method call mapping model
	 */
	private Model model() {
		return Model.builder()
			.on(LimitAssigned.class).system(event -> assignLimit(event.getAmount()))
			.on(CardWithdrawn.class).system(event -> withdraw(event.getAmount()))
			.on(CardRepaid.class).system(event -> repay(event.getAmount()))
			.on(CycleClosed.class).system(event -> closeCycle())
		.build();
	}

	/*
	 * State changing methods
	 */

	private void assignLimit(BigDecimal amount) {
		this.initialLimit = amount;
	}

	private void withdraw(BigDecimal amount) {
		this.usedLimit = usedLimit.add(amount);
		withdrawals++;
	}

	private void repay(BigDecimal amount) {
		usedLimit = usedLimit.subtract(amount);
	}

	private void closeCycle() {
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

	boolean isLimitAlreadyAssigned() {
		return initialLimit != null;
	}

	boolean isAccountOpen() {
		return true;
	}

	/*
	 * Event sourcing methods
	 */
	public List<DomainEvent> getPendingEvents() {
		return pendingEvents;
	}
	
	private void replay(UUID uuid, List<DomainEvent> events) {
		events.forEach(this::mutate);
		saveLatestEventOf(events);
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
	
	private void saveLatestEventOf(List<DomainEvent> events) {
		latestEvent = null;
		if(events.size() > 0) {
			latestEvent = events.get(events.size()-1);
		}
	}
	
	public Optional<Object> latestEvent() {
		return Optional.ofNullable(latestEvent);
	}
}