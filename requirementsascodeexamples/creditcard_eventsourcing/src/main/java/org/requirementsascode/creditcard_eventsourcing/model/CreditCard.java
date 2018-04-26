package org.requirementsascode.creditcard_eventsourcing.model;

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
        		.handles(LimitAssigned.class).with(this::limitAssigned)
        		.handles(CardWithdrawn.class).with(this::cardWithdrawn)
        		.handles(CardRepaid.class).with(this::cardRepaid)
        		.handles(CycleClosed.class).with(this::cycleWasClosed)
        	.build();
        this.modelRunner = new ModelRunner();
        modelRunner.run(eventHandlingModel);
    }


    public void assignLimit(BigDecimal amount) { 
        if(limitAlreadyAssigned()) {  
            throw new IllegalStateException(); 
        }
        limitAssigned(new LimitAssigned(uuid, amount, Instant.now()));
    }

    private void limitAssigned(LimitAssigned event) {
        this.initialLimit = event.getAmount(); 
        pendingEvents.add(event);
    }

    public void withdraw(BigDecimal amount) {
        if(notEnoughMoneyToWithdraw(amount)) {
            throw new IllegalStateException();
        }
        if(tooManyWithdrawalsInCycle()) {
            throw new IllegalStateException();
        }
        cardWithdrawn(new CardWithdrawn(uuid, amount, Instant.now()));
    }

    private void cardWithdrawn(CardWithdrawn event) {
        this.usedLimit = usedLimit.add(event.getAmount());
        withdrawals++;
        pendingEvents.add(event);
    }


    public void repay(BigDecimal amount) {
        cardRepaid(new CardRepaid(uuid, amount, Instant.now()));
    }

    private void cardRepaid(CardRepaid event) {
        usedLimit = usedLimit.subtract(event.getAmount());
        pendingEvents.add(event);
    }

    public void cycleClosed() {
        cycleWasClosed(new CycleClosed(uuid, Instant.now()));
    }

    private void cycleWasClosed(CycleClosed event) {
        withdrawals = 0;
        pendingEvents.add(event);
    }

    private boolean limitAlreadyAssigned() {
        return initialLimit != null;
    }

    private boolean tooManyWithdrawalsInCycle() {
        return withdrawals >= 45;
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
