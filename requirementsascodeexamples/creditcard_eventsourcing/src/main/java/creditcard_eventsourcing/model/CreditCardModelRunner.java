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

public class CreditCardModelRunner {
	// Step names
	public static final String ASSIGN = "Assign limit";
	public static final String ASSIGN_TWICE = "Assign limit twice";
	public static final String WITHDRAW = "Withdraw";
	public static final String WITHDRAW_AGAIN = "Withdraw again";
	public static final String WITHDRAW_TOO_OFTEN = "Withdraw too often";
	public static final String CLOSE = "Close cycle";
	public static final String REPEAT = "Repeat";
	public static final String REPAY = "Repay";

	// Command types
	private static final Class<RequestsToAssignLimit> requestsToAssignLimit = RequestsToAssignLimit.class;
	private static final Class<RequestsWithdrawal> requestsWithdrawal = RequestsWithdrawal.class;
	private static final Class<RequestsRepay> requestsRepay = RequestsRepay.class;
	private static final Class<RequestToCloseCycle> requestToCloseCycle = RequestToCloseCycle.class;

	// Command handling methods
	private Function<RequestsToAssignLimit, Object> assignsLimit = this::assignLimit;
	private Function<RequestsWithdrawal, Object> withdraws = this::withdraw;
	private Function<RequestsRepay, Object> repays = this::repay;
	private Function<RequestToCloseCycle, Object> closesCycle = this::closeCycle;
	private Consumer<RequestsToAssignLimit> throwsAssignLimitException = this::throwAssignLimitException;
	private Consumer<RequestsWithdrawal> throwsTooManyWithdrawalsException = this::throwTooManyWithdrawalsException;

	// Conditions
	private Condition tooManyWithdrawalsInCycle = this::tooManyWithdrawalsInCycle;
	private Condition limitAlreadyAssigned = this::limitAlreadyAssigned;
	private Condition accountOpen = this::accountOpen;

	// Other fields
	private UUID uuid;
	private CreditCardRepository repository;
	private CreditCard creditCard;

	public CreditCardModelRunner(UUID uuid, CreditCardRepository creditCardRepository) {
		this.uuid = uuid;
		this.repository = creditCardRepository;
	}

	private Model model() {
		Model model = Model.builder()
		  .useCase("Use credit card")
		    .basicFlow()
		    	.step(ASSIGN).user(requestsToAssignLimit).systemPublish(assignsLimit)
		    	.step(WITHDRAW).user(requestsWithdrawal).systemPublish(withdraws).reactWhile(accountOpen)
		    	.step(REPAY).user(requestsRepay).systemPublish(repays).reactWhile(accountOpen)
		    	
		    .flow("Withdraw again").after(REPAY)
		    	.step(WITHDRAW_AGAIN).user(requestsWithdrawal).systemPublish(withdraws)
		    	.step(REPEAT).continuesAt(WITHDRAW)
		    	
		    .flow("Cycle is over").anytime()
		    	.step(CLOSE).on(requestToCloseCycle).systemPublish(closesCycle)
		    	
		    .flow("Assign limit twice").condition(limitAlreadyAssigned)
		    	.step(ASSIGN_TWICE).user(requestsToAssignLimit).system(throwsAssignLimitException)
		    	
		    .flow("Too many withdrawals").condition(tooManyWithdrawalsInCycle) 
		    	.step(WITHDRAW_TOO_OFTEN).user(requestsWithdrawal).system(throwsTooManyWithdrawalsException)
		.build();
		return model;
	}

	/**
	 * This is the method to be called by clients for handling commands.
	 * Each command that is accepted will cause an event to be published to the credit card.
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
	
	private Object assignLimit(RequestsToAssignLimit request) {
		BigDecimal amount = request.getAmount();
		return new LimitAssigned(uuid, amount, Instant.now());
	}

	private Object withdraw(RequestsWithdrawal request) {
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

	private Object closeCycle(RequestToCloseCycle request) {
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
		return creditCard().tooManyWithdrawalsInCycle();
	}

	boolean limitAlreadyAssigned() {
		return creditCard().limitAlreadyAssigned();
	}

	boolean accountOpen() {
		return creditCard().accountOpen();
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
			stepName = ASSIGN;
		} else if (event instanceof CardWithdrawn) {
			stepName = WITHDRAW;
		} else if (event instanceof CardRepaid) {
			stepName = REPAY;
		} else if (event instanceof CycleClosed) {
			stepName = CLOSE;
		}
		Optional<Step> step = findNamedStep(model, stepName);
		return step;
	}

	private Optional<Step> findNamedStep(Model model, final String stepName) {
		Optional<Step> step = model.getSteps().stream().filter(s -> s.getName().equals(stepName)).findAny();
		return step;
	}
}
