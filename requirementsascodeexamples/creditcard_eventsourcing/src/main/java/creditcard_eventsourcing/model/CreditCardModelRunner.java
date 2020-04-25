package creditcard_eventsourcing.model;

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

	// Command handlers
	private Consumer<RequestsToAssignLimit> assignsLimit;
	private Consumer<RequestsWithdrawal> withdraws;
	private Consumer<RequestsRepay> repays;
	private Consumer<RequestToCloseCycle> closesCycle;
	private Consumer<RequestsToAssignLimit> throwsAssignLimitException;
	private Consumer<RequestsWithdrawal> throwsTooManyWithdrawalsException;

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

	public void requestToCloseCycle() {
		handleCommand(new RequestToCloseCycle());
	}

	private void assignCardDependentFields() { 
		this.assignsLimit = creditCard::assignLimit;
		this.withdraws = creditCard::withdraw;
		this.repays = creditCard::repay;
		this.closesCycle = creditCard::closeCycle;
		this.throwsAssignLimitException = creditCard::throwAssignLimitException;
		this.throwsTooManyWithdrawalsException = creditCard::throwTooManyWithdrawalsException;
 
		this.tooManyWithdrawalsInCycle = creditCard::tooManyWithdrawalsInCycle;
		this.limitAlreadyAssigned = creditCard::limitAlreadyAssigned;
		this.accountOpen = creditCard::accountOpen;
	}
}
