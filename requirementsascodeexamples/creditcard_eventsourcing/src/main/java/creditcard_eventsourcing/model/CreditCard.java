package creditcard_eventsourcing.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.requirementsascode.Condition;
import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.StandardEventHandler;

import creditcard_eventsourcing.model.request.RequestToCloseCycle;
import creditcard_eventsourcing.model.request.RequestsRepay;
import creditcard_eventsourcing.model.request.RequestsToAssignLimit;
import creditcard_eventsourcing.model.request.RequestsWithdrawal;

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
    
    private void addingPendingEvents(StandardEventHandler eventHandler) {
	eventHandler.handleEvent();
	DomainEvent domainEvent = (DomainEvent) eventHandler.getEvent();
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
    public UUID getUuid() {
        return uuid;
    }
    
    /*
     * Validation methods
     */
    private boolean notEnoughMoneyToWithdraw(BigDecimal amount) {
        return availableLimit().compareTo(amount) < 0;
    }

    public BigDecimal availableLimit() {
        return initialLimit.subtract(usedLimit);
    }

    /*
     * Commands
     */
    
    class AssignsLimit implements Consumer<RequestsToAssignLimit> {
	@Override
	public void accept(RequestsToAssignLimit request) {
	    BigDecimal amount = request.getAmount();
	    handle(new LimitAssigned(uuid, amount, Instant.now()));
	}
    }
    
    class Withdraws implements Consumer<RequestsWithdrawal> {
	@Override
	public void accept(RequestsWithdrawal request) {
	    BigDecimal amount = request.getAmount();
	    if (notEnoughMoneyToWithdraw(amount)) {
		throw new IllegalStateException();
	    }
	    handle(new CardWithdrawn(uuid, amount, Instant.now()));
	}
    }

    class Repays implements Consumer<RequestsRepay> {
	@Override
	public void accept(RequestsRepay request) {
	    BigDecimal amount = request.getAmount();
	    handle(new CardRepaid(uuid, amount, Instant.now()));
	}
    }

    class ClosesCycle implements Consumer<RequestToCloseCycle> {
	@Override
	public void accept(RequestToCloseCycle request) {
	    handle(new CycleClosed(uuid, Instant.now()));
	}
    }

    class ThrowsAssignLimitException implements Consumer<RequestsToAssignLimit> {
	public void accept(RequestsToAssignLimit request) {
	    throw new IllegalStateException();
	}
    }

    class ThrowsTooManyWithdrawalsException implements Consumer<RequestsWithdrawal> {
	public void accept(RequestsWithdrawal request) {
	    throw new IllegalStateException();
	}
    }
    
    /*
     * Conditions
     */
    
    class TooManyWithdrawalsInCycle implements Condition{
	@Override
	public boolean evaluate() {
	    return withdrawals >= 45;
	}
    }
    
    class LimitAlreadyAssigned implements Condition {
	@Override
	public boolean evaluate() {
	    return initialLimit != null;
	}
    }
    
    class AccountOpen implements Condition{
	@Override
	public boolean evaluate() {
	    return true;
	}
    }
    
    
    /*
     * Event sourcing methods
     */
    public static CreditCard recreateFrom(UUID uuid, List<DomainEvent> events) {
	CreditCard creditCard = new CreditCard(uuid);
	events.forEach(ev -> creditCard.handle(ev));
	return creditCard;
    }

    private void handle(DomainEvent event) {
	modelRunner.reactTo(event);
    }
    
    public void flushEvents() {
        pendingEvents.clear();
    }
}