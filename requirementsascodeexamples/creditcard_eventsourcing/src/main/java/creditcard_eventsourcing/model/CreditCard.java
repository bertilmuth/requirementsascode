package creditcard_eventsourcing.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.StepToBeRun;

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

	public CreditCard(UUID uuid) {
		this.uuid = uuid;
		this.eventHandlingModel = model();
		this.modelRunner = new ModelRunner();
		modelRunner.handleWith(this::addingPendingEvents);
		modelRunner.run(eventHandlingModel);
	}

	private Model model() {
		return Model.builder()
			.on(LimitAssigned.class).system(this::limitAssigned)
			.on(CardWithdrawn.class).system(this::cardWithdrawn)
			.on(CardRepaid.class).system(this::cardRepaid)
			.on(CycleClosed.class).system(this::cycleWasClosed)
		.build();
	}

	private void addingPendingEvents(StepToBeRun stepToBeRun) {
		stepToBeRun.run();
		DomainEvent domainEvent = (DomainEvent) stepToBeRun.getMessage().get();
		pendingEvents.add(domainEvent);
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
		events.forEach(ev -> creditCard.handle(ev));
		return creditCard;
	}

	void handle(DomainEvent event) {
		modelRunner.reactTo(event);
	}

	public void flushEvents() {
		pendingEvents.clear();
	}
}