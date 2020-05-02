package creditcard_eventsourcing.model;

import static creditcard_eventsourcing.model.CreditCard.assigningLimit;
import static creditcard_eventsourcing.model.CreditCard.assigningLimitTwice;
import static creditcard_eventsourcing.model.CreditCard.closingCycle;
import static creditcard_eventsourcing.model.CreditCard.repaying;
import static creditcard_eventsourcing.model.CreditCard.repeating;
import static creditcard_eventsourcing.model.CreditCard.withdrawingCard;
import static creditcard_eventsourcing.model.CreditCard.withdrawingCardAgain;
import static creditcard_eventsourcing.model.CreditCard.withdrawingCardTooOften;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import org.requirementsascode.Condition;
import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.Step;

import creditcard_eventsourcing.model.command.RequestToCloseCycle;
import creditcard_eventsourcing.model.command.RequestsRepay;
import creditcard_eventsourcing.model.command.RequestsToAssignLimit;
import creditcard_eventsourcing.model.command.RequestsWithdrawal;
import creditcard_eventsourcing.persistence.CreditCardRepository;

public class CreditCardAggregateRoot {
	private static final String useCreditCard = "Use credit card";

	// Command types
	private static final Class<RequestsToAssignLimit> requestsToAssignLimit = RequestsToAssignLimit.class;
	private static final Class<RequestsWithdrawal> requestsWithdrawingCard = RequestsWithdrawal.class;
	private static final Class<RequestsRepay> requestsRepay = RequestsRepay.class;
	private static final Class<RequestToCloseCycle> requestToCloseCycle = RequestToCloseCycle.class;

	// Command handling methods
	private Function<RequestsToAssignLimit, DomainEvent> assignedLimit = this::assignedLimit;
	private Function<RequestsWithdrawal, DomainEvent> withdrawnCard = this::withdrawnCard;
	private Function<RequestsRepay, DomainEvent> repay = this::repay;
	private Function<RequestToCloseCycle, DomainEvent> closedCycle = this::closedCycle;
	private Consumer<RequestsToAssignLimit> throwsAssignLimitException = this::throwAssignLimitException;
	private Consumer<RequestsWithdrawal> throwsTooManyWithdrawalsException = this::throwTooManyWithdrawalsException;

	// Conditions
	private Condition tooManyWithdrawalsInCycle = this::tooManyWithdrawalsInCycle;
	private Condition limitAlreadyAssigned = this::limitAlreadyAssigned;
	private Condition accountIsOpen = this::accountIsOpen;

	// Other fields
	private final UUID uuid;
	private final CreditCardRepository repository;
	private final Model model;
	
	private CreditCard creditCard;

	public CreditCardAggregateRoot(UUID uuid, CreditCardRepository creditCardRepository) {
		this.uuid = uuid;
		this.repository = creditCardRepository;
		this.model = buildModel();
	}

	/**
	 * Builds the model that defines the credit card behavior.
	 * 
	 * @return the use case model
	 */
	private Model buildModel() {
		Model model = Model.builder()
		  .useCase(useCreditCard)
		    .basicFlow()
		    	.step(assigningLimit).user(requestsToAssignLimit).systemPublish(assignedLimit)
		    	.step(withdrawingCard).user(requestsWithdrawingCard).systemPublish(withdrawnCard).reactWhile(accountIsOpen)
		    	.step(repaying).user(requestsRepay).systemPublish(repay).reactWhile(accountIsOpen)
		    	
		    .flow("Withdraw again").after(repaying)
		    	.step(withdrawingCardAgain).user(requestsWithdrawingCard).systemPublish(withdrawnCard)
		    	.step(repeating).continuesAt(withdrawingCard)
		    	
		    .flow("Cycle is over").anytime()
		    	.step(closingCycle).on(requestToCloseCycle).systemPublish(closedCycle)
		    	
		    .flow("Limit can only be assigned once").condition(limitAlreadyAssigned)
		    	.step(assigningLimitTwice).user(requestsToAssignLimit).system(throwsAssignLimitException)
		    	
		    .flow("Too many withdrawals").condition(tooManyWithdrawalsInCycle) 
		    	.step(withdrawingCardTooOften).user(requestsWithdrawingCard).system(throwsTooManyWithdrawalsException)
		.build();
		return model;
	}

