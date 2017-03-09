package shoppingfxexample.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import shoppingfxexample.domain.PaymentDetails;
import shoppingfxexample.usecase.event.EnterPaymentDetails;
import shoppingfxexample.usecase.event.GoBack;

public class DisplayPaymentDetailsFormController extends AbstractController{
    private PaymentDetails paymentDetails;
    
    @FXML
    private VBox vBox;
	
    @FXML
    private Button confirmButton;
    
    @FXML
    private Button backButton;

	@FXML
    void onConfirm(ActionEvent event) {
    	EnterPaymentDetails enterPaymentDetails =  new EnterPaymentDetails(paymentDetails);
    	useCaseRunner().reactTo(enterPaymentDetails);
    }
	
	@FXML
    void onBack(ActionEvent event) {
		GoBack goBack = new GoBack();
    	useCaseRunner().reactTo(goBack);
    }
    
	public void displayPaymentDetails() {
    	this.paymentDetails = new PaymentDetails();
	}
}