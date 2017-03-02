package shoppingfxexample.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import shoppingfxexample.domain.PaymentDetails;
import shoppingfxexample.usecase.event.EnterPaymentDetails;

public class DisplayPaymentDetailsFormController extends AbstractController{
    private PaymentDetails paymentDetails;
    
    @FXML
    private VBox vBox;
	
    @FXML
    private Button confirmButton;

	@FXML
    void onConfirm(ActionEvent event) {
    	EnterPaymentDetails enterPaymentDetails =  new EnterPaymentDetails(paymentDetails);
    	useCaseRunner().reactTo(enterPaymentDetails);
    }
    
	public void displayPaymentDetails() {
    	this.paymentDetails = new PaymentDetails();
	}
}