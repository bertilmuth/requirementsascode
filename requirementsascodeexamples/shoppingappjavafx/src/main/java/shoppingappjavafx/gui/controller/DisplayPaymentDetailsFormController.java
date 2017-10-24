package shoppingappjavafx.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import shoppingappjavafx.domain.PaymentDetails;
import shoppingappjavafx.usecase.userevent.EntersPaymentDetails;
import shoppingappjavafx.usecase.userevent.SignalsToGoBack;

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
    	useCaseModelRunner().reactTo(enterPaymentDetails);
    }
	
	@FXML
    void onBack(ActionEvent event) {
		SignalsToGoBack goBack = new SignalsToGoBack();
    	useCaseModelRunner().reactTo(goBack);
    }
    
	public void displayPaymentDetails() {
    	this.paymentDetails = new PaymentDetails();
	}
}