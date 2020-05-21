package creditcard_eventsourcing.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.Step;

import creditcard_eventsourcing.model.event.CardRepaid;
import creditcard_eventsourcing.model.event.CardWithdrawn;
import creditcard_eventsourcing.model.event.CycleClosed;
import creditcard_eventsourcing.model.event.DomainEvent;
import creditcard_eventsourcing.model.event.LimitAssigned;

/**
 * Based on code by Jakub Pilimon:
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 *
 */
class CreditCard 
{
	static final String assigningLimit = "Assigning limit";
	static final String assigningLimitTwice = "Assigning limit twice";
	static final String withdrawingCard = "Withdrawing card";
	static final String withdrawingCardAgain = "Withdrawing card again";
	static final String withdrawingCardTooOften = "Withdrawing card too often";
	static final String closingCycle = "Closing cycle";
	static final String repaying = "Repaying";
	static final String repeating = "Repeating";
	
	private BigDecimal initialLimit;
	private BigDecimal usedLimit = BigDecimal.ZERO;
	private int withdrawals;
	
	private final UUID uuid;
	private final Model eventHandlingModel;
	private List<DomainEvent> pendingEvents = new ArrayList<>();
	private ModelRunner modelRunner;

	public CreditCard(UUID uuid, List<DomainEvent> events) {
		this.uuid = uuid;
		this.eventHandlingModel = buildModel();
		this.modelRunner = new ModelRunner().run(eventHandlingModel);
		replay(uuid, events);
	}
	
	/*
	 * UUID
	 */
	public UUID uuid() {
		return uuid;
	}

	/**
	 * Builds a model that maps received events to method calls
	 * 
	 * @return the event to method call mapping model
	 */
	private Model buildModel() {
		return Model.builder()
			.step(assigningLimit).on(LimitAssigned.class).system(event -> assignLimit(event.getAmount()))
			.step(withdrawingCard).on(CardWithdrawn.class).system(event -> withdraw(event.getAmount()))
			.step(repaying).on(CardRepaid.class).system(event -> repay(event.getAmount()))
			.step(closingCycle).on(CycleClosed.class).system(event -> closeCycle())
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
	 * Validation methods
	 */
	boolean notEnoughMoneyToWithdraw(BigDecimal amount) {
		return getAvailableLimit().compareTo(amount) < 0;
	}

	public BigDecimal getAvailableLimit() {
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
	public List<DomainEvent> pendingEvents() {
		return pendingEvents;
	}
	
	private void replay(UUID uuid, List<DomainEvent> events) {
		events.forEach(this::mutate);
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
	
	Optional<Step> latestStep() {
		Optional<Step> latestStep = modelRunner.getLatestStep();
		return latestStep;
	}
}