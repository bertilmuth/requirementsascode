package creditcard_eventsourcing.model;

import java.math.BigDecimal;
import java.util.function.Predicate;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import creditcard_eventsourcing.model.CreditCard.AssignsLimit;
import creditcard_eventsourcing.model.CreditCard.ClosesCycle;
import creditcard_eventsourcing.model.CreditCard.Repays;
import creditcard_eventsourcing.model.CreditCard.ThrowsAssignLimitException;
import creditcard_eventsourcing.model.CreditCard.ThrowsTooManyWithdrawalsException;
import creditcard_eventsourcing.model.CreditCard.Withdraws;
import creditcard_eventsourcing.model.request.RequestToCloseCycle;
import creditcard_eventsourcing.model.request.RequestsRepay;
import creditcard_eventsourcing.model.request.RequestsToAssignLimit;
import creditcard_eventsourcing.model.request.RequestsWithdrawal;


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
    
    // Request types
    private static final Class<RequestsToAssignLimit> requestsToAssignLimit = RequestsToAssignLimit.class;
    private static final Class<RequestsWithdrawal> requestsWithdrawal = RequestsWithdrawal.class;
    private static final Class<RequestsRepay> requestsRepay = RequestsRepay.class;
    private static final Class<RequestToCloseCycle> requestToCloseCycle = RequestToCloseCycle.class;
    
    // Commands
    private AssignsLimit assignsLimit;
    private Withdraws withdraws;
    private Repays repays;
    private ClosesCycle closesCycle;
    private ThrowsAssignLimitException throwsAssignLimitException;
    private ThrowsTooManyWithdrawalsException throwsTooManyWithdrawalsException;
    
    // Conditions
    private Predicate<ModelRunner> tooManyWithdrawalsInCycle;
    private Predicate<ModelRunner> limitAlreadyAssigned;
    private Predicate<ModelRunner> accountOpen;
    
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
	    	
	    .flow("Assign limit twice").when(limitAlreadyAssigned)
	    	.step(ASSIGN_TWICE).user(requestsToAssignLimit).system(throwsAssignLimitException)
	    	
	    .flow("Too many withdrawals").when(tooManyWithdrawalsInCycle) 
	    	.step(WITHDRAW_TOO_OFTEN).user(requestsWithdrawal).system(throwsTooManyWithdrawalsException)
	.build();
	return model;
    }
    
    public void request(Object request) {
	modelRunner.reactTo(request);
    }
    
    public void requestToAssignLimit(BigDecimal amount) {
	request(new RequestsToAssignLimit(amount));
    }
    
    public void requestWithdrawal(BigDecimal amount) {
	request(new RequestsWithdrawal(amount));
    }
    
    public void requestRepay(BigDecimal amount) {
	request(new RequestsRepay(amount));
    }
    
    public void requestToCloseCycle() {
	request(new RequestToCloseCycle());
    }
    
    private void assignCardDependentFields() {
	assignsLimit = creditCard.new AssignsLimit();
	withdraws = creditCard.new Withdraws();
	repays = creditCard.new Repays();
	closesCycle = creditCard.new ClosesCycle();
	throwsAssignLimitException = creditCard.new ThrowsAssignLimitException();
	throwsTooManyWithdrawalsException = creditCard.new ThrowsTooManyWithdrawalsException();
	
	tooManyWithdrawalsInCycle = creditCard.new TooManyWithdrawalsInCycle();
	limitAlreadyAssigned = creditCard.new LimitAlreadyAssigned();
	accountOpen = creditCard.new AccountOpen();
    }
}