	/**
	 * This is the method to be called by clients for handling commands.
	 * Each command that is accepted will cause an event to be applied to the credit card.
	 * After that, the events are saved to the repository.
	 * 
	 * @param command the command to handle.
	 */
	public void accept(Object command) {
		loadCreditCard();
		Optional<DomainEvent> event = restoreStateAndHandle(command);
		applyToCreditCardIfPresent(event);
		saveCreditCard();
	}
	
	// Loads the credit card from the repository, replaying all saved events
	CreditCard loadCreditCard() {
		this.creditCard = repository().load(uuid());
		return creditCard;
	}
	
	// Creates a new model runner and restores the previous state.
	// The runner handles the command and returns an event.
	private Optional<DomainEvent> restoreStateAndHandle(Object command) {
		ModelRunner modelRunner = new ModelRunner().run(model());
		restorePreviousStateOf(modelRunner);
		return modelRunner.reactTo(command);
	}

	// If a command handler returned an event, apply it to the credit card 
	private void applyToCreditCardIfPresent(Optional<DomainEvent> event) {
		event.ifPresent(ev -> creditCard().apply(ev));
	}
	
	// Save all pending events of the credit card to the repository
	private void saveCreditCard() {
		repository().save(creditCard());
	}

	// Command handling methods (that return events)
	
	private DomainEvent assignedLimit(RequestsToAssignLimit request) {
		BigDecimal amount = request.getAmount();
		return new LimitAssigned(uuid(), amount, Instant.now());
	}

	private DomainEvent withdrawnCard(RequestsWithdrawal request) {
		BigDecimal amount = request.getAmount();
		if (creditCard().notEnoughMoneyToWithdraw(amount)) {
			throw new IllegalStateException();
		}
		return new CardWithdrawn(uuid(), amount, Instant.now());
	}

	private DomainEvent repay(RequestsRepay request) {
		BigDecimal amount = request.getAmount();
		return new CardRepaid(uuid(), amount, Instant.now());
	}
	
	private DomainEvent closedCycle(RequestToCloseCycle request) {
		return new CycleClosed(uuid(), Instant.now());
	}

	private void throwAssignLimitException(RequestsToAssignLimit request) {
		throw new IllegalStateException();
	}

	private void throwTooManyWithdrawalsException(RequestsWithdrawal request) {
		throw new IllegalStateException();
	}
	
	// Conditions
	
	boolean tooManyWithdrawalsInCycle() {
		return creditCard().tooManyWithdrawalsInCycle();
	}

	boolean limitAlreadyAssigned() {
		return creditCard().isLimitAlreadyAssigned();
	}

	boolean accountIsOpen() {
		return creditCard().isAccountOpen();
	}
	
	// Methods for restoring the previous state of the ModelRunner
	
	private void restorePreviousStateOf(ModelRunner modelRunner) {
		Optional<Step> latestStepOfEventModel = creditCard().latestStep();
		latestStepOfEventModel.ifPresent(step -> {
			Step latestStepOfCommandModel = findNamedStep(model(), step.getName());
			modelRunner.setLatestStep(latestStepOfCommandModel);
		});
	}

	private Step findNamedStep(Model model, final String stepName) {
		Step step = model.findUseCase(useCreditCard).findStep(stepName);
		return step;
	}

	private UUID uuid() {
		return uuid;
	}
	
	private CreditCard creditCard() {
		return creditCard;
	}

	private CreditCardRepository repository() {
		return repository;
	}

	private Model model() {
		return model;
	}
}
