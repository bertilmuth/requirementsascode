package creditcard_eventsourcing.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.Consumer;

import org.requirementsascode.Condition;
import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import creditcard_eventsourcing.model.command.RequestToCloseCycle;
import creditcard_eventsourcing.model.command.RequestsRepay;
import creditcard_eventsourcing.model.command.RequestsToAssignLimit;
import creditcard_eventsourcing.model.command.RequestsWithdrawal;

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
	private Consumer<RequestsToAssignLimit> assignsLimit = this::assignLimit;
	private Consumer<RequestsWithdrawal> withdraws = this::withdraw;
	private Consumer<RequestsRepay> repays = this::repay;
	private Consumer<RequestToCloseCycle> closesCycle = this::closeCycle;
	private Consumer<RequestsToAssignLimit> throwsAssignLimitException = this::throwAssignLimitException;
	private Consumer<RequestsWithdrawal> throwsTooManyWithdrawalsException = this::throwTooManyWithdrawalsException;

	// Conditions
	private Condition tooManyWithdrawalsInCycle;
	private Condition limitAlreadyAssigned;
	private Condition accountOpen;

	// Other fields
	private CreditCard creditCard;
	private ModelRunner modelRunner;

	public CreditCardModelRunner(CreditCard creditCard, ModelRunner modelRunner) {
		this.creditCard = creditCard;
		this.modelRunner = modelRunner;

		assignCardDependentFields();
		modelRunner.run(model());
	}

	public Model model() {
		Model model = Model.builder()
		  .useCase("Use credit card")
		    .basicFlow()
		    	.step(ASSIGN).user(requestsToAssignLimit).system(assignsLimit)
		    	.step(WITHDRAW).user(requestsWithdrawal).system(withdraws).reactWhile(accountOpen)
		    	.step(REPAY).user(requestsRepay).system(repays).reactWhile(accountOpen)
		    	
		    .flow("Withdraw again").after(REPAY)
		    	.step(WITHDRAW_AGAIN).user(requestsWithdrawal).system(withdraws)
		    	.step(REPEAT).continuesAt(WITHDRAW)
		    	
		    .flow("Cycle is over").anytime()
		    	.step(CLOSE).on(requestToCloseCycle).system(closesCycle)
		    	
		    .flow("Assign limit twice").condition(limitAlreadyAssigned)
		    	.step(ASSIGN_TWICE).user(requestsToAssignLimit).system(throwsAssignLimitException)
		    	
		    .flow("Too many withdrawals").condition(tooManyWithdrawalsInCycle) 
		    	.step(WITHDRAW_TOO_OFTEN).user(requestsWithdrawal).system(throwsTooManyWithdrawalsException)
		.build();
		return model;
	}

	public void handleCommand(Object command) {
		modelRunner.reactTo(command);
	}

	private void assignCardDependentFields() {  
		this.tooManyWithdrawalsInCycle = creditCard::tooManyWithdrawalsInCycle;
		this.limitAlreadyAssigned = creditCard::limitAlreadyAssigned;
		this.accountOpen = creditCard::accountOpen;
	}
	
	// Command handling methods
	private void assignLimit(RequestsToAssignLimit request) {
		BigDecimal amount = request.getAmount();
		creditCard.handle(new LimitAssigned(creditCard.uuid(), amount, Instant.now()));
	}
	
	private void withdraw(RequestsWithdrawal request) {
		BigDecimal amount = request.getAmount();
		if (creditCard.notEnoughMoneyToWithdraw(amount)) {
			throw new IllegalStateException();
		}
		creditCard.handle(new CardWithdrawn(creditCard.uuid(), amount, Instant.now()));
	}
	
	private void repay(RequestsRepay request) {
		BigDecimal amount = request.getAmount();
		creditCard.handle(new CardRepaid(creditCard.uuid(), amount, Instant.now()));
	}
	
	private void closeCycle(RequestToCloseCycle request) {
		creditCard.handle(new CycleClosed(creditCard.uuid(), Instant.now()));
	}
	
	private void throwAssignLimitException(RequestsToAssignLimit request) {
		throw new IllegalStateException();
	}

	private void throwTooManyWithdrawalsException(RequestsWithdrawal request) {
		throw new IllegalStateException();
	}
}
