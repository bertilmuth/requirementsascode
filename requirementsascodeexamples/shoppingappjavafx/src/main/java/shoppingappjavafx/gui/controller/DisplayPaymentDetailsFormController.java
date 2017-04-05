package shoppingappjavafx.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import shoppingappjavafx.domain.PaymentDetails;
import shoppingappjavafx.usecase.userevent.EnterPaymentDetails;
import shoppingappjavafx.usecase.userevent.GoBack;

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
    	EnterPaymentDetails enterPaymentDetails =  new EnterPaymentDetails(paymentDetails);
    	useCaseModelRunner().reactTo(enterPaymentDetails);
    }
	
	@FXML
    void onBack(ActionEvent event) {
		GoBack goBack = new GoBack();
    	useCaseModelRunner().reactTo(goBack);
    }
    
	public void displayPaymentDetails() {
    	this.paymentDetails = new PaymentDetails();
	}
}