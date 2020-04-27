package creditcard_eventsourcing.model;

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
	
	// Step names
	public static final String assigningLimit = "Assigning limit";
	public static final String assigningLimitTwice = "Assigning limit twice";
	public static final String withdrawingCard = "Withdrawing card";
	public static final String withdrawingCardAgain = "Withdrawing card again";
	public static final String withdrawingCardTooOften = "Withdrawing card too often";
	public static final String closingCycle = "Closing cycle";
	public static final String repaying = "Repaying";
	public static final String repeating = "Repeating";

	// Command types
	private static final Class<RequestsToAssignLimit> requestsToAssignLimit = RequestsToAssignLimit.class;
	private static final Class<RequestsWithdrawal> requestsWithdrawingCard = RequestsWithdrawal.class;
	private static final Class<RequestsRepay> requestsRepay = RequestsRepay.class;
	private static final Class<RequestToCloseCycle> requestToCloseCycle = RequestToCloseCycle.class;

	// Command handling methods
	private Function<RequestsToAssignLimit, Object> assignedLimit = this::assignedLimit;
	private Function<RequestsWithdrawal, Object> withdrawnCard = this::withdrawnCard;
	private Function<RequestsRepay, Object> repay = this::repay;
	private Function<RequestToCloseCycle, Object> closedCycle = this::closedCycle;
	private Consumer<RequestsToAssignLimit> throwsAssignLimitException = this::throwAssignLimitException;
	private Consumer<RequestsWithdrawal> throwsTooManyWithdrawalsException = this::throwTooManyWithdrawalsException;

	// Conditions
	private Condition tooManyWithdrawalsInCycle = this::tooManyWithdrawalsInCycle;
	private Condition limitAlreadyAssigned = this::limitAlreadyAssigned;
	private Condition accountIsOpen = this::accountIsOpen;

	// Other fields
	private UUID uuid;
	private CreditCardRepository repository;
	private CreditCard creditCard;

	public CreditCardAggregateRoot(UUID uuid, CreditCardRepository creditCardRepository) {
		this.uuid = uuid;
		this.repository = creditCardRepository;
	}

	/**
	 * Builds the model that defines the credit card behavior.
	 * 
	 * @return the use case model
	 */
	private Model model() {
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
		Optional<Object> event = handle(command);		
		applyEventToCreditCardIfPresent(event);
		saveCreditCard();
	}
	
	// Loads the credit card from the repository, replaying all saved events
	private void loadCreditCard() {
		creditCard = repository.load(uuid);
	}
	
	// Creates a new model runner and restores the previous state.
	// The runner handles the command and returns an event.
	private Optional<Object> handle(Object command) {
		Model model = model();
		ModelRunner modelRunner = new ModelRunner().run(model);
		restorePreviousState(modelRunner, model);
		return modelRunner.reactTo(command);
	}

	// If a command handler returned an event, apply it to the credit card 
	private void applyEventToCreditCardIfPresent(Optional<Object> event) {
		event.ifPresent(ev -> creditCard.apply((DomainEvent) ev));
	}
	
	// Save all pending events of the credit card to the repository
	private void saveCreditCard() {
		repository.save(creditCard);
	}

	// Command handling methods (that return events)
	
	private Object assignedLimit(RequestsToAssignLimit request) {
		BigDecimal amount = request.getAmount();
		return new LimitAssigned(uuid, amount, Instant.now());
	}

	private Object withdrawnCard(RequestsWithdrawal request) {
		BigDecimal amount = request.getAmount();
		if (creditCard.notEnoughMoneyToWithdraw(amount)) {
			throw new IllegalStateException();
		}
		return new CardWithdrawn(uuid, amount, Instant.now());
	}

	private Object repay(RequestsRepay request) {
		BigDecimal amount = request.getAmount();
		return new CardRepaid(uuid, amount, Instant.now());
	}

	private Object closedCycle(RequestToCloseCycle request) {
		return new CycleClosed(uuid, Instant.now());
	}

	private void throwAssignLimitException(RequestsToAssignLimit request) {
		throw new IllegalStateException();
	}

	private void throwTooManyWithdrawalsException(RequestsWithdrawal request) {
		throw new IllegalStateException();
	}
	
	// Conditions
	
	boolean tooManyWithdrawalsInCycle() {
		return creditCard.tooManyWithdrawalsInCycle();
	}

	boolean limitAlreadyAssigned() {
		return creditCard.limitAlreadyAssigned();
	}

	boolean accountIsOpen() {
		return creditCard.accountOpen();
	}

	public CreditCard creditCard() {
		loadCreditCard();
		return creditCard;
	}
	
	// Methods for restoring the previous state of the ModelRunner
	
	private void restorePreviousState(ModelRunner modelRunner, Model model) {
		Optional<Object> latestEvent = creditCard.latestEvent();
		latestEvent.ifPresent(event -> {
			Optional<Step> latestStep = stepThatCausedEvent(model, event);
			latestStep.ifPresent(step -> modelRunner.setLatestStep(step));
		});
	}

	private Optional<Step> stepThatCausedEvent(Model model, Object event) {
		String stepName = null;
		if (event instanceof LimitAssigned) {
			stepName = assigningLimit;
		} else if (event instanceof CardWithdrawn) {
			stepName = withdrawingCard;
		} else if (event instanceof CardRepaid) {
			stepName = repaying;
		} else if (event instanceof CycleClosed) {
			stepName = closingCycle;
		}
		Step step = findNamedStep(model, stepName);
		return Optional.ofNullable(step);
	}

	private Step findNamedStep(Model model, final String stepName) {
		Step step = model.findUseCase(useCreditCard).findStep(stepName);
		return step;
	}
}
