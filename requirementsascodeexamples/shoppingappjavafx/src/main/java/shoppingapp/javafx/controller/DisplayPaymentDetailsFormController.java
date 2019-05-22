package shoppingapp.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import shoppingapp.boundary.internal.domain.PaymentDetails;
import shoppingapp.command.EntersPaymentDetails;
import shoppingapp.command.SignalsToGoBack;

public class DisplayPaymentDetailsFormController extends AbstractController{
    private PaymentDetails paymentDetails;
    
    @FXML
    private Button backButton;
    
    @FXML
    private VBox vBox;
	
    @FXML
    private Button confirmButton;


	@FXML
    void onConfirm(ActionEvent event) {
    	EntersPaymentDetails enterPaymentDetails =  new EntersPaymentDetails(paymentDetails);
    	javafxDriver().reactTo(enterPaymentDetails);
    }
	
	@FXML
    void onBack(ActionEvent event) {
		SignalsToGoBack goBack = new SignalsToGoBack();
    	javafxDriver().reactTo(goBack);
    }
    
	public void displayPaymentDetails() {
    	this.paymentDetails = new PaymentDetails();
	}
}