package creditcard_eventsourcing.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

/**
 * Based on code by Jakub Pilimon: 
 * https://gitlab.com/pilloPl/eventsourced-credit-cards/blob/4329a0aac283067f1376b3802e13f5a561f18753
 * 
 * @author b_muth
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
        this.eventHandlingModel = 
        	Model.builder()
        		.when(this::limitNotAssigned).handles(LimitAssigned.class).with(this::limitAssigned)
        		.when(this::limitAlreadyAssigned).handles(LimitAssigned.class).with(this::throwsException)
        		.when(this::notTooManyWithdrawalsInCycle).handles(CardWithdrawn.class).with(this::cardWithdrawn)
        		.when(this::tooManyWithdrawalsInCycle).handles(CardWithdrawn.class).with(this::throwsException)
        		.handles(CardRepaid.class).with(this::cardRepaid)
        		.handles(CycleClosed.class).with(this::cycleWasClosed)
        	.build();
        this.modelRunner = new ModelRunner();
        modelRunner.adaptSystemReaction(tr -> {
            tr.trigger();
            DomainEvent domainEvent = (DomainEvent)tr.getEvent();
            pendingEvents.add(domainEvent);
        });
        modelRunner.run(eventHandlingModel);
    }

    public void assignLimit(BigDecimal amount) { 
        handle(new LimitAssigned(uuid, amount, Instant.now()));
    }

    private void limitAssigned(LimitAssigned event) {
        this.initialLimit = event.getAmount(); 
    }

    public void withdraw(BigDecimal amount) {
        if(notEnoughMoneyToWithdraw(amount)) {
            throw new IllegalStateException();
        }
        handle(new CardWithdrawn(uuid, amount, Instant.now()));
    }

    private void cardWithdrawn(CardWithdrawn event) {
        this.usedLimit = usedLimit.add(event.getAmount());
        withdrawals++;
    }

    public void repay(BigDecimal amount) {
        handle(new CardRepaid(uuid, amount, Instant.now()));
    }

    private void cardRepaid(CardRepaid event) {
        usedLimit = usedLimit.subtract(event.getAmount());
    }

    public void cycleClosed() {
        handle(new CycleClosed(uuid, Instant.now()));
    }

    private void cycleWasClosed(CycleClosed event) {
        withdrawals = 0;
    }
    
    public void throwsException(Object event) {
	throw new IllegalStateException(); 
    }
    
    private boolean limitAlreadyAssigned(ModelRunner r) {
        return initialLimit != null;
    }

    private boolean limitNotAssigned(ModelRunner r) {
        return !limitAlreadyAssigned(r);
    }

    private boolean tooManyWithdrawalsInCycle(ModelRunner r) {
        return withdrawals >= 45;
    }
    
    private boolean notTooManyWithdrawalsInCycle(ModelRunner r) {
        return !tooManyWithdrawalsInCycle(r);
    }

    private boolean notEnoughMoneyToWithdraw(BigDecimal amount) {
        return availableLimit().compareTo(amount) < 0;
    }


    public BigDecimal availableLimit() {
        return initialLimit.subtract(usedLimit);
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<DomainEvent> getPendingEvents() {
        return pendingEvents;
    }

    public void flushEvents() {
        pendingEvents.clear();
    }

    public static CreditCard recreateFrom(UUID uuid, List<DomainEvent> events) {
	CreditCard creditCard = new CreditCard(uuid);
	events.forEach(ev -> creditCard.handle(ev));
	return creditCard;
    }

    private void handle(DomainEvent event) {
	modelRunner.reactTo(event);
    }
}