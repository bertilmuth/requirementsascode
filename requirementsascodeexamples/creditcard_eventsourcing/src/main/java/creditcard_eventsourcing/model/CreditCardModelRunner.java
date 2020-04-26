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
	private Condition tooManyWithdrawalsInCycle;
	private Condition limitAlreadyAssigned;
	private Condition accountOpen;

	// Other fields
	private UUID uuid;
	private ModelRunner modelRunner;
	private CreditCardRepository repository;

	public CreditCardModelRunner(UUID uuid, ModelRunner modelRunner, CreditCardRepository creditCardRepository) {
		this.uuid = uuid;
		this.modelRunner = modelRunner;
		this.repository = creditCardRepository;
	}

	public Model createModelFor(CreditCard creditCard) {
		this.tooManyWithdrawalsInCycle = creditCard::tooManyWithdrawalsInCycle;
		this.limitAlreadyAssigned = creditCard::limitAlreadyAssigned;
		this.accountOpen = creditCard::accountOpen;
		
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

	public void handleCommand(Object command) {
		CreditCard creditCard = repository.load(uuid);

		String latestStepName = saveLatestStepName();
		Model model = createModelFor(creditCard);
		modelRunner.run(model);
		restoreLatestStep(model, latestStepName);
		
		Optional<Object> event = modelRunner.reactTo(command);		
		event.ifPresent(ev -> creditCard.apply((DomainEvent) ev));
		
		repository.save(creditCard);
	}
	private String saveLatestStepName() {
		Optional<Step> latestStep = modelRunner.getLatestStep();
		String stepName = latestStep.map(s->s.getName()).orElse(null);
		return stepName;
	}

	private void restoreLatestStep(Model model, String stepName) {
		if(stepName != null) {
			Optional<Step> step = model.getSteps().stream().filter(s -> stepName.equals(s.getName())).findAny();
			step.ifPresent(modelRunner::setLatestStep);
		} 
	}

	CreditCard creditCard() {
		CreditCard creditCard = repository.load(uuid);
		return creditCard;
	}

	// Command handling methods
	private Object assignLimit(RequestsToAssignLimit request) {
		BigDecimal amount = request.getAmount();
		return new LimitAssigned(uuid, amount, Instant.now());
	}

	private Object withdraw(RequestsWithdrawal request) {
		BigDecimal amount = request.getAmount();
		if (creditCard().notEnoughMoneyToWithdraw(amount)) {
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
}
