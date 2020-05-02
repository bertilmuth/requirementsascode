package creditcard_eventsourcing.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.Step;

/**
 * Based on code by Jakub Pilimon:
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 *
 */
public class CreditCard 
{
	public static final String assigningLimit = "Assigning limit";
	public static final String assigningLimitTwice = "Assigning limit twice";
	public static final String withdrawingCard = "Withdrawing card";
	public static final String withdrawingCardAgain = "Withdrawing card again";
	public static final String withdrawingCardTooOften = "Withdrawing card too often";
	public static final String closingCycle = "Closing cycle";
	public static final String repaying = "Repaying";
	public static final String repeating = "Repeating";
	
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
	
	public Optional<Step> latestStep() {
		Optional<Step> latestStep = modelRunner.getLatestStep();
		return latestStep;
	}
}